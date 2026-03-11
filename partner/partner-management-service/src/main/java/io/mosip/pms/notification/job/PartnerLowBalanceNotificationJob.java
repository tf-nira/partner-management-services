package io.mosip.pms.notification.job;

import io.mosip.kernel.core.logger.spi.Logger;
import io.mosip.pms.common.constant.EventType;
import io.mosip.pms.common.entity.Partner;
import io.mosip.pms.common.entity.PartnerBalance;
import io.mosip.pms.common.entity.PartnerBalanceNotification;
import io.mosip.pms.common.exception.ApiAccessibleException;
import io.mosip.pms.common.repository.PartnerBalanceNotificationRepository;
import io.mosip.pms.common.repository.PartnerBalanceRepository;
import io.mosip.pms.common.repository.PartnerServiceRepository;
import io.mosip.pms.common.response.dto.NotificationDto;
import io.mosip.pms.common.service.NotificatonService;
import io.mosip.pms.common.util.PMSLogger;
import io.mosip.pms.partner.constant.ErrorCode;
import io.mosip.pms.partner.exception.PartnerServiceException;

import org.springframework.beans.factory.annotation.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Component
public class PartnerLowBalanceNotificationJob {

    private Logger logger = PMSLogger.getLogger(PartnerLowBalanceNotificationJob.class);

    @Autowired
    private PartnerBalanceRepository partnerBalanceRepository;

    @Autowired
    private PartnerBalanceNotificationRepository notificationRepo;

    @Autowired
    private NotificatonService notificationService;

    @Autowired
    private PartnerServiceRepository partnerRepository;

    @Value("${pms.balance.threshold.warning}")
    private Double warningThreshold;

    @Value("${pms.balance.threshold.critical}")
    private Double criticalThreshold;

    @Value("${pms.balance.notification.days}")
    private Integer notificationDays;

//    @Scheduled(
//            initialDelayString = "#{60 * 60 * 1000 * ${pms.balance.notification.init-delay}}",
//            fixedRateString = "#{60 * 60 * 1000 * ${pms.balance.notification.fixed-rate}}"
//    )
    @Scheduled(
        initialDelayString = "${pms.notifications-schedule.init-delay}",
        fixedRateString = "${pms.notifications-schedule.fixed-rate}"
    )
    public void checkLowBalancePartners() {

        logger.info("Starting Low Balance Notification Job");

        resolveRestoredBalances();

        List<PartnerBalance> partners = partnerBalanceRepository.findPartnersBelowThreshold(warningThreshold);

        List<NotificationDto> notifications = new ArrayList<>();

        for (PartnerBalance partner : partners) {
            Double balance = partner.getBalance();
            int currentLevel = getCurrentLevel(balance);

            if (currentLevel == 0) {
                continue;
            }

            PartnerBalanceNotification record = notificationRepo.findByPartnerIdAndResolvedFalse(partner.getPartnerId());

            if (record == null) {
                record = createNewNotification(partner.getPartnerId(), currentLevel);
                try {
                    notificationRepo.save(record);
                } catch(Exception dbEx){
                    logger.error("PartnerBalanceNotification new record  failed to save to local DB: {}", dbEx.getMessage());
                    throw new ApiAccessibleException("DB_ERROR", "PartnerBalanceNotification new record failed to persist");
                }
                notifications.add(prepareNotificationDto(partner, currentLevel));
                continue;
            }

            if (currentLevel > record.getNotificationLevel()) {
                record.setNotificationLevel(currentLevel);
                record.setNotificationCount(record.getNotificationCount() + 1);
                record.setLastNotifiedDate(LocalDateTime.now());
                record.setUpdDtimes(LocalDateTime.now());
                try {
                    notificationRepo.save(record);
                } catch(Exception dbEx){
                    logger.error("PartnerBalanceNotification  update record failed to save to local DB: {}", dbEx.getMessage());
                    throw new ApiAccessibleException("DB_ERROR", "PartnerBalanceNotification update record failed to persist");
                }
                notifications.add(prepareNotificationDto(partner, currentLevel));
                continue;
            }

            if (record.getNotificationCount() < notificationDays) {
                record.setNotificationCount(record.getNotificationCount() + 1);
                record.setLastNotifiedDate(LocalDateTime.now());
                record.setUpdDtimes(LocalDateTime.now());
                try {
                    notificationRepo.save(record);
                } catch(Exception dbEx){
                    logger.error("PartnerBalanceNotification  update record failed for configured days to save to local DB: {}", dbEx.getMessage());
                    throw new ApiAccessibleException("DB_ERROR", "PartnerBalanceNotification update record failed for configured days to persist");
                }
                notifications.add(prepareNotificationDto(partner, currentLevel));
            }
        }

        if (!notifications.isEmpty()) {
            notificationService.sendNotications(EventType.PARTNERS_LOW_BALANCE, notifications);
            logger.info("Notifications sent: {}", notifications.size());
        }
        logger.info("Low Balance Notification Job Completed");
    }

    private void resolveRestoredBalances() {
        List<PartnerBalanceNotification> active = notificationRepo.findByResolvedFalse();

        for (PartnerBalanceNotification record : active) {
            PartnerBalance partner = partnerBalanceRepository.findById(record.getPartnerId()).orElse(null);
            if (partner != null && partner.getBalance() >= warningThreshold) {
                record.setResolved(true);
                record.setUpdDtimes(LocalDateTime.now());
                try {
                    notificationRepo.save(record);
                } catch(Exception dbEx){
                    logger.error("resolveRestoredBalances failed to save to local DB: {}", dbEx.getMessage());
                    throw new ApiAccessibleException("DB_ERROR", "resolveRestoredBalances update record failed to persist");
                }
                logger.info("Balance restored for partner {}", partner.getPartnerId());
            }
        }
    }

    private int getCurrentLevel(Double balance) {
        if (balance < criticalThreshold) {
            return 2;
        } else if (balance < warningThreshold) {
            return 1;
        }
        return 0;
    }

    private PartnerBalanceNotification createNewNotification(String partnerId, int level) {

        return PartnerBalanceNotification.builder()
                .id(UUID.randomUUID())
                .partnerId(partnerId)
                .notificationLevel(level)
                .notificationCount(1)
                .firstNotifiedDate(LocalDateTime.now())
                .lastNotifiedDate(LocalDateTime.now())
                .resolved(false)
                .crDtimes(LocalDateTime.now())
                .build();
    }

    private NotificationDto prepareNotificationDto(PartnerBalance partner, int level) {
        Partner partnerData = getValidPartner(partner.getPartnerId(), false);
        NotificationDto dto = new NotificationDto();
        dto.setPartnerId(partnerData.getId());
        dto.setEmailId(partnerData.getEmailId());
        dto.setLangCode(partnerData.getLangCode());
        return dto;
    }

    private Partner getValidPartner(String partnerId, boolean isToRetrieve) {
        Optional<Partner> partnerById = partnerRepository.findById(partnerId);
        if (partnerById.isEmpty()) {
            throw new PartnerServiceException(ErrorCode.PARTNER_DOES_NOT_EXIST_EXCEPTION.getErrorCode(),
                    ErrorCode.PARTNER_DOES_NOT_EXIST_EXCEPTION.getErrorMessage());
        }
        if (!isToRetrieve) {
            if (!partnerById.get().getIsActive()) {
                throw new PartnerServiceException(ErrorCode.PARTNER_NOT_ACTIVE_EXCEPTION.getErrorCode(),
                        ErrorCode.PARTNER_NOT_ACTIVE_EXCEPTION.getErrorMessage());
            }
            if (!partnerById.get().getRequiresPayment()) {
                throw new PartnerServiceException(ErrorCode.PARTNER_NOT_REQUIRED_PAYMENT_EXCEPTION.getErrorCode(),
                        ErrorCode.PARTNER_NOT_REQUIRED_PAYMENT_EXCEPTION.getErrorMessage());
            }
        }
        return partnerById.get();
    }
}