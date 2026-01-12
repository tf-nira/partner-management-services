package io.mosip.pms.payment.response.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PrnData {

    private String errorCode;
    private String errorDesc;
    private String expiryDate;
    private String prn;
    private String searchCode;
    private BigDecimal amount;
    private String currency;
}
