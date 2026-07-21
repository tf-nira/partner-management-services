package io.mosip.pms.payment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.mosip.kernel.core.logger.spi.Logger;
import io.mosip.kernel.core.websub.model.EventModel;
import io.mosip.kernel.websub.api.annotation.PreAuthenticateContentAndVerifyIntent;
import io.mosip.pms.common.util.PMSLogger;
import io.mosip.pms.payment.service.PaymentService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.http.HttpServletRequest;


@RestController
public class AuthTransactionCallbackController {
    

    @Autowired
    PaymentService paymentService;

    private static final Logger logger = PMSLogger.getLogger(AuthTransactionCallbackController.class);

    @PostMapping(path = "/callback/partnermanagement/partners_auth_trn", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Request authenticated successfully"),
            @ApiResponse(responseCode = "201", description = "Created", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(hidden = true)))})
    @PreAuthenticateContentAndVerifyIntent(secret = "${partner-websub-ida-partner-service-callback-secret}", callback = "/v1/partnermanager/callback/partnermanagement/partners_auth_trn", topic = "${pms.websub.topic.partner.auth.transactions}")
    public void insertPartnerAuthTransactionEvent(HttpServletRequest request) throws Exception {
        logger.info("Entering Into insertPartnerAuthTransactionEvent..........");
        ContentCachingRequestWrapper cachingRequest = (ContentCachingRequestWrapper) request;
        byte[] cachedBody = cachingRequest.getContentAsByteArray();

        ObjectMapper mapper = new ObjectMapper();
        EventModel eventModel = mapper.readValue(cachedBody, EventModel.class);
        try {
            paymentService.insertPartnersAuthTransaction(eventModel);
        } catch (Exception e) {
            logger.info("PartnerServiceCallbackController entered exc..........");
            logger.error("PartnerServiceCallbackController",
                    ExceptionUtils.getFullStackTrace(e));
            throw e;
        }
    }

}
