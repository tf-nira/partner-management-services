package io.mosip.pms.common.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "partner_payment_transactions")
public class PartnerPaymentTransactions implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "transaction_id", nullable = false)
    private String transactionId;
    // PRN, auth transaction ID

    @Column(name = "partner_id", nullable = false)
    private String partnerId;

    @Column(name = "entry_type", nullable = false)
    private String entryType;
    // DEBIT, CREDIT

    @Column(name = "amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(name = "source_system", nullable = false)
    private String sourceSystem;
    // IDA, PMS

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "log_dtimes", nullable = false)
    private LocalDateTime logDtimes;

    @Column(name = "cr_by", nullable = false)
    private String crBy;

    @Column(name = "cr_dtimes", nullable = false)
    private LocalDateTime crDtimes;

    @Column(name = "upd_by")
    private String updBy;

    @Column(name = "upd_dtimes")
    private LocalDateTime updDtimes;

    // Getters and Setters

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getEntryType() {
        return entryType;
    }

    public void setEntryType(String entryType) {
        this.entryType = entryType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getSourceSystem() {
        return sourceSystem;
    }

    public void setSourceSystem(String sourceSystem) {
        this.sourceSystem = sourceSystem;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getLogDtimes() {
        return logDtimes;
    }

    public void setLogDtimes(LocalDateTime logDtimes) {
        this.logDtimes = logDtimes;
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
