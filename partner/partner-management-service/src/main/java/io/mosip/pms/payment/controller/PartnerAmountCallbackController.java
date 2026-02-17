package io.mosip.pms.payment.controller;

import io.mosip.kernel.core.logger.spi.Logger;
import io.mosip.kernel.core.websub.model.EventModel;
import io.mosip.kernel.websub.api.annotation.PreAuthenticateContentAndVerifyIntent;
import io.mosip.pms.common.constant.ConfigKeyConstants;
import io.mosip.pms.common.util.PMSLogger;
import io.mosip.pms.payment.service.PaymentService;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class PartnerAmountCallbackController {

    @Autowired
    PaymentService paymentService;

    private static final Logger logger = PMSLogger.getLogger(PartnerAmountCallbackController.class);

//    @RequestMapping(
//            value = "/callback/partnermanagement/partners_amount_ack",
//            method = {RequestMethod.GET, RequestMethod.POST}
//    )
//    @PreAuthenticateContentAndVerifyIntent(secret = "${" + ConfigKeyConstants.PARTNER_WEBSUB_IDA_PARTNER_SERVICE_CALLBACK_SECRET
//            + "}", callback = "${pms-websub-partner-service-partner-amount-updated-callback-relative-url}", topic = "${" + ConfigKeyConstants.topic + "}")
//    public void handlePartnerAmountUpdatedAck(
//            @RequestBody io.mosip.kernel.core.websub.model.EventModel eventModel) {
//        try {
//            logger.info("PartnerServiceCallbackController", "PartnerAmountUpdatedAck");
//            paymentService.paymentSettled(eventModel);
//        } catch (Exception e) {
//            logger.error("PartnerServiceCallbackController",
//                    ExceptionUtils.getFullStackTrace(e));
//        }
//    }

    @GetMapping("/callback/partnermanagement/partners_amount_ack")
    public ResponseEntity<String> verifySubscription(
            @RequestParam("hub.mode") String mode,
            @RequestParam("hub.topic") String topic,
            @RequestParam("hub.challenge") String challenge,
            @RequestParam("hub.lease_seconds") String leaseSeconds) {

        logger.info("Verifying WebSub subscription");
        return ResponseEntity.ok(challenge);
    }

    @PostMapping(
            value = "/callback/partnermanagement/partners_amount_ack",
            consumes = "application/json"
    )
    @PreAuthenticateContentAndVerifyIntent(
            secret = "${" + ConfigKeyConstants.PARTNER_WEBSUB_IDA_PARTNER_SERVICE_CALLBACK_SECRET + "}",
            callback = "${pms-websub-partner-service-partner-amount-updated-callback-relative-url}",
            topic = "${" + ConfigKeyConstants.topic + "}"
    )
    public void handlePartnerAmountUpdatedAck(
            @RequestBody EventModel eventModel) {

        try {
            logger.info("PartnerAmountUpdatedAck received");
            paymentService.paymentSettled(eventModel);
        } catch (Exception e) {
            logger.error("PartnerServiceCallbackController",
                    ExceptionUtils.getFullStackTrace(e));
        }
    }



}