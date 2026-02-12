package io.mosip.pms.common.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.*;
/**
 *
 * @author Karthik S
 *
 */
@Entity
@Table(name = "partner_prn")
@IdClass(PartnerPrnId.class)
public class PartnerPrn implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "partner_id", nullable = false)
    private String partnerId;

    @Id
    @Column(name = "prn", nullable = false)
    private String prn;

    @Column(name = "status", nullable = false)
    private String status;


    @Column(name = "amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(name = "service_code", nullable = false)
    private String serviceCode;
    // IDA, ID Repo

    @Column(name = "remarks", nullable = false)
    private String remarks;

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

    public String getPrn() {
        return prn;
    }

    public void setPrn(String prn) {
        this.prn = prn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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
