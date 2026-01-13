package io.mosip.pms.payment.controller;

import io.mosip.kernel.core.http.ResponseFilter;
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
    public ResponseWrapper<PrnResponse> generatePrn(
            @RequestBody @Valid RequestWrapper<PrnRequest> request) {

        auditUtil.setAuditRequestDto(PartnerServiceAuditEnum.GENERATE_PARTNER_PRN);
        ResponseWrapper<PrnResponse> responseWrapper = new ResponseWrapper<>();
        responseWrapper.setResponse(paymentService.generatePrn(request.getRequest()));
        return responseWrapper;
    }



    @ResponseFilter
    @PostMapping("/validatePrn")
    @PreAuthorize("hasAnyRole(@authorizedRoles.getPostpartnerpayment())")
    @Operation(summary = "Service to validate prn", description = "Service to validate prn")
    public ResponseWrapper<ValidatePrnResponse> validatePartnerPrn(
            @RequestBody @Valid RequestWrapper<ValidatePrnRequest> request) {
        ResponseWrapper<ValidatePrnResponse> responseWrapper = new ResponseWrapper<>();
        auditUtil.setAuditRequestDto(PartnerServiceAuditEnum.VALIDATE_PARTNER_PRN);
        responseWrapper.setResponse(paymentService.validatePrn(request.getRequest()));
        return responseWrapper;
    }
}
