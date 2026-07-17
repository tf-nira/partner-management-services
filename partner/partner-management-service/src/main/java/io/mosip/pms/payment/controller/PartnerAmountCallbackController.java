package io.mosip.pms.payment.controller;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;


@RestController
public class PartnerAmountCallbackController {

    @Value("${partner-websub-ida-partner-service-callback-secret}")
    private String partnerSecrettest;

    @Value("${pms.websub.topic.partner.amount.updated.ack}")
    private String partnerTopictest;

    @PostConstruct
    public void logConfig() {
        logger.info("WebSub Secretsk..............: {}", partnerSecrettest);
        logger.info("WebSub Topicsk................: {}", partnerTopictest);
    }

    @Autowired
    PaymentService paymentService;

    private static final Logger logger = PMSLogger.getLogger(PartnerAmountCallbackController.class);

//    @PostMapping(path = "/callback/partnermanagement/partners_amount_ack", consumes = MediaType.APPLICATION_JSON_VALUE)
//    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Request authenticated successfully"),
//            @ApiResponse(responseCode = "201", description = "Created", content = @Content(schema = @Schema(hidden = true))),
//            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
//            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(hidden = true))),
//            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(hidden = true)))})
//  //@PreAuthenticateContentAndVerifyIntent(secret = "${partner-websub-ida-partner-service-callback-secret}", callback = "/v1/partnermanager/callback/partnermanagement/partners_amount_ack", topic = "${pms.websub.topic.partner.amount.updated.ack}")
//    public void prnStatusUpdateEvent(@RequestBody EventModel eventModel) throws Exception {
//        logger.info("Enterring Into prnStatusUpdateEvent..........");
//        try {
//            paymentService.prnStatusUpdateIda(eventModel);
//        } catch (Exception e) {
//            logger.info("PartnerServiceCallbackController entered exc..........");
//            logger.error("PartnerServiceCallbackController",
//                    ExceptionUtils.getFullStackTrace(e));
//        }
//    }

    @PostMapping(path = "/callback/partnermanagement/partners_amount_ack", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void prnStatusUpdateEvent(
            @RequestBody EventModel eventModel,
            HttpServletRequest request) throws Exception {

        logger.info("===== Entered prnStatusUpdateEvent callback =====");

        // Log request details
        logger.info("Method      : {}", request.getMethod());
        logger.info("Request URI : {}", request.getRequestURI());
        logger.info("Remote Host : {}", request.getRemoteAddr());

        // Log all headers
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String header = headerNames.nextElement();
            logger.info("Header [{}] = {}", header, request.getHeader(header));
        }

        // Log the WebSub signature separately
        logger.info("x-hub-signature = {}", request.getHeader("x-hub-signature"));

        // Log the payload
        logger.info("Received Event : {}", eventModel);

        try {
            paymentService.prnStatusUpdateIda(eventModel);
            logger.info("Successfully processed partner amount acknowledgement.");
        } catch (Exception e) {
            logger.error("Exception while processing callback", e);
            throw e;
        }

        logger.info("===== Exiting prnStatusUpdateEvent callback =====");
    }


    @PostMapping(path = "/callback/partnermanagement/partners_balance_update", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Request authenticated successfully"),
            @ApiResponse(responseCode = "201", description = "Created", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(hidden = true)))})
   //@PreAuthenticateContentAndVerifyIntent(secret = "${partner-websub-ida-partner-service-callback-secret}", callback = "/v1/partnermanager/callback/partnermanagement/partners_balance_update", topic = "${pms.websub.topic.partner.balance.updated}")
    public void partnersBalanceUpdateEvent(@RequestBody EventModel eventModel) throws Exception {
        logger.info("Enterring Into partnersBalanceUpdateIda..........");
        try {
            paymentService.partnersBalanceUpdateIda(eventModel);
        } catch (Exception e) {
            logger.info("PartnerServiceCallbackController entered exc..........");
            logger.error("PartnerServiceCallbackController",
                    ExceptionUtils.getFullStackTrace(e));
        }
    }
}
