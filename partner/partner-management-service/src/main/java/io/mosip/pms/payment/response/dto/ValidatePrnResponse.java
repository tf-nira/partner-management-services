package io.mosip.pms.payment.response.dto;

import lombok.Data;

@Data
public class ValidatePrnResponse {

    private String id;
    private String version;
    private String responsetime;
    private ValidatePrnInnerResponse response;
    private Object errors;
}
