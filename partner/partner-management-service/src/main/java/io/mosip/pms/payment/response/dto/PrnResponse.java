package io.mosip.pms.payment.response.dto;

import io.mosip.pms.common.request.dto.ErrorResponse;
import lombok.Data;

import java.util.List;

@Data
public class PrnResponse {

    private String id;
    private String version;
    private String responsetime;
    private PrnInnerResponse response;
    private List<ErrorResponse> errors;
}
