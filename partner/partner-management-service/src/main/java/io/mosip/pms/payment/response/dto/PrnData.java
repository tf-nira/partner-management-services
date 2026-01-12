package io.mosip.pms.payment.response.dto;

import lombok.Data;

@Data
public class PrnData {

    private String errorCode;
    private String errorDesc;
    private String expiryDate;
    private String prn;
    private String searchCode;
    private String amount;
    private String currency;
}
