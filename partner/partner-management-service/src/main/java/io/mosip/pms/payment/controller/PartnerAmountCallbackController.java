package io.mosip.pms.payment.controller;

import io.mosip.kernel.core.logger.spi.Logger;
import io.mosip.kernel.core.websub.model.EventModel;
import io.mosip.kernel.websub.api.annotation.PreAuthenticateContentAndVerifyIntent;
import io.mosip.pms.common.util.PMSLogger;
import io.mosip.pms.payment.service.PaymentService;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class PartnerAmountCallbackController {

    @Autowired
    PaymentService paymentService;


    private static final Logger logger = PMSLogger.getLogger(PartnerAmountCallbackController.class);

    @PostMapping(path = "/callback/partnermanagement/partners_amount_ack", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    //@PreAuthenticateContentAndVerifyIntent(secret = "${partner-websub-ida-partner-service-callback-secret}", callback = "/v1/partnermanager/callback/partnermanagement/partners_amount_ack", topic = "${pms.websub.topic.partner.amount.updated}")
    public ResponseEntity<String> handleSubscribeEvent(@RequestBody EventModel eventModel) throws Exception {
        logger.info("PartnerServiceCallbackController", "PartnerAmountUpdatedAck");
        try {
            paymentService.paymentSettled(eventModel);
        } catch (Exception e) {
            logger.error("PartnerServiceCallbackController",
                    ExceptionUtils.getFullStackTrace(e));
        }
        return new ResponseEntity<>("request accepted.", HttpStatus.OK);
    }

}