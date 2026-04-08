package io.mosip.pms.common.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.*;

import lombok.Data;

/**
 *
 * @author Karthik S
 *
 */

@Data
@Entity
@Table(name = "auth_transaction")
public class AuthTransaction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @Column(name = "request_dtimes", nullable = false)
    private LocalDateTime requestDtimes;

    @Column(name = "response_dtimes", nullable = false)
    private LocalDateTime responseDtimes;

    @Column(name = "request_trn_id", length = 64)
    private String requestTrnId;

    @Column(name = "auth_type_code", nullable = false, length = 128)
    private String authTypeCode;

    @Column(name = "status_code", nullable = false, length = 36)
    private String statusCode;

    @Column(name = "status_comment", length = 1024)
    private String statusComment;

    @Column(name = "requested_entity_id", length = 36)
    private String requestedEntityId;

    @Column(name = "requested_entity_name", length = 128)
    private String requestedEntityName;

    @Column(name = "amount")
    private double amount;

    @Column(name = "cr_by", nullable = false, length = 256)
    private String crBy;

    @Column(name = "cr_dtimes", nullable = false)
    private LocalDateTime crDtimes;

    @Column(name = "upd_by", length = 256)
    private String updBy;

    @Column(name = "upd_dtimes")
    private LocalDateTime updDtimes;
}