package io.mosip.pms.payment.service.impl;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.mosip.kernel.core.logger.spi.Logger;
import io.mosip.pms.common.constant.ApiAccessibleExceptionConstant;
import io.mosip.pms.common.entity.*;
import io.mosip.pms.common.exception.ApiAccessibleException;
import io.mosip.pms.common.repository.PartnerBalanceRepository;
import io.mosip.pms.common.repository.PartnerPaymentTransactionsRepository;
import io.mosip.pms.common.repository.PartnerPrnRepository;
import io.mosip.pms.common.repository.PartnerServiceRepository;
import io.mosip.pms.common.util.PMSLogger;
import io.mosip.pms.common.util.RestUtil;
import io.mosip.pms.common.util.UserDetailUtil;
import io.mosip.pms.device.util.AuditUtil;
import io.mosip.pms.partner.constant.ErrorCode;
import io.mosip.pms.partner.constant.PartnerServiceAuditEnum;
import io.mosip.pms.partner.exception.PartnerServiceException;
import io.mosip.pms.payment.constant.PaymentConstants;
import io.mosip.pms.payment.request.dto.PrnRequest;
import io.mosip.pms.payment.request.dto.ValidatePrnRequest;
import io.mosip.pms.payment.response.dto.PrnResponse;
import io.mosip.pms.payment.response.dto.ValidatePrnResponse;
import io.mosip.pms.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {
    private static final Logger LOGGER = PMSLogger.getLogger(PaymentServiceImpl.class);

    @Autowired
    RestUtil restUtil;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    PartnerPrnRepository partnerPrnRepository;

    @Autowired
    PartnerServiceRepository partnerRepository;

    @Autowired
    PartnerPaymentTransactionsRepository paymentRepository;

    @Autowired
    PartnerBalanceRepository balanceRepository;

    @Autowired
    AuditUtil auditUtil;

    @Value("${pmp.prn.generate.rest.uri}")
    private String generatePrnUrl;

    @Value("${pmp.prn.validate.rest.uri}")
    private String validatePrnUrl;


    public PrnResponse generatePrn(PrnRequest request) {
        Partner partnerData = getValidPartner(request.getPartnerId(), false);
        request.setFullName(partnerData.getName());
        request.setNin(null);
        request.setService(PaymentConstants.SERVICE_NEWAID);
        try {
            Map<String, Object> apiResponse = restUtil.postApi(
                    generatePrnUrl, null, "", "",
                    MediaType.APPLICATION_JSON, request, Map.class
            );
            if (apiResponse == null || apiResponse.isEmpty()) {
                throw new ApiAccessibleException("EXTERNAL_API_ERROR", "Provider returned an empty response");
            }
            PrnResponse prnResponse = mapper.convertValue(apiResponse, PrnResponse.class);
            if (isPrnPresent(prnResponse)) {
                try {
                    PartnerPrn partnerPrn = mapPartnerPrnFromRequest(request, prnResponse);
                    partnerPrnRepository.save(partnerPrn);
                } catch (Exception dbEx) {
                    LOGGER.error("PRN generated but failed to save to local DB: {}", dbEx.getMessage());
                }
            }
            return prnResponse;
        } catch (IllegalArgumentException e) {
            LOGGER.error("Mapping error for PRN response: {}", e.getMessage());
            throw new ApiAccessibleException("PARSE_ERROR", "Failed to process partner response data");

        } catch (RestClientException e) {
            LOGGER.error("Network error calling PRN service: {}", e.getMessage());
            throw new ApiAccessibleException("SERVICE_UNAVAILABLE", "Payment gateway is currently unreachable");

        } catch (Exception e) {
            LOGGER.error("Unexpected error in generatePrn: ", e);
            throw new ApiAccessibleException("INTERNAL_SERVER_ERROR", "An unexpected error occurred processing the PRN");
        }
    }

    /**
     * Null-safe check for nested PRN data
     */
    private boolean isPrnPresent(PrnResponse response) {
        return response != null
                && response.getResponse() != null
                && response.getResponse().getData() != null
                && response.getResponse().getData().getPrn() != null;
    }

    public ValidatePrnResponse validatePrn(ValidatePrnRequest request){
        Partner partnerData = getValidPartner(request.getPartnerId(),false);
        ValidatePrnResponse validatePrnResponse = null;
        try {
            Map<String, Object> apiResponse = restUtil.postApi(validatePrnUrl, null,
                    "", "", MediaType.APPLICATION_JSON, request, Map.class);
            validatePrnResponse = mapper.convertValue(apiResponse, ValidatePrnResponse.class);
        } catch (Exception e) {
            LOGGER.error("Error occured while parsing the response from template api", e.getLocalizedMessage());
        }
        if(validatePrnResponse == null) {
            throw new ApiAccessibleException(ApiAccessibleExceptionConstant.TEMPLATE_NOT_FOUND.getErrorCode(),
                    ApiAccessibleExceptionConstant.TEMPLATE_NOT_FOUND.getErrorMessage());
        }

        if(validatePrnResponse.getResponse().getStatusCode().equalsIgnoreCase(PaymentConstants.NOTPAID_STATUSCODE)){
            PartnerPrn partnerPrn = getpartnerprndetails(request.getPrn(), request.getPartnerId());
            partnerPrn.setStatus(PaymentConstants.VALIDATED_NOT_PAID);
            partnerPrn.setRemarks(PaymentConstants.AMOUNT_NOT_PAID);
            partnerPrn.setUpdBy((getLoggedInUserId()));
            partnerPrn.setUpdDtimes(LocalDateTime.now());
            partnerPrnRepository.save(partnerPrn);
        } else if (validatePrnResponse.getResponse().getStatusCode().equalsIgnoreCase(PaymentConstants.PAID_STATUSCODE)) {
            PartnerPrn partnerPrn = getpartnerprndetails(request.getPrn(), request.getPartnerId());
            partnerPrn.setStatus(PaymentConstants.VALIDATED_PAID);
            partnerPrn.setRemarks(PaymentConstants.AMOUNT_PAID);
            partnerPrn.setUpdBy((getLoggedInUserId()));
            partnerPrn.setUpdDtimes(LocalDateTime.now());
            partnerPrnRepository.save(partnerPrn);
            Boolean isTransactionAlreadyExist = paymentRepository.isTransactionAlreadyExist(request.getPrn());
            if(!isTransactionAlreadyExist){
                PartnerPaymentTransactions transaction = mapTransactionFromResponse(request,validatePrnResponse);
                paymentRepository.save(transaction);
                addBalance(request,validatePrnResponse);
            }

        }
        return validatePrnResponse;
    }

private void addBalance(ValidatePrnRequest request, ValidatePrnResponse response) {
    BigDecimal creditedAmount = Optional.ofNullable(response.getResponse().getAmountPaid())
            .orElse(BigDecimal.ZERO);
    PartnerBalance balanceDetails = balanceRepository.findById(request.getPartnerId())
            .map(existingBalance -> {
                BigDecimal current = Optional.ofNullable(existingBalance.getBalance()).orElse(BigDecimal.ZERO);
                existingBalance.setBalance(current.add(creditedAmount));
                existingBalance.setUpdBy(getLoggedInUserId());
                existingBalance.setUpdDtimes(LocalDateTime.now());
                return existingBalance;
            })
            .orElseGet(() -> {
                return mapBalanceDetails(request, response);
            });
    balanceRepository.save(balanceDetails);
}

    private PartnerBalance mapBalanceDetails(ValidatePrnRequest request, ValidatePrnResponse response){
        PartnerBalance balanceDetails = new PartnerBalance();
        balanceDetails.setPartnerId(request.getPartnerId());
        balanceDetails.setBalance(Optional.ofNullable(response.getResponse().getAmountPaid()).orElse(BigDecimal.ZERO));
        balanceDetails.setCrBy((getLoggedInUserId()));
        balanceDetails.setCrDtimes(LocalDateTime.now());
        return balanceDetails;
    }

    private PartnerPrn mapPartnerPrnFromRequest(PrnRequest request, PrnResponse response){
        PartnerPrn partnerPrn = new PartnerPrn();
        partnerPrn.setPartnerId(request.getPartnerId());
        partnerPrn.setPrn(response.getResponse().getData().getPrn());
        partnerPrn.setStatus(PaymentConstants.GENERATED);
        partnerPrn.setAmount(response.getResponse().getData().getAmount());
        partnerPrn.setServiceCode(request.getServiceCode());
        partnerPrn.setRemarks(PaymentConstants.PRN_GENERATED);
        partnerPrn.setCrBy((getLoggedInUserId()));
        partnerPrn.setCrDtimes(LocalDateTime.now());
        return partnerPrn;
    }

    private PartnerPaymentTransactions mapTransactionFromResponse(ValidatePrnRequest request, ValidatePrnResponse response){
        PartnerPaymentTransactions transaction = new PartnerPaymentTransactions();
        transaction.setTransactionId(response.getResponse().getPrn());
        transaction.setPartnerId(request.getPartnerId());
        transaction.setEntryType(PaymentConstants.CREDIT);
        transaction.setAmount(response.getResponse().getAmountPaid());
        transaction.setSourceSystem(PaymentConstants.PMS);
        transaction.setDescription(PaymentConstants.AMOUNT_CREDITED);
        transaction.setLogDtimes(LocalDateTime.now());
        transaction.setCrBy((getLoggedInUserId()));
        transaction.setCrDtimes(LocalDateTime.now());
        return transaction;
    }

    private PartnerPrn getpartnerprndetails(String prn , String partnerId){
        PartnerPrnId id = new PartnerPrnId(partnerId, prn);
        return partnerPrnRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Record not found for id: " + id));
    }

    
    private String getLoggedInUserId() {
        return UserDetailUtil.getLoggedInUserId();
    }

    private Partner getValidPartner(String partnerId, boolean isToRetrieve) {
        Optional<Partner> partnerById = partnerRepository.findById(partnerId);
        if (partnerById.isEmpty()) {
            auditUtil.setAuditRequestDto(PartnerServiceAuditEnum.RETRIVE_PARTNER_FAILURE, partnerId, "partnerId");
            throw new PartnerServiceException(ErrorCode.PARTNER_DOES_NOT_EXIST_EXCEPTION.getErrorCode(),
                    ErrorCode.PARTNER_DOES_NOT_EXIST_EXCEPTION.getErrorMessage());
        }
        if (!isToRetrieve) {
            if (!partnerById.get().getIsActive()) {
                auditUtil.setAuditRequestDto(PartnerServiceAuditEnum.RETRIVE_PARTNER_FAILURE, partnerId, "partnerId");
                throw new PartnerServiceException(ErrorCode.PARTNER_NOT_ACTIVE_EXCEPTION.getErrorCode(),
                        ErrorCode.PARTNER_NOT_ACTIVE_EXCEPTION.getErrorMessage());
            }
        }
        return partnerById.get();
    }

}
