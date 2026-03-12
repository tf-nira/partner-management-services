package io.mosip.pms.notification.job;

import io.mosip.kernel.core.logger.spi.Logger;
import io.mosip.pms.common.constant.EventType;
import io.mosip.pms.common.entity.Partner;
import io.mosip.pms.common.entity.PartnerBalance;
import io.mosip.pms.common.entity.NotificationHistory;
import io.mosip.pms.common.exception.ApiAccessibleException;
import io.mosip.pms.common.repository.PartnerBalanceRepository;
import io.mosip.pms.common.repository.NotificationHistoryRepository;
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
import java.util.*;
import java.util.stream.Collectors;

@Component
public class PartnerLowBalanceNotificationJob {

    private Logger logger = PMSLogger.getLogger(PartnerLowBalanceNotificationJob.class);

    @Autowired
    private PartnerBalanceRepository partnerBalanceRepository;

    @Autowired
    private NotificationHistoryRepository notificationRepo;

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
    @Scheduled(initialDelayString = "${pms.balance.notification.init-delay}", fixedRateString = "${pms.balance.notification.fixed-rate}")
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

            NotificationHistory record = notificationRepo.findByPartnerIdAndResolvedFalse(partner.getPartnerId());

            if (record == null) {
                record = createNewNotification(partner.getPartnerId(), currentLevel);
                try {
                    notificationRepo.save(record);
                } catch(Exception dbEx){
                    logger.error("NotificationHistory new record  failed to save to local DB: {}", dbEx.getMessage());
                    throw new ApiAccessibleException("DB_ERROR", "NotificationHistory new record failed to persist");
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
                    logger.error("NotificationHistory  update record failed to save to local DB: {}", dbEx.getMessage());
                    throw new ApiAccessibleException("DB_ERROR", "NotificationHistory update record failed to persist");
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
                    logger.error("NotificationHistory  update record failed for configured days to save to local DB: {}", dbEx.getMessage());
                    throw new ApiAccessibleException("DB_ERROR", "NotificationHistory update record failed for configured days to persist");
                }
                notifications.add(prepareNotificationDto(partner, currentLevel));
            }
        }

        if (!notifications.isEmpty()) {
            List<NotificationDto> level1Notifications = notifications.stream()
                    .filter(n -> n.getLevel() == 1)
                    .collect(Collectors.toList());

            List<NotificationDto> level2Notifications = notifications.stream()
                    .filter(n -> n.getLevel() == 2)
                    .collect(Collectors.toList());

            if (!level1Notifications.isEmpty()) {
                notificationService.sendNotications(EventType.PARTNERS_LOW_BALANCE_LEVEL1, level1Notifications);
                logger.info("Level 1 notifications sent: {}", level1Notifications.size());
            }

            if (!level2Notifications.isEmpty()) {
                notificationService.sendNotications(EventType.PARTNERS_LOW_BALANCE_LEVEL2, level2Notifications);
                logger.info("Level 2 notifications sent: {}", level2Notifications.size());
            }
        }
        logger.info("Low Balance Notification Job Completed");
    }

    private void resolveRestoredBalances() {
        List<NotificationHistory> active = notificationRepo.findByResolvedFalse();

        for (NotificationHistory record : active) {
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

    private NotificationHistory createNewNotification(String partnerId, int level) {

        return NotificationHistory.builder()
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
        try {
            Partner partnerData = getValidPartner(partner.getPartnerId(), false);
            NotificationDto dto = new NotificationDto();
            dto.setPartnerId(partnerData.getId());
            dto.setPartnerName(partnerData.getName());
            dto.setEmailId(partnerData.getEmailId());
            dto.setLangCode(partnerData.getLangCode());
            dto.setLevel(level);
            return dto;
        } catch (PartnerServiceException ex) {
            logger.error("Skipping low-balance notification for partner {}: {}", partner.getPartnerId(), ex.getMessage());
            return null;
        }
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