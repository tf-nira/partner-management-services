package io.mosip.pms.payment.response.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
/**
 *
 * @author Jagadeesh
 *
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ValidatePrnInnerResponse {

    private double amountPaid;
    private String errorCode;
    private String errorDesc;
    private String taxPayerName;
    private String prn;
    private String statusCode;
    private String statusDesc;
    @JsonProperty("isValidPmsTaxHead")
    private boolean isValidPmsTaxHead;
}
