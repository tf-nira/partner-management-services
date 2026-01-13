package io.mosip.pms.payment.response.dto;

import lombok.Data;

@Data
public class ErrorDTO {
    private String errorCode;
    private String errorMessage;
}
