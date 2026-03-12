package io.mosip.pms.common.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "notification_history")
public class NotificationHistory {

    @Id
    private UUID id;

    @Column(name = "partner_id")
    private String partnerId;

    @Column(name = "notification_level")
    private Integer notificationLevel;

    @Column(name = "first_notified_date")
    private LocalDateTime firstNotifiedDate;

    @Column(name = "last_notified_date")
    private LocalDateTime lastNotifiedDate;

    @Column(name = "notification_count")
    private Integer notificationCount;

    @Column(name = "resolved")
    private Boolean resolved;

    @Column(name = "cr_by")
    private String crBy;

    @Column(name = "cr_dtimes")
    private LocalDateTime crDtimes;

    @Column(name = "upd_by")
    private String updBy;

    @Column(name = "upd_dtimes")
    private LocalDateTime updDtimes;
}