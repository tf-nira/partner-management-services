package io.mosip.pms.payment.response.dto;

import lombok.Data;

@Data
public class PrnInnerResponse {

    private String message;
    private String code;
    private PrnData data;
}
