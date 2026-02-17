package io.mosip.pms.common.helper;

import io.mosip.kernel.core.logger.spi.Logger;
import io.mosip.kernel.core.websub.spi.PublisherClient;
import io.mosip.kernel.websub.api.constants.WebSubClientErrorCode;
import io.mosip.kernel.websub.api.exception.WebSubClientException;
import io.mosip.pms.common.util.PMSLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import io.mosip.kernel.core.websub.spi.SubscriptionClient;
import io.mosip.kernel.websub.api.model.SubscriptionChangeRequest;
import io.mosip.kernel.websub.api.model.SubscriptionChangeResponse;
import io.mosip.kernel.websub.api.model.UnsubscriptionRequest;

import java.util.HashSet;
import java.util.Set;

@ConditionalOnProperty(
        name = "websub.subscription.enabled",
        havingValue = "true",
        matchIfMissing = true
)
@Component
@Async("webSubHelperExecutor")
public class PmsWebSubHelper {

    private static final Logger logger = PMSLogger.getLogger(PmsWebSubHelper.class);

    @Value("${websub.hub.url}")
    private String hubURL;
    @Value("${partner-websub-ida-partner-service-callback-secret}")
    private String callbackSecret;
    @Value("${pms.websub.topic.partner.amount.updated}")
    private String topic;
    @Value("${pms-websub-partner-service-partner-amount-updated-callback-relative-url}")
    private String callbackUrl;

    @Autowired
    protected SubscriptionClient<
            SubscriptionChangeRequest,
            UnsubscriptionRequest,
            SubscriptionChangeResponse> subscriptionClient;
    @Autowired
    private PublisherClient<String, Object, HttpHeaders> publisher;

    @Scheduled(fixedDelayString = "${websub.resubscribe.interval}",
            initialDelayString = "${websub.event.delay-millisecs}")
    public void initSubsriptions() {
        tryRegisteringTopic(topic);
        logger.info("Initializing subscribptions... ");
        subscribeForSettledEvents();
    }

    private Set<String> registeredTopicCache = new HashSet<>();

    /*
     * Cacheable is added to execute topic registration only once per topic
     */
    public void tryRegisteringTopic(String topic) {
        if (!registeredTopicCache.contains(topic)) {
            try {
                this.registerTopic(topic);
                registeredTopicCache.add(topic);
            } catch (WebSubClientException e) {
                if (WebSubClientErrorCode.REGISTER_ERROR.getErrorCode().equals(e.getErrorCode())) {
                    // If topic is already registered this error is expected, then we will add the
                    // topic to cache
                    registeredTopicCache.add(topic);
                }
                logger.info("try registering tipic... ");
            } catch (Exception e) {
                logger.info("error registaering topic");
            }
        }
    }

    public void registerTopic(String topic) {
        publisher.registerTopic(topic, hubURL);
    }
    private void subscribeForSettledEvents() {
        try {
            SubscriptionChangeRequest subscriptionRequest = new SubscriptionChangeRequest();
            subscriptionRequest.setCallbackURL(callbackUrl);
            subscriptionRequest.setHubURL(hubURL);
            subscriptionRequest.setSecret(callbackSecret);
            subscriptionRequest.setTopic(topic);
            logger.info("subscribing... ");
            subscriptionClient.subscribe(subscriptionRequest);
        } catch (WebSubClientException e) {
            logger.info("error in subscribing... ");
        }
    }
}
