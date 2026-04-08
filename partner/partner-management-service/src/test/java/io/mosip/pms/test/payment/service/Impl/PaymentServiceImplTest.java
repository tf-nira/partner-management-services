package io.mosip.pms.test.payment.service.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.mosip.kernel.core.websub.model.Event;
import io.mosip.kernel.core.websub.model.EventModel;
import io.mosip.pms.common.entity.*;
import io.mosip.pms.common.exception.ApiAccessibleException;
import io.mosip.pms.common.helper.WebSubPublisher;
import io.mosip.pms.common.repository.*;
import io.mosip.pms.common.util.RestUtil;
import io.mosip.pms.device.util.AuditUtil;
import io.mosip.pms.partner.constant.PartnerServiceAuditEnum;
import io.mosip.pms.partner.exception.PartnerServiceException;
import io.mosip.pms.payment.constant.PaymentConstants;
import io.mosip.pms.payment.request.dto.PrnRequest;
import io.mosip.pms.payment.request.dto.ValidatePrnRequest;
import io.mosip.pms.payment.response.dto.PrnData;
import io.mosip.pms.payment.response.dto.PrnInnerResponse;
import io.mosip.pms.payment.response.dto.PrnResponse;
import io.mosip.pms.payment.response.dto.ValidatePrnInnerResponse;
import io.mosip.pms.payment.response.dto.ValidatePrnResponse;
import io.mosip.pms.payment.service.impl.PaymentServiceImpl;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.*;
/**
 *
 * @author Jagadeesh
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class PaymentServiceImplTest {

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Mock
    private WebSubPublisher webSubPublisher;

    @Mock
    private RestUtil restUtil;

    @Mock
    private ObjectMapper mapper;

    @Mock
    private PartnerPrnRepository partnerPrnRepository;

    @Mock
    private PartnerServiceRepository partnerRepository;

    @Mock
    private PartnerPaymentTransactionsRepository paymentRepository;

    @Mock
    private PartnerBalanceRepository balanceRepository;

    @Mock
    private AuthTransactionRepository authTransactionRepository;

    @Mock
    private AuditUtil auditUtil;

    private Partner mockActivePartner() {
        Partner partner = new Partner();
        partner.setId("mosip");
        partner.setIsActive(true);
        partner.setRequiresPayment(true);
        partner.setName("MOSIP Partner");
        return partner;
    }

    @Test
    public void testGeneratePrnSuccess() {

        ReflectionTestUtils.setField(paymentService, "generatePrnUrl", "https://api-internal.niradev1.idencode.link/v1/payment/generatePrnPms");
        ReflectionTestUtils.setField(paymentService, "minimumBalanceRequired", 50.0);

        PrnRequest request = new PrnRequest();
        request.setPartnerId("mosip");

        Mockito.when(partnerRepository.findById("mosip"))
                .thenReturn(Optional.of(mockActivePartner()));

        Map<String, Object> apiMap = new HashMap<>();
        apiMap.put("response", new HashMap<>());

        Mockito.when(restUtil.postApi(Mockito.anyString(), Mockito.any(), Mockito.any(),
                Mockito.any(), Mockito.any(), Mockito.any(), Mockito.eq(Map.class))).thenReturn(apiMap);

        PrnInnerResponse inner = new PrnInnerResponse();
        inner.setPrn("PRN123");
        inner.setAmount(100.0);

        PrnResponse response = new PrnResponse();
        response.setResponse(inner);
        Mockito.when(mapper.convertValue(apiMap, PrnResponse.class))
                .thenReturn(response);
        paymentService.generatePrn(request);
        Mockito.verify(partnerPrnRepository).save(Mockito.any());
    }

    @Test(expected = ApiAccessibleException.class)
    public void testGeneratePrnEmptyApiResponse() {

        PrnRequest request = new PrnRequest();
        request.setPartnerId("mosip");

        ReflectionTestUtils.setField(paymentService,
                "minimumBalanceRequired",
                Double.valueOf(100));
        Mockito.when(partnerRepository.findById("mosip"))
                .thenReturn(Optional.of(mockActivePartner()));
        Mockito.when(restUtil.postApi(Mockito.any(), Mockito.any(), Mockito.any(),
                Mockito.any(), Mockito.any(), Mockito.any(),Mockito.eq(Map.class)))
                .thenReturn(null);
        paymentService.generatePrn(request);
    }


    @Test(expected = PartnerServiceException.class)
    public void testGeneratePrnPartnerNotFound() {
        Mockito.when(partnerRepository.findById("mosip"))
                .thenReturn(Optional.empty());
        PrnRequest request = new PrnRequest();
        request.setPartnerId("mosip");

        paymentService.generatePrn(request);
    }

    @Test
    public void testValidatePrnNotPaid() {

        ValidatePrnRequest request = new ValidatePrnRequest();
        request.setPartnerId("mosip");
        request.setPrn("PRN123");

        Mockito.when(partnerRepository.findById("mosip"))
                .thenReturn(Optional.of(mockActivePartner()));

        ValidatePrnInnerResponse inner = new ValidatePrnInnerResponse();
        inner.setStatusCode(PaymentConstants.NOTPAID_STATUSCODE);
        inner.setPrn("PRN123");
        inner.setValidPmsTaxHead(true);

        ValidatePrnResponse response = new ValidatePrnResponse();
        response.setResponse(inner);

        Mockito.when(restUtil.postApi(
                        Mockito.any(),
                        Mockito.any(),
                        Mockito.any(),
                        Mockito.any(),
                        Mockito.any(),
                        Mockito.any(),
                        Mockito.eq(Map.class)))
                .thenReturn(new HashMap<>());

        Mockito.when(mapper.convertValue(Mockito.any(), Mockito.eq(ValidatePrnResponse.class)))
                .thenReturn(response);

        Mockito.when(partnerPrnRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(new PartnerPrn()));

        paymentService.validatePrn(request);

        Mockito.verify(partnerPrnRepository).save(Mockito.any());
    }

    @Test
    public void testValidatePrnPaidNewTransaction() {

        ValidatePrnRequest request = new ValidatePrnRequest();
        request.setPartnerId("mosip");
        request.setPrn("PRN123");

        Mockito.when(partnerRepository.findById("mosip"))
                .thenReturn(Optional.of(mockActivePartner()));

        ValidatePrnInnerResponse inner = new ValidatePrnInnerResponse();
        inner.setStatusCode(PaymentConstants.PAID_STATUSCODE);
        inner.setAmountPaid(100.0);
        inner.setPrn("PRN123");
        inner.setValidPmsTaxHead(true);

        ValidatePrnResponse response = new ValidatePrnResponse();
        response.setResponse(inner);

        Mockito.when(restUtil.postApi(
                        Mockito.any(),
                        Mockito.any(),
                        Mockito.any(),
                        Mockito.any(),
                        Mockito.any(),
                        Mockito.any(),
                        Mockito.eq(Map.class)))
                .thenReturn(new HashMap<>());

        Mockito.when(mapper.convertValue(Mockito.any(), Mockito.eq(ValidatePrnResponse.class)))
                .thenReturn(response);

        Mockito.when(partnerPrnRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(new PartnerPrn()));

        Mockito.when(paymentRepository.isTransactionAlreadyExist("PRN123"))
                .thenReturn(false);

        Mockito.when(balanceRepository.findById("mosip"))
                .thenReturn(Optional.empty());

        Mockito.doNothing().when(webSubPublisher)
                .notify(Mockito.any(), Mockito.any(), Mockito.any());

        paymentService.validatePrn(request);

        Mockito.verify(paymentRepository).save(Mockito.any());
        Mockito.verify(balanceRepository).save(Mockito.any());
    }


    @Test(expected = ApiAccessibleException.class)
    public void testValidatePrnApiReturnsNull() {

        ValidatePrnRequest request = new ValidatePrnRequest();
        request.setPartnerId("mosip");

        Mockito.when(partnerRepository.findById("mosip"))
                .thenReturn(Optional.of(mockActivePartner()));
        Mockito.when(restUtil.postApi(Mockito.any(), Mockito.any(), Mockito.any(),
                        Mockito.any(), Mockito.any(), Mockito.any(), Mockito.eq(Map.class)))
                .thenReturn(null);

        paymentService.validatePrn(request);
    }

    @Ignore
    @Test
    public void testGeneratePrnMinimumBalanceFailure() {

        ReflectionTestUtils.setField(paymentService, "minimumBalanceRequired", 100.0);

        PrnRequest request = new PrnRequest();
        request.setPartnerId("mosip");

        Mockito.when(partnerRepository.findById("mosip"))
                .thenReturn(Optional.of(mockActivePartner()));

        PartnerBalance balance = new PartnerBalance();
        balance.setBalance(10.0);

        Mockito.when(balanceRepository.findById("mosip"))
                .thenReturn(Optional.of(balance));

        try {
            paymentService.generatePrn(request);
        } catch (Exception e) {
            assert e instanceof PartnerServiceException;
        }
    }

    @Test(expected = ApiAccessibleException.class)
    public void testValidatePrnPrnNotFound() {

        ValidatePrnRequest request = new ValidatePrnRequest();
        request.setPartnerId("mosip");
        request.setPrn("PRN123");

        Mockito.when(partnerRepository.findById("mosip"))
                .thenReturn(Optional.of(mockActivePartner()));

        ValidatePrnInnerResponse inner = new ValidatePrnInnerResponse();
        inner.setStatusCode(PaymentConstants.NOTPAID_STATUSCODE);

        ValidatePrnResponse response = new ValidatePrnResponse();
        response.setResponse(inner);

        Mockito.when(restUtil.postApi(Mockito.any(), Mockito.any(), Mockito.any(),
                        Mockito.any(), Mockito.any(), Mockito.any(), Mockito.eq(Map.class)))
                .thenReturn(new HashMap<>());
        Mockito.when(mapper.convertValue(Mockito.any(), Mockito.eq(ValidatePrnResponse.class)))
                .thenReturn(response);
        Mockito.when(partnerPrnRepository.findById(Mockito.any()))
                .thenReturn(Optional.empty());

        paymentService.validatePrn(request);
    }

    @Test(expected = ApiAccessibleException.class)
    public void testValidatePrnInvalidResponse() {

        ValidatePrnRequest request = new ValidatePrnRequest();
        request.setPartnerId("mosip");
        request.setPrn("PRN123");

        Mockito.when(partnerRepository.findById("mosip"))
                .thenReturn(Optional.of(mockActivePartner()));
        Mockito.when(restUtil.postApi(Mockito.any(), Mockito.any(), Mockito.any(),
                        Mockito.any(), Mockito.any(), Mockito.any(), Mockito.eq(Map.class)))
                .thenReturn(new HashMap<>());

        ValidatePrnResponse response = new ValidatePrnResponse();
        response.setResponse(null);

        Mockito.when(mapper.convertValue(Mockito.any(), Mockito.eq(ValidatePrnResponse.class)))
                .thenReturn(response);
        paymentService.validatePrn(request);
    }

    @Test
    public void insertPartnersAuthTransaction_Success() {

        EventModel eventModel = new EventModel();
        Event event = new Event();

        Map<String, Object> data = new HashMap<>();

        data.put(PaymentConstants.ID, "TXN123");
        data.put(PaymentConstants.REQUEST_DTIMES, LocalDateTime.now());
        data.put(PaymentConstants.RESPONSE_DTIMES, LocalDateTime.now());
        data.put(PaymentConstants.REQUEST_TRN_ID, "REQ123");
        data.put(PaymentConstants.AUTH_TYPE_CODE, "OTP");
        data.put(PaymentConstants.STATUS_CODE, "SUCCESS");
        data.put(PaymentConstants.STATUS_COMMENT, "Completed");
        data.put(PaymentConstants.REQUESTED_ENTITY_ID, "ENT123");
        data.put(PaymentConstants.REQUESTED_ENTITY_NAME, "PartnerA");
        data.put(PaymentConstants.CHARGE_AMOUNT, 100.0);

        event.setData(data);
        eventModel.setEvent(event);

        Mockito.when(
                authTransactionRepository.save(Mockito.any())
        ).thenReturn(new AuthTransaction());

        paymentService.insertPartnersAuthTransaction(eventModel);
        Mockito.verify(authTransactionRepository)
                .save(Mockito.any(AuthTransaction.class));

        Mockito.verify(auditUtil)
                .setAuditRequestDto(
                        PartnerServiceAuditEnum.AUTH_TRANSACTION_DB_SAVE_SUCCESS,
                        "PartnerA",
                        "partner"
                );
    }

    @Test(expected = ApiAccessibleException.class)
    public void insertPartnersAuthTransaction_DBFailure() {

        EventModel eventModel = new EventModel();
        Event event = new Event();

        Map<String, Object> data = new HashMap<>();

        data.put(PaymentConstants.ID, "TXN123");
        data.put(PaymentConstants.REQUEST_DTIMES, LocalDateTime.now());
        data.put(PaymentConstants.RESPONSE_DTIMES, LocalDateTime.now());
        data.put(PaymentConstants.REQUEST_TRN_ID, "REQ123");
        data.put(PaymentConstants.AUTH_TYPE_CODE, "OTP");
        data.put(PaymentConstants.STATUS_CODE, "FAILED");
        data.put(PaymentConstants.STATUS_COMMENT, "Error");
        data.put(PaymentConstants.REQUESTED_ENTITY_ID, "ENT123");
        data.put(PaymentConstants.REQUESTED_ENTITY_NAME, "PartnerA");
        data.put(PaymentConstants.CHARGE_AMOUNT, 100.0);

        event.setData(data);
        eventModel.setEvent(event);
        Mockito.when(
                authTransactionRepository.save(Mockito.any())
        ).thenThrow(new RuntimeException("DB Error"));

        paymentService.insertPartnersAuthTransaction(eventModel);
        Mockito.verify(auditUtil)
                .setAuditRequestDto(
                        PartnerServiceAuditEnum.AUTH_TRANSACTION_DB_SAVE_FAILURE,
                        "PartnerA",
                        "partner"
                );
    }


}
