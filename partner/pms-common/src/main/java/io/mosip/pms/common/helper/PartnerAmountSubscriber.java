package io.mosip.pms.common.helper;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class PartnerAmountSubscriber {

    @Value("${pms.websub.topic.partner.amount.updated}")
    private String topic;

    @Value("${pms-websub-partner-service-partner-amount-updated-callback-relative-url}")
    private String callbackUrl;

    @Autowired
    private PmsWebSubHelper webSubHelper;

    @PostConstruct
    public void init() {
        subscribe();
    }

    @Scheduled(fixedDelayString = "${websub.resubscribe.interval}")
    public void resubscribe() {
        subscribe();
    }

    private void subscribe() {
        webSubHelper.subscribe(topic, callbackUrl);
    }
}
