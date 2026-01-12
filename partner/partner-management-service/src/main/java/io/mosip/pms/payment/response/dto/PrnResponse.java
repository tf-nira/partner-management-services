package io.mosip.pms.payment.response.dto;

import lombok.Data;

@Data
public class PrnResponse {

    private String id;
    private String version;
    private String responsetime;
    private PrnInnerResponse response;
    private Object errors;
}
