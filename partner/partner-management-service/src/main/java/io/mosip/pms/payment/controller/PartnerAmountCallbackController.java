package io.mosip.pms.payment.controller;

import io.mosip.kernel.core.logger.spi.Logger;
import io.mosip.kernel.websub.api.annotation.PreAuthenticateContentAndVerifyIntent;
import io.mosip.pms.common.constant.ConfigKeyConstants;
import io.mosip.pms.common.util.PMSLogger;
import io.mosip.pms.payment.service.PaymentService;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class PartnerAmountCallbackController {

    @Autowired
    PaymentService paymentService;

    private static final Logger logger = PMSLogger.getLogger(PartnerAmountCallbackController.class);
    @PostMapping(
            value = "/callback/partnermanagement/partners_amount_ack",
            consumes = "application/json"
    )
    	@PreAuthenticateContentAndVerifyIntent(secret = "${" + ConfigKeyConstants.PARTNER_WEBSUB_IDA_PARTNER_SERVICE_CALLBACK_SECRET
			+ "}", callback = "${pms-websub-partner-service-partner-amount-updated-callback-relative-url}", topic = "${" + ConfigKeyConstants.topic + "}")
    public void handlePartnerAmountUpdatedAck(
            @RequestBody io.mosip.kernel.core.websub.model.EventModel eventModel) {
        try {
            logger.info("PartnerServiceCallbackController", "PartnerAmountUpdatedAck");
            paymentService.paymentSettled(eventModel);
        } catch (Exception e) {
            logger.error("PartnerServiceCallbackController",
                    ExceptionUtils.getFullStackTrace(e));
        }
    }
}