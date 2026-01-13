package io.mosip.pms.payment.response.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ValidatePrnInnerResponse {

    private String amountPaid;
    private String errorCode;
    private String errorDesc;
    private String instrumentID;
    private String paymentExpiryDate;
    private String prn;
    private String statusCode;
    private String statusDesc;
    private String subServiceTypePaidFor;
    private String processFlowPaidFor;
}
