package io.mosip.pms.payment.service.impl;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.mosip.kernel.core.logger.spi.Logger;
import io.mosip.pms.common.constant.EventType;
import io.mosip.pms.common.dto.PageResponseDto;
import io.mosip.pms.common.dto.SearchDto;
import io.mosip.pms.common.dto.Type;
import io.mosip.pms.common.entity.*;
import io.mosip.pms.common.exception.ApiAccessibleException;
import io.mosip.pms.common.helper.SearchHelper;
import io.mosip.pms.common.helper.WebSubPublisher;
import io.mosip.pms.common.repository.PartnerBalanceRepository;
import io.mosip.pms.common.repository.PartnerPaymentTransactionsRepository;
import io.mosip.pms.common.repository.PartnerPrnRepository;
import io.mosip.pms.common.repository.PartnerServiceRepository;
import io.mosip.pms.common.util.*;
import io.mosip.pms.device.util.AuditUtil;
import io.mosip.pms.partner.constant.ErrorCode;
import io.mosip.pms.partner.constant.PartnerConstants;
import io.mosip.pms.partner.constant.PartnerServiceAuditEnum;
import io.mosip.pms.partner.exception.PartnerServiceException;
import io.mosip.pms.payment.constant.PaymentConstants;
import io.mosip.pms.payment.constant.PaymentServiceAuditEnum;
import io.mosip.pms.payment.request.dto.PrnRequest;
import io.mosip.pms.payment.request.dto.ValidatePrnRequest;
import io.mosip.pms.payment.response.dto.PrnResponse;
import io.mosip.pms.payment.response.dto.ValidatePrnResponse;
import io.mosip.pms.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private static final Logger LOGGER = PMSLogger.getLogger(PaymentServiceImpl.class);
    private final RestUtil restUtil;
    private final ObjectMapper mapper;
    private final PartnerPrnRepository partnerPrnRepository;
    private final PartnerServiceRepository partnerRepository;
    private final PartnerPaymentTransactionsRepository paymentRepository;
    private final PartnerBalanceRepository balanceRepository;
    private final AuditUtil auditUtil;
    private final WebSubPublisher webSubPublisher;
    private final SearchHelper searchHelper;
    private final PageUtils pageUtils;

    @Value("${pmp.prn.generate.rest.uri}")
    private String generatePrnUrl;

    @Value("${pmp.prn.validate.rest.uri}")
    private String validatePrnUrl;

    @Value("${pmp.prn.minimumBalanceRequired.value}")
    private BigDecimal minimumBalanceRequired;


    @Override
    public PrnResponse generatePrn(PrnRequest request) {
        Partner partnerData = getValidPartner(request.getPartnerId(), false);
        if(request.getAmount().compareTo(minimumBalanceRequired) < 0){
            BigDecimal currentBalance = getBalance(request.getPartnerId());
            BigDecimal accumulatedBalance = currentBalance.add(request.getAmount());
            if(accumulatedBalance.compareTo(minimumBalanceRequired)< 0){
                throw new PartnerServiceException(ErrorCode.PARTNER_DOES_NOT_HAVE_MINIMUM_BALANCE_EXCEPTION.getErrorCode(),
                        ErrorCode.PARTNER_DOES_NOT_HAVE_MINIMUM_BALANCE_EXCEPTION.getErrorMessage());
            }
        }
        if (request.getServiceCode() == null || request.getServiceCode().isEmpty()) {
        	auditUtil.setAuditRequestDto(PaymentServiceAuditEnum.GENERATE_PRN_DEFAULT_SERVICE_CODE, "IDA", "serviceCode");
            request.setServiceCode("IDA");
        }
        request.setFullName(partnerData.getName());
        request.setNin(null);
        request.setService(PaymentConstants.SERVICE_NEWAID);
        try {
        	auditUtil.setAuditRequestDto(PaymentServiceAuditEnum.GENERATE_PRN_EXTERNAL_CALL, request.getPartnerId(), "partnerId");
            Map<String, Object> apiResponse = restUtil.postApi(
                    generatePrnUrl, null, "", "",
                    MediaType.APPLICATION_JSON, request, Map.class
            );
            if (apiResponse == null || apiResponse.isEmpty()) {
            	auditUtil.setAuditRequestDto(PaymentServiceAuditEnum.GENERATE_PRN_EMPTY_RESPONSE, request.getPartnerId(), "partnerId");
                throw new ApiAccessibleException("EXTERNAL_API_ERROR", "Provider returned an empty response");
            }
            PrnResponse prnResponse = mapper.convertValue(apiResponse, PrnResponse.class);
            String prn = prnResponse.getResponse().getData().getPrn();
            auditUtil.setAuditRequestDto(PaymentServiceAuditEnum.GENERATE_PRN_RESPONSE_MAPPED, prn , "prn");
            if (isPrnPresent(prnResponse)) {
                try {
                    PartnerPrn partnerPrn = mapPartnerPrnFromRequest(request, prnResponse);
                    partnerPrnRepository.save(partnerPrn);
                    auditUtil.setAuditRequestDto(PaymentServiceAuditEnum.GENERATE_PRN_DB_SAVE_SUCCESS, prn, "prn");
                } catch (Exception dbEx) {
                    LOGGER.error("PRN generated but failed to save to local DB: {}", dbEx.getMessage());
                    auditUtil.setAuditRequestDto(PaymentServiceAuditEnum.GENERATE_PRN_DB_SAVE_FAILURE, prn, "prn");
                    throw new ApiAccessibleException("DB_ERROR", "PRN generated but failed to persist");
                }
            } else {
            	auditUtil.setAuditRequestDto(PaymentServiceAuditEnum.GENERATE_PRN_MISSING, request.getPartnerId(), "partnerId");
            }
            auditUtil.setAuditRequestDto(PaymentServiceAuditEnum.GENERATE_PRN_SUCCESS, request.getPartnerId(), "partnerId");
            return prnResponse;
        } catch (IllegalArgumentException e) {
        	LOGGER.error("Mapping error for PRN response: {}", e.getMessage());
        	auditUtil.setAuditRequestDto(PaymentServiceAuditEnum.GENERATE_PRN_RESPONSE_PARSE_FAILURE, request.getPartnerId(), "partnerId");
            throw new ApiAccessibleException("PARSE_ERROR", "Failed to process partner response data");

        } catch (RestClientException e) {
            LOGGER.error("Network error calling PRN service: {}", e.getMessage());
            auditUtil.setAuditRequestDto(PaymentServiceAuditEnum.GENERATE_PRN_EXTERNAL_SERVICE_FAILURE, request.getPartnerId(), "partnerId");
            throw new ApiAccessibleException("SERVICE_UNAVAILABLE", "Payment gateway is currently unreachable");

        } catch (Exception e) {
            LOGGER.error("Unexpected error in generatePrn: ", e);
            auditUtil.setAuditRequestDto(PaymentServiceAuditEnum.GENERATE_PRN_INTERNAL_FAILURE, request.getPartnerId(), "partnerId");
            throw new ApiAccessibleException("INTERNAL_SERVER_ERROR", "An unexpected error occurred processing the PRN");
        }
    }

    @Override
    public ValidatePrnResponse validatePrn(ValidatePrnRequest request) {
        Partner partnerData = getValidPartner(request.getPartnerId(), false);
        ValidatePrnResponse validatePrnResponse = null;
        try {
        	auditUtil.setAuditRequestDto(PaymentServiceAuditEnum.VALIDATE_PRN_EXTERNAL_CALL, request.getPrn(), "prn");
            Map<String, Object> apiResponse = restUtil.postApi(
                    validatePrnUrl, null, "", "",
                    MediaType.APPLICATION_JSON, request, Map.class);
            
            if (apiResponse == null) {
            	auditUtil.setAuditRequestDto( PaymentServiceAuditEnum.VALIDATE_PRN_EMPTY_RESPONSE, request.getPrn(), "prn");
                throw new ApiAccessibleException("API_ERROR", "Provider returned no response");
            }
            validatePrnResponse = mapper.convertValue(apiResponse, ValidatePrnResponse.class);
            auditUtil.setAuditRequestDto( PaymentServiceAuditEnum.VALIDATE_PRN_RESPONSE_MAPPED, request.getPrn(), "prn");

        } catch (Exception e) {
            LOGGER.error("PRN Validation API failed for PRN {}: {}", request.getPrn(), e.getMessage());
            auditUtil.setAuditRequestDto(PaymentServiceAuditEnum.VALIDATE_PRN_EXTERNAL_SERVICE_FAILURE, request.getPrn(), "prn");
            throw new ApiAccessibleException("INTERNAL_SERVER_ERROR", "An unexpected error occurred processing the PRN");

        }
        if (validatePrnResponse == null || validatePrnResponse.getResponse() == null) {
        	auditUtil.setAuditRequestDto(PaymentServiceAuditEnum.VALIDATE_PRN_INVALID_RESPONSE, request.getPrn(), "prn");
            throw new ApiAccessibleException("INVALID_RESPONSE", "Invalid structure in validation response");
        }
        
        String statusCode = validatePrnResponse.getResponse().getStatusCode();
        processDatabaseUpdates(request, validatePrnResponse, statusCode);
        auditUtil.setAuditRequestDto(PaymentServiceAuditEnum.VALIDATE_PRN_SUCCESS, request.getPrn(), "prn");

        return validatePrnResponse;
    }

    private void processDatabaseUpdates(ValidatePrnRequest request, ValidatePrnResponse response, String statusCode) {
        PartnerPrn partnerPrn = getpartnerprndetails(request.getPrn(), request.getPartnerId());
        partnerPrn.setUpdBy(getLoggedInUserId());
        partnerPrn.setUpdDtimes(LocalDateTime.now());

        if (statusCode.equalsIgnoreCase(PaymentConstants.NOTPAID_STATUSCODE)) {
            partnerPrn.setStatus(PaymentConstants.VALIDATED_NOT_PAID);
            partnerPrn.setRemarks(PaymentConstants.AMOUNT_NOT_PAID);
            partnerPrnRepository.save(partnerPrn);

        } else if (statusCode.equalsIgnoreCase(PaymentConstants.PAID_STATUSCODE)) {
            partnerPrn.setStatus(PaymentConstants.VALIDATED_PAID);
            partnerPrn.setRemarks(PaymentConstants.AMOUNT_PAID);
            partnerPrnRepository.save(partnerPrn);

            if (!paymentRepository.isTransactionAlreadyExist(request.getPrn())) {
                PartnerPaymentTransactions transaction = mapTransactionFromResponse(request, response);
                paymentRepository.save(transaction);
                addBalance(request, response);
                LOGGER.info("Successfully processed payment and balance for PRN: {}", request.getPrn());
            }
        }
        else {
            LOGGER.warn("Unknown payment status {} for PRN {}", statusCode, request.getPrn());
        }

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
        try {
            balanceRepository.save(balanceDetails);
        } catch (Exception dbEx) {
            LOGGER.error("Balance failed to save to local DB: {}", dbEx.getMessage());
            auditUtil.setAuditRequestDto(PaymentServiceAuditEnum.BALANCE_DB_SAVE_FAILURE, request.getPartnerId(), "partner");
            throw new ApiAccessibleException("DB_ERROR", "Balance failed to persist");
        }
        notify(balanceDetails, response.getResponse().getAmountPaid());
    }

    private BigDecimal getBalance(String partnerId) {
        return balanceRepository.findById(partnerId)
                .map(balance -> Optional.ofNullable(balance.getBalance())
                        .orElse(BigDecimal.ZERO))
                .orElse(BigDecimal.ZERO);
    }

    private boolean isPrnPresent(PrnResponse response) {
        return response != null
                && response.getResponse() != null
                && response.getResponse().getData() != null
                && response.getResponse().getData().getPrn() != null;
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

    private PartnerPrn getpartnerprndetails(String prn, String partnerId) {
        PartnerPrnId id = new PartnerPrnId(partnerId, prn);
        return partnerPrnRepository.findById(id)
                .orElseThrow(() -> new ApiAccessibleException("PRN_NOT_FOUND", "PRN not found for partner"));
    }

    private String getLoggedInUserId() {
        return UserDetailUtil.getLoggedInUserId();
    }

    private Partner getValidPartner(String partnerId, boolean isToRetrieve) {
        Optional<Partner> partnerById = partnerRepository.findById(partnerId);
        if (partnerById.isEmpty()) {
            auditUtil.setAuditRequestDto(PaymentServiceAuditEnum.RETRIVE_PARTNER_FAILURE, partnerId, "partnerId");
            throw new PartnerServiceException(ErrorCode.PARTNER_DOES_NOT_EXIST_EXCEPTION.getErrorCode(),
                    ErrorCode.PARTNER_DOES_NOT_EXIST_EXCEPTION.getErrorMessage());
        }
        if (!isToRetrieve) {
            if (!partnerById.get().getIsActive()) {
                auditUtil.setAuditRequestDto(PaymentServiceAuditEnum.RETRIVE_PARTNER_ACTIVE_FAILURE, partnerId, "partnerId");
                throw new PartnerServiceException(ErrorCode.PARTNER_NOT_ACTIVE_EXCEPTION.getErrorCode(),
                        ErrorCode.PARTNER_NOT_ACTIVE_EXCEPTION.getErrorMessage());
            }
            if (!partnerById.get().getRequiresPayment()) {
                auditUtil.setAuditRequestDto(PaymentServiceAuditEnum.RETRIVE_PARTNER_REQUIRED_PAYMENT_FAILURE, partnerId, "partnerId");
                throw new PartnerServiceException(ErrorCode.PARTNER_NOT_REQUIRED_PAYMENT_EXCEPTION.getErrorCode(),
                        ErrorCode.PARTNER_NOT_REQUIRED_PAYMENT_EXCEPTION.getErrorMessage());
            }
        }
        return partnerById.get();
    }

    private void notify(PartnerBalance balanceDetails, BigDecimal addedAmount) {
        Type type = new Type();
        type.setName("PaymentServiceImpl");
        type.setNamespace("io.mosip.pms.payment.service.impl.PaymentServiceImpl");
        Map<String, Object> data = new HashMap<>();
        data.put(PartnerConstants.CREDITED_AMOUNT, addedAmount);
        data.put(PartnerConstants.UPDATED_BALANCE_DATA,balanceDetails);
        webSubPublisher.notify(EventType.PARTNERS_AMOUNT, data, type);
    }

    @Override
    public PageResponseDto<PartnerPaymentTransactions> searchPayment(SearchDto dto) {
        List<PartnerPaymentTransactions> partnerTypes = new ArrayList<>();
        PageResponseDto<PartnerPaymentTransactions> pageDto = new PageResponseDto<>();
        Page<PartnerPaymentTransactions> page = searchHelper.search(PartnerPaymentTransactions.class, dto, null);
        if (page.getContent() != null && !page.getContent().isEmpty()) {
            partnerTypes = MapperUtils.mapAll(page.getContent(), PartnerPaymentTransactions.class);
            pageDto = pageUtils.sortPage(partnerTypes, dto.getSort(), dto.getPagination(), page.getTotalElements());
        }
        auditUtil.setAuditRequestDto(PaymentServiceAuditEnum.SEARCH_PAYMENT_SUCCESS);
        return pageDto;
    }

}
