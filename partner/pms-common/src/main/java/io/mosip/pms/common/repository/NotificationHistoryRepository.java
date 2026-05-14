package io.mosip.pms.common.repository;


import io.mosip.pms.common.entity.NotificationHistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface NotificationHistoryRepository
        extends JpaRepository<NotificationHistory, UUID> {

    @Query(value =
            "SELECT * FROM notification_history " +
                    "WHERE partner_id = :partnerId " +
                    "AND resolved = false",
            nativeQuery = true)
    NotificationHistory findByPartnerIdAndResolvedFalse(@Param("partnerId") String partnerId);

    @Query(value =
            "SELECT * FROM notification_history " +
                    "WHERE resolved = false",
            nativeQuery = true)
    List<NotificationHistory> findByResolvedFalse();
}