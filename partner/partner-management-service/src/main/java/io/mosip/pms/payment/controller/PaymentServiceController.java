package io.mosip.pms.payment.controller;

import io.mosip.kernel.core.http.ResponseFilter;
import io.mosip.pms.common.dto.PageResponseDto;
import io.mosip.pms.common.dto.SearchDto;
import io.mosip.pms.common.entity.PartnerBalance;
import io.mosip.pms.common.entity.PartnerPaymentTransactions;
import io.mosip.pms.common.entity.PartnerPrn;
import io.mosip.pms.common.request.dto.RequestWrapper;
import io.mosip.pms.common.response.dto.ResponseWrapper;
import io.mosip.pms.device.util.AuditUtil;
import io.mosip.pms.partner.constant.PartnerServiceAuditEnum;
import io.mosip.pms.payment.service.PaymentService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.mosip.pms.payment.request.dto.PrnRequest;
import io.mosip.pms.payment.request.dto.ValidatePrnRequest;
import io.mosip.pms.payment.response.dto.PrnResponse;
import io.mosip.pms.payment.response.dto.ValidatePrnResponse;
import javax.validation.Valid;
/**
 *
 * @author Karthik S, Jagadeesh
 *
 */
@RestController
@RequestMapping(value = "/partners")
@Api(tags = { "Partner Payment Controller" })
public class PaymentServiceController {

    @Autowired
    AuditUtil auditUtil;

    @Autowired
    PaymentService paymentService;

    @ResponseFilter
    @PostMapping(
            value = "/generatePrn",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAnyRole(@authorizedRoles.getPostpartnerpayment())")
    @Operation(summary = "Service to generate PRN", description = "Generates a PRN for a given request")
    public PrnResponse generatePrn(
            @RequestBody @Valid RequestWrapper<PrnRequest> request) {

        auditUtil.setAuditRequestDto(PartnerServiceAuditEnum.GENERATE_PARTNER_PRN);
        return paymentService.generatePrn(request.getRequest());
    }



    @ResponseFilter
    @PostMapping("/validatePrn")
    @PreAuthorize("hasAnyRole(@authorizedRoles.getPostpartnerpayment())")
    @Operation(summary = "Service to validate prn", description = "Service to validate prn")
    public ValidatePrnResponse validatePartnerPrn(
            @RequestBody @Valid RequestWrapper<ValidatePrnRequest> request) {
        auditUtil.setAuditRequestDto(PartnerServiceAuditEnum.VALIDATE_PARTNER_PRN);
        return paymentService.validatePrn(request.getRequest());
    }


    @ResponseFilter
    @PostMapping("/payment/search")
    @PreAuthorize("hasAnyRole(@authorizedRoles.getPostpaymentsearch())")
    @Operation(summary = "Service to search payment details", description = "Service to search payment details")
    public ResponseWrapper<PageResponseDto<PartnerPaymentTransactions>> searchPartner(
            @RequestBody @Valid RequestWrapper<SearchDto> request) {
        ResponseWrapper<PageResponseDto<PartnerPaymentTransactions>> responseWrapper = new ResponseWrapper<>();
        auditUtil.setAuditRequestDto(PartnerServiceAuditEnum.SEARCH_PAYMENT);
        responseWrapper.setResponse(paymentService.searchPayment(request.getRequest()));
        return responseWrapper;
    }
    
    @ResponseFilter
    @PostMapping("/prn/search")
    @PreAuthorize("hasAnyRole(@authorizedRoles.getPostpartnerprnsearch())")
    @Operation(summary = "Service to search prn details", description = "Service to search prn details")
    public ResponseWrapper<PageResponseDto<PartnerPrn>> searchPartnerPrn(
            @RequestBody @Valid RequestWrapper<SearchDto> request) {
        ResponseWrapper<PageResponseDto<PartnerPrn>> responseWrapper = new ResponseWrapper<>();
        auditUtil.setAuditRequestDto(PartnerServiceAuditEnum.SEARCH_PARTNER_PRN);
        responseWrapper.setResponse(paymentService.searchPartnerPrn(request.getRequest()));
        return responseWrapper;
    }

    @ResponseFilter
    @PostMapping("/balance/search")
    @PreAuthorize("hasAnyRole(@authorizedRoles.getPostpartnerbalancesearch())")
    @Operation(summary = "Service to search balance details", description = "Service to search balance details")
    public ResponseWrapper<PageResponseDto<PartnerBalance>> searchPartnerBalance(
            @RequestBody @Valid RequestWrapper<SearchDto> request) {
        ResponseWrapper<PageResponseDto<PartnerBalance>> responseWrapper = new ResponseWrapper<>();
        auditUtil.setAuditRequestDto(PartnerServiceAuditEnum.SEARCH_PARTNER_BALANCE);
        responseWrapper.setResponse(paymentService.searchPartnerBalance(request.getRequest()));
        return responseWrapper;
    }
}
