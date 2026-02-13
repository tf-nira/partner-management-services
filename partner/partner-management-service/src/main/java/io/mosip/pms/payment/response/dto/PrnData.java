package io.mosip.pms.payment.response.dto;

import lombok.Data;

import java.math.BigDecimal;
/**
 *
 * @author Karthik S
 *
 */
@Data
public class PrnData {

    private String errorCode;
    private String errorDesc;
    private String expiryDate;
    private String prn;
    private String searchCode;
    private double amount;
    private String currency;
}
