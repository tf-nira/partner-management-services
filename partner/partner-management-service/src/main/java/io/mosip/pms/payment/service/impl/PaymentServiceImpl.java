package io.mosip.pms.payment.service.impl;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.mosip.kernel.core.logger.spi.Logger;
import io.mosip.pms.common.constant.ApiAccessibleExceptionConstant;
import io.mosip.pms.common.entity.Partner;
import io.mosip.pms.common.entity.PartnerPaymentTransactions;
import io.mosip.pms.common.entity.PartnerPrn;
import io.mosip.pms.common.entity.PartnerPrnId;
import io.mosip.pms.common.exception.ApiAccessibleException;
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

import java.sql.Timestamp;
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
    AuditUtil auditUtil;

    @Value("${pmp.prn.generate.rest.uri}")
    private String generatePrnUrl;

    @Value("${pmp.prn.validate.rest.uri}")
    private String validatePrnUrl;


    public PrnResponse generatePrn(PrnRequest request) {

        Partner partnerData = getValidPartner(request.getPartnerId(),false);
        request.setFullName(partnerData.getName());
        request.setNin(null);
        request.setService(PaymentConstants.SERVICE_NEWAID);
        PrnResponse prnResponse = null;
        try {
            Map<String, Object> apiResponse = restUtil.postApi(generatePrnUrl, null,
                    "", "", MediaType.APPLICATION_JSON, request, Map.class);
            prnResponse = mapper.convertValue(apiResponse, PrnResponse.class);
        } catch (Exception e) {
            LOGGER.error("Error occured while parsing the response from template api", e.getLocalizedMessage());
        }
        if(prnResponse == null) {
            throw new ApiAccessibleException(ApiAccessibleExceptionConstant.TEMPLATE_NOT_FOUND.getErrorCode(),
                    ApiAccessibleExceptionConstant.TEMPLATE_NOT_FOUND.getErrorMessage());
        }
        if (prnResponse.getResponse().getData().getPrn() != null) {
            PartnerPrn partnerPrn = mapPartnerPrnFromRequest(request, prnResponse);
            partnerPrnRepository.save(partnerPrn);
        }
        return prnResponse;

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
            partnerPrn.setUpdDtimes(Timestamp.valueOf(LocalDateTime.now()).toLocalDateTime());
            partnerPrnRepository.save(partnerPrn);
        } else if (validatePrnResponse.getResponse().getStatusCode().equalsIgnoreCase(PaymentConstants.PAID_STATUSCODE)) {
            PartnerPrn partnerPrn = getpartnerprndetails(request.getPrn(), request.getPartnerId());
            partnerPrn.setStatus(PaymentConstants.VALIDATED_PAID);
            partnerPrn.setRemarks(PaymentConstants.AMOUNT_PAID);
            partnerPrn.setUpdBy((getLoggedInUserId()));
            partnerPrn.setUpdDtimes(Timestamp.valueOf(LocalDateTime.now()).toLocalDateTime());
            partnerPrnRepository.save(partnerPrn);
            Boolean isTransactionAlreadyExist = paymentRepository.isTransactionAlreadyExist(request.getPrn());
            if(!isTransactionAlreadyExist){
                PartnerPaymentTransactions transaction = mapTransactionFromResponse(request,validatePrnResponse);
                paymentRepository.save(transaction);
            }

        }
        return validatePrnResponse;
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
        partnerPrn.setCrDtimes(Timestamp.valueOf(LocalDateTime.now()).toLocalDateTime());
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
        transaction.setLogDtimes(Timestamp.valueOf(LocalDateTime.now()).toLocalDateTime());
        transaction.setCrBy((getLoggedInUserId()));
        transaction.setCrDtimes(Timestamp.valueOf(LocalDateTime.now()).toLocalDateTime());
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
