package io.mosip.pms.common.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "partner_current_balance")
public class PartnerBalance implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "partner_id", nullable = false)
    private String partnerId;

    @Column(name = "balance", nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;

    @Column(name = "cr_by", nullable = false)
    private String crBy;

    @Column(name = "cr_dtimes", nullable = false)
    private LocalDateTime crDtimes;

    @Column(name = "upd_by")
    private String updBy;

    @Column(name = "upd_dtimes")
    private LocalDateTime updDtimes;

    // Getters and Setters

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getCrBy() {
        return crBy;
    }

    public void setCrBy(String crBy) {
        this.crBy = crBy;
    }

    public LocalDateTime getCrDtimes() {
        return crDtimes;
    }

    public void setCrDtimes(LocalDateTime crDtimes) {
        this.crDtimes = crDtimes;
    }

    public String getUpdBy() {
        return updBy;
    }

    public void setUpdBy(String updBy) {
        this.updBy = updBy;
    }

    public LocalDateTime getUpdDtimes() {
        return updDtimes;
    }

    public void setUpdDtimes(LocalDateTime updDtimes) {
        this.updDtimes = updDtimes;
    }
}
