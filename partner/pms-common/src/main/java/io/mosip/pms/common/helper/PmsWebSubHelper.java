package io.mosip.pms.common.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.mosip.kernel.core.retry.WithRetry;
import io.mosip.kernel.core.websub.spi.SubscriptionClient;
import io.mosip.kernel.websub.api.model.SubscriptionChangeRequest;
import io.mosip.kernel.websub.api.model.SubscriptionChangeResponse;
import io.mosip.kernel.websub.api.model.UnsubscriptionRequest;

@Component
public class PmsWebSubHelper {

    @Value("${websub.hub.url}")
    private String hubURL;
    @Value("${partner-websub-ida-partner-service-callback-secret}")
    private String callbackSecret;

    @Autowired
    protected SubscriptionClient<
            SubscriptionChangeRequest,
            UnsubscriptionRequest,
            SubscriptionChangeResponse> subscriptionClient;

    @WithRetry
    public SubscriptionChangeResponse subscribe(String topic, String callbackUrl) {

        SubscriptionChangeRequest request = new SubscriptionChangeRequest();
        request.setHubURL(hubURL);
        request.setTopic(topic);
        request.setCallbackURL(callbackUrl);
        request.setSecret(callbackSecret); // better inject from property
        System.out.println("SECRET SENT TO HUB = " + request.getSecret());

        return subscriptionClient.subscribe(request);
    }
}
