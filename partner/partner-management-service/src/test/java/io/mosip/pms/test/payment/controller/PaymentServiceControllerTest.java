package io.mosip.pms.test.payment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.mosip.pms.common.dto.PageResponseDto;
import io.mosip.pms.common.dto.Pagination;
import io.mosip.pms.common.dto.SearchDto;
import io.mosip.pms.common.entity.PartnerPaymentTransactions;
import io.mosip.pms.common.entity.PartnerPrn;

import org.junit.Test;
import io.mosip.pms.payment.controller.PaymentServiceController;
import io.mosip.pms.payment.request.dto.PrnRequest;
import io.mosip.pms.payment.request.dto.ValidatePrnRequest;
import io.mosip.pms.payment.response.dto.PrnResponse;
import io.mosip.pms.payment.response.dto.ValidatePrnResponse;
import io.mosip.pms.common.request.dto.RequestWrapper;
import io.mosip.pms.payment.service.PaymentService;
import io.mosip.pms.device.util.AuditUtil;

import org.junit.runner.RunWith;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;


import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
/**
 *
 * @author Jagadeesh
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest(PaymentServiceController.class)
@AutoConfigureMockMvc
public class PaymentServiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    @MockBean
    private AuditUtil auditUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = {"PARTNER"})
    public void testGeneratePrnSuccess() throws Exception {

        PrnRequest prnRequest = new PrnRequest();
        prnRequest.setService("PAYMENT");
        prnRequest.setPartnerId("mosip");
        prnRequest.setServiceCode("IDA");
        prnRequest.setNin("1234567890");
        prnRequest.setFullName("Test User");

        RequestWrapper<PrnRequest> request = new RequestWrapper<>();
        request.setRequest(prnRequest);
        request.setId("mosip.pms.generate.prn");
        request.setVersion("1.0");
        request.setRequesttime(ZonedDateTime.now(ZoneOffset.UTC).toLocalDateTime());
        request.setMetadata("{}");

        PrnResponse prnResponse = new PrnResponse();
        prnResponse.setId("mosip.pms.generate.prn");
        prnResponse.setVersion("1.0");

        Mockito.when(paymentService.generatePrn(prnRequest))
        		.thenReturn(prnResponse);

        mockMvc.perform(post("/partners/generatePrn")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"PARTNER"})
    public void testValidatePartnerPrnSuccess() throws Exception {

        ValidatePrnRequest validateRequest = new ValidatePrnRequest();
        validateRequest.setPrn("2240015260462");
        validateRequest.setPartnerId("mosip");
        validateRequest.setServiceCode("IDA");
        validateRequest.setAmount(Double.valueOf(100));

        RequestWrapper<ValidatePrnRequest> wrapper = new RequestWrapper<>();
        wrapper.setRequest(validateRequest);

        ValidatePrnResponse response = new ValidatePrnResponse();
        response.setId("mosip.pms.validate.prn");
        response.setVersion("1.0");

        Mockito.when(paymentService.validatePrn(validateRequest))
        		.thenReturn(response);

        mockMvc.perform(post("/partners/validatePrn")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(wrapper)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"PARTNER"})
    public void testGeneratePrnFailure() throws Exception {

        PrnRequest prnRequest = new PrnRequest();
        prnRequest.setService("PAYMENT");
        prnRequest.setPartnerId("mosip");
        prnRequest.setServiceCode("IDA");
        prnRequest.setNin("1234567890");
        prnRequest.setFullName("Test User");

        RequestWrapper<PrnRequest> request = new RequestWrapper<>();
        request.setRequest(prnRequest);

        Mockito.when(paymentService.generatePrn(prnRequest))
                .thenThrow(new RuntimeException("PRN generation failed"));

        mockMvc.perform(post("/partners/generatePrn")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()) // because controller wraps error in response body
                .andExpect(jsonPath("$.errors").exists());
    }

    @Test
    @WithMockUser(roles = {"PARTNER"})
    public void testValidatePartnerPrnFailure() throws Exception {

        ValidatePrnRequest validateRequest = new ValidatePrnRequest();
        validateRequest.setPrn("2240015260462");
        validateRequest.setPartnerId("mosip");
        validateRequest.setServiceCode("IDA");
        validateRequest.setAmount(Double.valueOf(100));

        RequestWrapper<ValidatePrnRequest> wrapper = new RequestWrapper<>();
        wrapper.setRequest(validateRequest);

        Mockito.when(paymentService.validatePrn(validateRequest))
                .thenThrow(new RuntimeException("Invalid PRN"));

        mockMvc.perform(post("/partners/validatePrn")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrapper)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errors").exists());
    }

    @Test
    @WithMockUser(roles = {"PARTNER"})
    public void testSearchPaymentSuccess() throws Exception {

        SearchDto searchDto = new SearchDto();
        searchDto.setFilters(Collections.emptyList());
        searchDto.setSort(Collections.emptyList());

        Pagination pagination = new Pagination();
        pagination.setPageStart(1);
        pagination.setPageFetch(8);
        searchDto.setPagination(pagination);

        RequestWrapper<SearchDto> request = new RequestWrapper<>();
        request.setRequest(searchDto);
        
        PartnerPaymentTransactions transaction = new PartnerPaymentTransactions();
        transaction.setTransactionId("fffff");
        transaction.setPartnerId("xdfd");
        transaction.setEntryType("ddfd");
        transaction.setAmount(Double.valueOf(235));
        transaction.setSourceSystem("sdsd");
        transaction.setDescription("fvhfg");

        List<PartnerPaymentTransactions> list =
                Collections.singletonList(transaction);

        PageResponseDto<PartnerPaymentTransactions> pageResponse = new PageResponseDto<>();
        pageResponse.setFromRecord(1);
        pageResponse.setToRecord(8);
        pageResponse.setTotalRecord(8);
        pageResponse.setData(list);

        Mockito.when(paymentService.searchPayment(Mockito.any(SearchDto.class)))
                .thenReturn(pageResponse);
        mockMvc.perform(post("/partners/payment/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"PARTNER"})
    public void testSearchPaymentFailure() throws Exception {

        SearchDto searchDto = new SearchDto();
        searchDto.setFilters(Collections.emptyList());
        searchDto.setPagination(new Pagination());

        RequestWrapper<SearchDto> request = new RequestWrapper<>();
        request.setRequest(searchDto);

        Mockito.when(paymentService.searchPayment(Mockito.any(SearchDto.class)))
                .thenThrow(new RuntimeException("Search failed"));

        mockMvc.perform(post("/partners/payment/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errors").exists());
    }

    @Test
    @WithMockUser(roles = "PARTNER")
    public void testSearchPartnerPrn_Success() throws Exception {

    	SearchDto searchDto = new SearchDto();
        searchDto.setFilters(Collections.emptyList());
        searchDto.setSort(Collections.emptyList());

        Pagination pagination = new Pagination();
        pagination.setPageStart(1);
        pagination.setPageFetch(8);
        searchDto.setPagination(pagination);
        
        RequestWrapper<SearchDto> request = new RequestWrapper<>();
        request.setRequest(searchDto);

        PartnerPrn prn = new PartnerPrn();
        prn.setPartnerId("xdfd");
        prn.setPrn("PRN123");
        prn.setAmount(Double.valueOf(235));
        prn.setServiceCode("IDA");
        prn.setStatus("GENERATED");
        prn.setRemarks("Prn Generated");
        
        List<PartnerPrn> list =
                Collections.singletonList(prn);

        PageResponseDto<PartnerPrn> pageResponse = new PageResponseDto<>();
        pageResponse.setFromRecord(1);
        pageResponse.setToRecord(8);
        pageResponse.setTotalRecord(8);
        pageResponse.setData(list);

        Mockito.when(paymentService.searchPartnerPrn(Mockito.any(SearchDto.class)))
        		.thenReturn(pageResponse);
        mockMvc.perform(post("/partners/prn/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "PARTNER")
    public void testSearchPartnerPrn_Unauthorized() throws Exception {
    	
    	SearchDto searchDto = new SearchDto();
        searchDto.setFilters(Collections.emptyList());
        searchDto.setPagination(new Pagination());
        
    	RequestWrapper<SearchDto> request = new RequestWrapper<>();
        request.setRequest(searchDto);
        
        Mockito.when(paymentService.searchPartnerPrn(Mockito.any(SearchDto.class)))
        .thenThrow(new RuntimeException("Prn Search failed"));

        mockMvc.perform(post("/partners/prn/search")
                		.contentType(MediaType.APPLICATION_JSON)
                		.content(objectMapper.writeValueAsString(request)))
        		.andExpect(status().isOk())
        		.andExpect(jsonPath("$.errors").exists());
    }


}
