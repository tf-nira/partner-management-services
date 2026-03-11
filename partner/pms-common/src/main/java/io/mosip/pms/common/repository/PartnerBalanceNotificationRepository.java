package io.mosip.pms.common.repository;

import io.mosip.pms.common.entity.PartnerBalanceNotification;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface PartnerBalanceNotificationRepository
        extends JpaRepository<PartnerBalanceNotification, UUID> {

    @Query(value =
            "SELECT * FROM partner_balance_notification " +
                    "WHERE partner_id = :partnerId " +
                    "AND resolved = false",
            nativeQuery = true)
    PartnerBalanceNotification findByPartnerIdAndResolvedFalse(@Param("partnerId") String partnerId);

    @Query(value =
            "SELECT * FROM partner_balance_notification " +
                    "WHERE resolved = false",
            nativeQuery = true)
    List<PartnerBalanceNotification> findByResolvedFalse();
}