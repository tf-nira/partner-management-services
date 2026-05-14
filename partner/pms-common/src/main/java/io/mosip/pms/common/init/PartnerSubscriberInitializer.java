package io.mosip.pms.common.init;

import io.mosip.kernel.core.logger.spi.Logger;
import io.mosip.pms.common.helper.WebSubPublisher;
import io.mosip.pms.common.util.PMSLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@ConditionalOnProperty(
        name = "websub.subscription.enabled",
        havingValue = "true",
        matchIfMissing = true
)
@Component
public class PartnerSubscriberInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger logger = PMSLogger.getLogger(PartnerSubscriberInitializer.class);
    @Autowired
    WebSubPublisher webSubPublisher;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        logger.info("onApplicationEvent ... subscribeForSettledEvents");
        webSubPublisher.subscribeForSettledPrn();
        webSubPublisher.subscribeForBalanceUpdate();
        webSubPublisher.subscribeForAuthTransaction();

    }
}
