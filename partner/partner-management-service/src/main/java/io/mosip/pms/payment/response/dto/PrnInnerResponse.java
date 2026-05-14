package io.mosip.pms.payment.response.dto;

import lombok.Data;
/**
 *
 * @author Karthik S
 *
 */
@Data
public class PrnInnerResponse {
    private String errorCode;
    private String errorDesc;
    private String expiryDate;
    private String prn;
    private double amount;
    private String currency;
    private Integer numberOfRecords;
}
