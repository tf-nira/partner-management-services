package io.mosip.pms.payment.service.impl;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.mosip.kernel.core.logger.spi.Logger;
import io.mosip.kernel.core.websub.model.EventModel;
import io.mosip.pms.common.constant.EventType;
import io.mosip.pms.common.dto.PageResponseDto;
import io.mosip.pms.common.dto.SearchDto;
import io.mosip.pms.common.dto.Type;
import io.mosip.pms.common.entity.*;
import io.mosip.pms.common.exception.ApiAccessibleException;
import io.mosip.pms.common.helper.SearchHelper;
import io.mosip.pms.common.helper.WebSubPublisher;
import io.mosip.pms.common.repository.*;
import io.mosip.pms.common.request.dto.ErrorResponse;
import io.mosip.pms.common.util.*;
import io.mosip.pms.device.util.AuditUtil;
import io.mosip.pms.partner.constant.ErrorCode;
import io.mosip.pms.partner.constant.PartnerConstants;
import io.mosip.pms.partner.constant.PartnerServiceAuditEnum;
import io.mosip.pms.partner.exception.PartnerServiceException;
import io.mosip.pms.payment.constant.PaymentConstants;
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

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.*;
/**
 *
 * @author Karthik S, Jagadeesh
 *
 */
@Service
@Transactional
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private static final Logger LOGGER = PMSLogger.getLogger(PaymentServiceImpl.class);
    private final RestUtil restUtil;
    private final ObjectMapper mapper;
    private final PartnerPrnRepository partnerPrnRepository;
    private final PartnersTransactionRepository partnersTransactionRepository;
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
    private double minimumBalanceRequired;

    @PostConstruct
    public void init() {
        minimumBalanceRequired = roundToTwo(minimumBalanceRequired);
    }

    @Override
    public PrnResponse generatePrn(PrnRequest request) {
        Partner partnerData = getValidPartner(request.getPartnerId(), false);
//        double amount = roundToTwo(request.getAmount());
//        if(amount < minimumBalanceRequired){
//            double currentBalance = roundToTwo(getBalance(request.getPartnerId()));
//            double accumulatedBalance = roundToTwo(currentBalance + amount);
//            if(accumulatedBalance<minimumBalanceRequired){
//                throw new PartnerServiceException(ErrorCode.PARTNER_DOES_NOT_HAVE_MINIMUM_BALANCE_EXCEPTION.getErrorCode(),
//                        ErrorCode.PARTNER_DOES_NOT_HAVE_MINIMUM_BALANCE_EXCEPTION.getErrorMessage());
//            }
//        }
//        if (request.getServiceCode() == null || request.getServiceCode().isEmpty()) {
//        	auditUtil.setAuditRequestDto(PartnerServiceAuditEnum.GENERATE_PRN_DEFAULT_SERVICE_CODE, "IDA", "serviceCode");
//            request.setServiceCode("IDA");
//        }
        request.setPartnerName(partnerData.getName());
        try {
            auditUtil.setAuditRequestDto(PartnerServiceAuditEnum.GENERATE_PRN_EXTERNAL_CALL, request.getPartnerGroup(), "partnerGroup");
            auditUtil.setAuditRequestDto(PartnerServiceAuditEnum.GENERATE_PRN_EXTERNAL_CALL, request.getPartnerType(), "partnerType");
            auditUtil.setAuditRequestDto(PartnerServiceAuditEnum.GENERATE_PRN_EXTERNAL_CALL, request.getPartnerName(), "partnerName");
            auditUtil.setAuditRequestDto(PartnerServiceAuditEnum.GENERATE_PRN_EXTERNAL_CALL, request.getPartnerId(), "partnerId");
            auditUtil.setAuditRequestDto(PartnerServiceAuditEnum.GENERATE_PRN_EXTERNAL_CALL, String.valueOf(request.getNumberOfRecords()), "numberOfRecords");
            Map<String, Object> apiResponse = restUtil.postApi(
                    generatePrnUrl, null, "", "",
                    MediaType.APPLICATION_JSON, request, Map.class
            );
            if (apiResponse == null || apiResponse.isEmpty()) {
            	auditUtil.setAuditRequestDto(PartnerServiceAuditEnum.GENERATE_PRN_EMPTY_RESPONSE, request.getPartnerId(), "partnerId");
                throw new ApiAccessibleException("EXTERNAL_API_ERROR", "Provider returned an empty response");
            }
            PrnResponse prnResponse = mapper.convertValue(apiResponse, PrnResponse.class);
            String prn = prnResponse.getResponse().getPrn();
            auditUtil.setAuditRequestDto(PartnerServiceAuditEnum.GENERATE_PRN_RESPONSE_MAPPED, prn , "prn");
            if (!prn.isEmpty()) {
                try {
                    PartnerPrn partnerPrn = mapPartnerPrnFromRequest(request, prnResponse);
                    partnerPrnRepository.save(partnerPrn);
                    auditUtil.setAuditRequestDto(PartnerServiceAuditEnum.GENERATE_PRN_DB_SAVE_SUCCESS, prn, "prn");
                } catch (Exception dbEx) {
                    LOGGER.error("PRN generated but failed to save to local DB: {}", dbEx.getMessage());
                    auditUtil.setAuditRequestDto(PartnerServiceAuditEnum.GENERATE_PRN_DB_SAVE_FAILURE, prn, "prn");
                    throw new ApiAccessibleException("DB_ERROR", "PRN generated but failed to persist");
                }
            } else {
            	auditUtil.setAuditRequestDto(PartnerServiceAuditEnum.GENERATE_PRN_MISSING, request.getPartnerId(), "partnerId");
            }
            auditUtil.setAuditRequestDto(PartnerServiceAuditEnum.GENERATE_PRN_SUCCESS, request.getPartnerId(), "partnerId");
            return prnResponse;
        } catch (IllegalArgumentException e) {
        	LOGGER.error("Mapping error for PRN response: {}", e.getMessage());
        	auditUtil.setAuditRequestDto(PartnerServiceAuditEnum.GENERATE_PRN_RESPONSE_PARSE_FAILURE, request.getPartnerId(), "partnerId");
            throw new ApiAccessibleException("PARSE_ERROR", "Failed to process partner response data");

        } catch (RestClientException e) {
            LOGGER.error("Network error calling PRN service: {}", e.getMessage());
            auditUtil.setAuditRequestDto(PartnerServiceAuditEnum.GENERATE_PRN_EXTERNAL_SERVICE_FAILURE, request.getPartnerId(), "partnerId");
            throw new ApiAccessibleException("SERVICE_UNAVAILABLE", "Payment gateway is currently unreachable");

        } catch (Exception e) {
            LOGGER.error("Unexpected error in generatePrn: ", e);
            auditUtil.setAuditRequestDto(PartnerServiceAuditEnum.GENERATE_PRN_INTERNAL_FAILURE, request.getPartnerId(), "partnerId");
            throw new ApiAccessibleException("INTERNAL_SERVER_ERROR", "An unexpected error occurred processing the PRN");
        }
    }

    @Override
    public ValidatePrnResponse validatePrn(ValidatePrnRequest request) {
        Partner partnerData = getValidPartner(request.getPartnerId(), false);
        ValidatePrnResponse validatePrnResponse = null;
        try {
        	auditUtil.setAuditRequestDto(PartnerServiceAuditEnum.VALIDATE_PRN_EXTERNAL_CALL, request.getPrn(), "prn");
            Map<String, Object> apiResponse = restUtil.postApi(
                    validatePrnUrl, null, "", "",
                    MediaType.APPLICATION_JSON, request, Map.class);
            
            if (apiResponse == null) {
            	auditUtil.setAuditRequestDto( PartnerServiceAuditEnum.VALIDATE_PRN_EMPTY_RESPONSE, request.getPrn(), "prn");
                throw new ApiAccessibleException("API_ERROR", "Provider returned no response");
            }
            validatePrnResponse = mapper.convertValue(apiResponse, ValidatePrnResponse.class);
            auditUtil.setAuditRequestDto( PartnerServiceAuditEnum.VALIDATE_PRN_RESPONSE_MAPPED, request.getPrn(), "prn");

        } catch (Exception e) {
            LOGGER.error("PRN Validation API failed for PRN {}: {}", request.getPrn(), e.getMessage());
            auditUtil.setAuditRequestDto(PartnerServiceAuditEnum.VALIDATE_PRN_EXTERNAL_SERVICE_FAILURE, request.getPrn(), "prn");
            throw new ApiAccessibleException("INTERNAL_SERVER_ERROR", "An unexpected error occurred processing the PRN");

        }
        if (validatePrnResponse == null || validatePrnResponse.getResponse() == null) {
            auditUtil.setAuditRequestDto(PartnerServiceAuditEnum.VALIDATE_PRN_INVALID_RESPONSE, request.getPrn(), "prn");
            if (validatePrnResponse.getErrors() != null &&
                    !validatePrnResponse.getErrors().isEmpty()) {
                ErrorResponse error = validatePrnResponse.getErrors().get(0);
                throw new ApiAccessibleException(
                        error.getErrorCode(),
                        error.getMessage()
                );
            }
            else {
                throw new ApiAccessibleException("INVALID_RESPONSE", "Invalid structure in validation response");
            }
        }
        
        String statusCode = validatePrnResponse.getResponse().getStatusCode();
        Boolean isValidPmsTaxHead = validatePrnResponse.getResponse().isValidPmsTaxHead();
        processDatabaseUpdates(request, validatePrnResponse, statusCode, isValidPmsTaxHead);
        auditUtil.setAuditRequestDto(PartnerServiceAuditEnum.VALIDATE_PRN_SUCCESS, request.getPrn(), "prn");

        return validatePrnResponse;
    }

    private void processDatabaseUpdates(ValidatePrnRequest request, ValidatePrnResponse response, String statusCode, Boolean isValidPmsTaxHead) {
        PartnerPrn partnerPrn = getpartnerprndetails(request.getPrn(), request.getPartnerId());
        partnerPrn.setUpdBy(getLoggedInUserId());
        partnerPrn.setUpdDtimes(LocalDateTime.now());

        if(!isValidPmsTaxHead){
            throw new ApiAccessibleException("INVALID  TAXHEAD", "Invalid pms taxHead");

        } else if (statusCode.equalsIgnoreCase(PaymentConstants.NOTPAID_STATUSCODE)) {
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
                saveIntoPartnersTransaction(transaction);
                addBalance(request, response);
                LOGGER.info("Successfully processed payment and balance for PRN: {}", request.getPrn());
            }
        }
        else {
            LOGGER.warn("Unknown payment status {} for PRN {}", statusCode, request.getPrn());
        }

    }

    private void addBalance(ValidatePrnRequest request, ValidatePrnResponse response) {
        double creditedAmount = roundToTwo(
                Optional.ofNullable(response.getResponse().getAmountPaid())
                        .orElse(0.0)
        );
        PartnerBalance balanceDetails = balanceRepository.findById(request.getPartnerId())
                .map(existingBalance -> {

                    double current = existingBalance.getBalance(); // primitive double → no null
                    double updatedBalance = roundToTwo(current + creditedAmount);

                    existingBalance.setBalance(updatedBalance);
                    existingBalance.setUpdBy(getLoggedInUserId());
                    existingBalance.setUpdDtimes(LocalDateTime.now());

                    return existingBalance;
                })
                .orElseGet(() -> mapBalanceDetails(request, response));
        try {
            balanceRepository.save(balanceDetails);
        } catch (Exception dbEx) {
            LOGGER.error("Balance failed to save to local DB: {}", dbEx.getMessage());
            auditUtil.setAuditRequestDto(PartnerServiceAuditEnum.BALANCE_DB_SAVE_FAILURE, request.getPartnerId(), "partner");
            throw new ApiAccessibleException("DB_ERROR", "Balance failed to persist");
        }
        notify(balanceDetails, roundToTwo(response.getResponse().getAmountPaid()),response.getResponse().getPrn());
    }

    public double getBalance(String partnerId) {
        return balanceRepository.findById(partnerId)
                .map(balance -> balance.getBalance())
                .orElse(0.0);
    }


//    private boolean isPrnPresent(PrnResponse response) {
//        return response != null
//                && response.getResponse() != null
//                && response.getResponse().getData() != null
//                && response.getResponse().getData().getPrn() != null;
//    }

    private PartnerBalance mapBalanceDetails(ValidatePrnRequest request, ValidatePrnResponse response){
        PartnerBalance balanceDetails = new PartnerBalance();
        balanceDetails.setPartnerId(request.getPartnerId());
        balanceDetails.setBalance(roundToTwo(response.getResponse().getAmountPaid()));
        balanceDetails.setCrBy((getLoggedInUserId()));
        balanceDetails.setCrDtimes(LocalDateTime.now());
        return balanceDetails;
    }

    private PartnerPrn mapPartnerPrnFromRequest(PrnRequest request, PrnResponse response){
        PartnerPrn partnerPrn = new PartnerPrn();
        partnerPrn.setPartnerId(request.getPartnerId());
        partnerPrn.setPartnerName(request.getPartnerName());
        partnerPrn.setPrn(response.getResponse().getPrn());
        partnerPrn.setStatus(PaymentConstants.GENERATED);
        partnerPrn.setAmount(roundToTwo(response.getResponse().getAmount()));
        partnerPrn.setServiceCode("PMS");
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
        transaction.setAmount(roundToTwo(response.getResponse().getAmountPaid()));
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
            auditUtil.setAuditRequestDto(PartnerServiceAuditEnum.RETRIVE_PARTNER_FAILURE, partnerId, "partnerId");
            throw new PartnerServiceException(ErrorCode.PARTNER_DOES_NOT_EXIST_EXCEPTION.getErrorCode(),
                    ErrorCode.PARTNER_DOES_NOT_EXIST_EXCEPTION.getErrorMessage());
        }
        if (!isToRetrieve) {
            if (!partnerById.get().getIsActive()) {
                auditUtil.setAuditRequestDto(PartnerServiceAuditEnum.RETRIVE_PARTNER_ACTIVE_FAILURE, partnerId, "partnerId");
                throw new PartnerServiceException(ErrorCode.PARTNER_NOT_ACTIVE_EXCEPTION.getErrorCode(),
                        ErrorCode.PARTNER_NOT_ACTIVE_EXCEPTION.getErrorMessage());
            }
            if (!partnerById.get().getRequiresPayment()) {
                auditUtil.setAuditRequestDto(PartnerServiceAuditEnum.RETRIVE_PARTNER_REQUIRED_PAYMENT_FAILURE, partnerId, "partnerId");
                throw new PartnerServiceException(ErrorCode.PARTNER_NOT_REQUIRED_PAYMENT_EXCEPTION.getErrorCode(),
                        ErrorCode.PARTNER_NOT_REQUIRED_PAYMENT_EXCEPTION.getErrorMessage());
            }
        }
        return partnerById.get();
    }

    private void notify(PartnerBalance balanceDetails, double addedAmount, String prn) {
        Type type = new Type();
        type.setName("PaymentServiceImpl");
        type.setNamespace("io.mosip.pms.payment.service.impl.PaymentServiceImpl");
        Map<String, Object> data = new HashMap<>();
        data.put(PartnerConstants.CREDITED_AMOUNT, addedAmount);
        data.put(PartnerConstants.UPDATED_BALANCE_DATA,balanceDetails);
        data.put(PartnerConstants.PRN_DATA,prn);
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
        auditUtil.setAuditRequestDto(PartnerServiceAuditEnum.SEARCH_PAYMENT_SUCCESS);
        return pageDto;
    }

    private double roundToTwo(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
    
    @Override
    public PageResponseDto<PartnerPrn> searchPartnerPrn(SearchDto dto) {
        List<PartnerPrn> partnerPrns = new ArrayList<>();
        PageResponseDto<PartnerPrn> pageDto = new PageResponseDto<>();
        Page<PartnerPrn> page = searchHelper.search(PartnerPrn.class, dto, null);
        if (page.getContent() != null && !page.getContent().isEmpty()) {
            partnerPrns = MapperUtils.mapAll(page.getContent(), PartnerPrn.class);
            pageDto = pageUtils.sortPage(partnerPrns, dto.getSort(), dto.getPagination(), page.getTotalElements());
        }
        auditUtil.setAuditRequestDto(PartnerServiceAuditEnum.SEARCH_PARTNER_PRN_SUCCESS);
        return pageDto;
    }

    @Override
    public PageResponseDto<PartnerBalance> searchPartnerBalance(SearchDto dto) {
        List<PartnerBalance> partnerBalances = new ArrayList<>();
        PageResponseDto<PartnerBalance> pageDto = new PageResponseDto<>();
        Page<PartnerBalance> page = searchHelper.search(PartnerBalance.class, dto, null);
        if (page.getContent() != null && !page.getContent().isEmpty()) {
            partnerBalances = MapperUtils.mapAll(page.getContent(), PartnerBalance.class);
            pageDto = pageUtils.sortPage(partnerBalances, dto.getSort(), dto.getPagination(), page.getTotalElements());
        }
        auditUtil.setAuditRequestDto(PartnerServiceAuditEnum.SEARCH_PARTNER_BALANCE_SUCCESS);
        return pageDto;
    }

    @Override
    public PageResponseDto<PartnersTransaction> searchPartnerTransaction(SearchDto dto) {
        List<PartnersTransaction> partnersTransactionst = new ArrayList<>();
        PageResponseDto<PartnersTransaction> pageDto = new PageResponseDto<>();
        Page<PartnersTransaction> page = searchHelper.search(PartnersTransaction.class, dto, null);
        if (page.getContent() != null && !page.getContent().isEmpty()) {
            partnersTransactionst = MapperUtils.mapAll(page.getContent(), PartnersTransaction.class);
            pageDto = pageUtils.sortPage(partnersTransactionst, dto.getSort(), dto.getPagination(), page.getTotalElements());
        }
        auditUtil.setAuditRequestDto(PartnerServiceAuditEnum.SEARCH_PARTNER_BALANCE_SUCCESS);
        return pageDto;
    }

    public void prnStatusUpdateIda(EventModel eventModel) {
        LOGGER.info("Enterring into  prnStatusUpdateFromIda..........");
        Map<String, Object> eventData = eventModel.getEvent().getData();
        String prn = (String) eventData.get(PaymentConstants.PRN);
        String partnerId = (String)eventData.get(PaymentConstants.PARTNER_ID);
        Boolean isCreditted = (Boolean) eventData.get(PaymentConstants.AMOUNT_CREDITTED);
        if(isCreditted){
            LOGGER.info("Entering isCreditted True.........");
            PartnerPrn partnerPrn = getpartnerprndetails(prn,partnerId);
            partnerPrn.setUpdBy(getLoggedInUserId());
            partnerPrn.setUpdDtimes(LocalDateTime.now());
            partnerPrn.setStatus(PaymentConstants.SETTLED);
            try {
                LOGGER.info("saving prn status update........");
                partnerPrnRepository.save(partnerPrn);
                auditUtil.setAuditRequestDto(PartnerServiceAuditEnum.SETTLE_PRN_DB_SAVE_SUCCESS, prn, "prn");
            } catch (Exception dbEx) {
                LOGGER.info("paymentsettled catch block........");
                LOGGER.error("PRN Settled but failed to save  DB: {}", dbEx.getMessage());
                auditUtil.setAuditRequestDto(PartnerServiceAuditEnum.SETTLE_PRN_DB_SAVE_FAILURE, prn, "prn");
                throw new ApiAccessibleException("DB_ERROR", "PRN settled but failed to persist");
            }

        }

    }

    public void insertPartnersAuthTransaction(EventModel eventModel) {
        LOGGER.info("Enterring into  insertPartnersAuthTransactions..........");
        Map<String, Object> eventData = eventModel.getEvent().getData();
        if (eventData.get(PaymentConstants.CHARGE_AMOUNT) != null) {
            PartnersTransaction partnersTransaction = new PartnersTransaction();

            String requestedEntityId = (String) eventData.get(PaymentConstants.REQUESTED_ENTITY_ID);
            partnersTransaction.setRequestedEntityId(requestedEntityId);

            double amount = (double) eventData.get(PaymentConstants.CHARGE_AMOUNT);
            partnersTransaction.setAmount(amount);

            String requestedEntityName = (String) eventData.get(PaymentConstants.REQUESTED_ENTITY_NAME);
            partnersTransaction.setRequestedEntityName(requestedEntityName);

            partnersTransaction.setEntryType(PaymentConstants.DEBIT);

            String id = (String) eventData.get(PaymentConstants.ID);
            partnersTransaction.setId(id);

            LocalDateTime requestDtimes = convertToLocalDateTime(eventData.get(PaymentConstants.REQUEST_DTIMES));
            partnersTransaction.setRequestDtimes(requestDtimes);

            LocalDateTime responseDtimes = convertToLocalDateTime(eventData.get(PaymentConstants.RESPONSE_DTIMES));
            partnersTransaction.setResponseDtimes(responseDtimes);

            String requestTrnId = (String) eventData.get(PaymentConstants.REQUEST_TRN_ID);
            partnersTransaction.setRequestTrnId(requestTrnId);

            String authTypeCode = (String) eventData.get(PaymentConstants.AUTH_TYPE_CODE);
            partnersTransaction.setAuthTypeCode(authTypeCode);

            String statusCode = (String) eventData.get(PaymentConstants.STATUS_CODE);
            partnersTransaction.setStatusCode(statusCode);

            String statusComment = (String) eventData.get(PaymentConstants.STATUS_COMMENT);
            partnersTransaction.setStatusComment(statusComment);

            partnersTransaction.setCrBy(PaymentConstants.IDA);
            partnersTransaction.setCrDtimes(LocalDateTime.now());

            try {
                LOGGER.info("saving auth transaction........");
                partnersTransactionRepository.save(partnersTransaction);
                auditUtil.setAuditRequestDto(PartnerServiceAuditEnum.AUTH_TRANSACTION_DB_SAVE_SUCCESS, requestedEntityName, "partner");
            } catch (Exception dbEx) {
                LOGGER.error("auth transaction failed to save in DB: {}", dbEx.getMessage());
                auditUtil.setAuditRequestDto(PartnerServiceAuditEnum.AUTH_TRANSACTION_DB_SAVE_FAILURE, requestedEntityName, "partner");
                throw new ApiAccessibleException("DB_ERROR", "auth transaction failed to persist");
            }
        }

    }

    public void saveIntoPartnersTransaction(PartnerPaymentTransactions transaction) {
        LOGGER.info("Enterring into  saveIntoPartnersAuthTransaction..........");
            PartnersTransaction partnersTransaction = new PartnersTransaction();
            Partner partnerData = getValidPartner(transaction.getPartnerId(), false);
            partnersTransaction.setRequestedEntityId(transaction.getPartnerId());
            partnersTransaction.setAmount(transaction.getAmount());
            partnersTransaction.setRequestedEntityName(partnerData.getName());
            partnersTransaction.setEntryType(PaymentConstants.CREDIT);
            partnersTransaction.setId(UUID.randomUUID().toString());
            partnersTransaction.setRequestDtimes(LocalDateTime.now());
            partnersTransaction.setResponseDtimes(LocalDateTime.now());
            partnersTransaction.setRequestTrnId(transaction.getTransactionId());
            partnersTransaction.setAuthTypeCode(null);
            partnersTransaction.setStatusCode(null);
            partnersTransaction.setStatusComment(PaymentConstants.AMOUNT_CREDITED);
            partnersTransaction.setCrBy(PaymentConstants.PMS);
            partnersTransaction.setCrDtimes(LocalDateTime.now());

            try {
                LOGGER.info("saving auth transaction........");
                partnersTransactionRepository.save(partnersTransaction);
                auditUtil.setAuditRequestDto(PartnerServiceAuditEnum.AUTH_TRANSACTION_DB_SAVE_SUCCESS, transaction.getPartnerId(), "partner");
            } catch (Exception dbEx) {
                LOGGER.error("auth transaction failed to save in DB: {}", dbEx.getMessage());
                auditUtil.setAuditRequestDto(PartnerServiceAuditEnum.AUTH_TRANSACTION_DB_SAVE_FAILURE, transaction.getPartnerId(), "partner");
                throw new ApiAccessibleException("DB_ERROR", "auth transaction failed to persist");
            }

    }

    public void partnersBalanceUpdateIda(EventModel eventModel) {
        LOGGER.info("Enterring into  partnersBalanceUpdateIda..........");
        Map<String, Object> eventData = eventModel.getEvent().getData();
        Double balance = (Double) eventData.get(PaymentConstants.BALANCE);
        String partnerId = (String)eventData.get(PaymentConstants.PARTNER_ID);
        if(balance!=null &&  !partnerId.isEmpty()){
            LOGGER.info("Entering isCreditted True.........");
            PartnerBalance balanceDetails = balanceRepository
                    .findById(partnerId)
                    .orElseThrow(() -> new ApiAccessibleException("PARTNER_NOT_FOUND","no partner match found"));
            balanceDetails.setUpdBy(getLoggedInUserId());
            balanceDetails.setUpdDtimes(LocalDateTime.now());
            balanceDetails.setBalance(roundToTwo(balance));
            try {
                LOGGER.info("saving prn status update........");
                balanceRepository.save(balanceDetails);
                auditUtil.setAuditRequestDto(PartnerServiceAuditEnum.BALANCE_UPDATE_DB_SAVE_SUCCESS, partnerId, "prn");
            } catch (Exception dbEx) {
                LOGGER.info("paymentsettled catch block........");
                LOGGER.error("PRN Settled but failed to save  DB: {}", dbEx.getMessage());
                auditUtil.setAuditRequestDto(PartnerServiceAuditEnum.BALANCE_UPDATE_DB_SAVE_FAILURE, partnerId, "prn");
                throw new ApiAccessibleException("DB_ERROR", "PRN settled but failed to persist");
            }

        }

    }

    private LocalDateTime convertToLocalDateTime(Object value) {
        if (value == null) {
            return null;
        }
        try {
            return mapper.convertValue(
                    value,
                    LocalDateTime.class
            );
        } catch (IllegalArgumentException e) {
            throw new ApiAccessibleException(
                    "INVALID_TIMESTAMP",
                    "Invalid datetime format: " + value
            );
        }
    }
}
