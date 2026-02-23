package io.mosip.pms.common.helper;

import java.util.Map;
import java.util.UUID;

import io.mosip.kernel.core.websub.spi.SubscriptionClient;
import io.mosip.kernel.websub.api.exception.WebSubClientException;
import io.mosip.kernel.websub.api.model.SubscriptionChangeRequest;
import io.mosip.kernel.websub.api.model.SubscriptionChangeResponse;
import io.mosip.kernel.websub.api.model.UnsubscriptionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import io.mosip.kernel.core.logger.spi.Logger;
import io.mosip.kernel.core.util.DateUtils;
import io.mosip.kernel.core.websub.spi.PublisherClient;
import io.mosip.pms.common.constant.EventType;
import io.mosip.pms.common.dto.Event;
import io.mosip.pms.common.dto.EventModel;
import io.mosip.pms.common.dto.Type;
import io.mosip.pms.common.util.PMSLogger;
import org.springframework.scheduling.annotation.Scheduled;

public class WebSubPublisher {

	private static final Logger logger = PMSLogger.getLogger(WebSubPublisher.class);
	
	@Value("${websub.publish.url}")
	private String webSubHubPublishUrl;
	
	@Autowired
	private PublisherClient<String, EventModel, HttpHeaders> pb;

//	@Autowired
//	private PublisherClient<String, Object, HttpHeaders> pbObj;

	@Value("${websub.hub.url}")
	private String hubURL;
	@Value("${partner-websub-ida-partner-service-callback-secret}")
	private String callbackSecret;
	@Value("${pms.websub.topic.partner.amount.updated.ack}")
	private String ackTopic;
	@Value("${pms-websub-partner-service-partner-amount-updated-ack-callback-relative-url}")
	private String callbackUrlSettled;
	@Value("${pms.websub.topic.partner.balance.updated}")
	private String balanceUpdateTopic;
	@Value("${pms-websub-partner-service-partner-balance-updated-callback-relative-url}")
	private String callbackUrlBalanceUpdate;

	@Autowired
	protected SubscriptionClient<
			SubscriptionChangeRequest,
			UnsubscriptionRequest,
			SubscriptionChangeResponse> subscriptionClient;

	@Scheduled(fixedDelayString = "${websub.resubscribe.interval}",
			initialDelayString = "${websub.event.delay-millisecs}")
	public void initSubsriptions() {
		logger.info("Initializing subscribptions... ");
		subscribeForSettledPrn();
		subscribeForBalanceUpdate();
	}

	public void subscribeForSettledPrn() {
		try {
			SubscriptionChangeRequest subscriptionRequest = new SubscriptionChangeRequest();
			subscriptionRequest.setCallbackURL(callbackUrlSettled);
			subscriptionRequest.setHubURL(hubURL);
			subscriptionRequest.setSecret(callbackSecret);
			subscriptionRequest.setTopic(ackTopic);
			logger.info("subscribing... settled");
			subscriptionClient.subscribe(subscriptionRequest);
		} catch (WebSubClientException e) {
			logger.info("error in subscribing... ");
		}
	}

	public void subscribeForBalanceUpdate() {
		try {
			SubscriptionChangeRequest subscriptionRequest = new SubscriptionChangeRequest();
			subscriptionRequest.setCallbackURL(callbackUrlBalanceUpdate);
			subscriptionRequest.setHubURL(hubURL);
			subscriptionRequest.setSecret(callbackSecret);
			subscriptionRequest.setTopic(balanceUpdateTopic);
			logger.info("subscribing... balance");
			subscriptionClient.subscribe(subscriptionRequest);
		} catch (WebSubClientException e) {
			logger.info("error in subscribing... ");
		}
	}
	
	
	public void notify(EventType eventType,Map<String,Object> data,Type type) {
		sendEventToIDA(createEventModel(eventType,data,type));
	}
	
	private void sendEventToIDA(EventModel model) {
		try {
			logger.info(this.getClass().getSimpleName(), "sendEventToIDA", "Trying registering topic: " + model.getTopic());
			pb.registerTopic(model.getTopic(), webSubHubPublishUrl);
		} catch (Exception e) {
			//Exception will be there if topic already registered. Ignore that
			logger.warn(this.getClass().getSimpleName(), "sendEventToIDA", "Error in registering topic: " + model.getTopic() + " : " + e.getMessage() );
		}
		logger.info(this.getClass().getSimpleName(), "sendEventToIDA", "Publising event to topic: " + model.getTopic());
		pb.publishUpdate(model.getTopic(), model, MediaType.APPLICATION_JSON_VALUE, null, webSubHubPublishUrl);
	}
	
	private EventModel createEventModel(EventType eventType,Map<String,Object> data,Type type) {
		EventModel model = new EventModel();
		model.setPublisher(type.getName());
		String dateTime = DateUtils.formatToISOString(DateUtils.getUTCCurrentDateTime());
		model.setPublishedOn(dateTime);
		Event event = new Event();
		event.setTimestamp(dateTime);
		String eventId = UUID.randomUUID().toString();
		event.setId(eventId);
		event.setType(type);
		event.setData(data);
		model.setEvent(event);
		model.setTopic(eventType.toString());
		return model;
	}
}
