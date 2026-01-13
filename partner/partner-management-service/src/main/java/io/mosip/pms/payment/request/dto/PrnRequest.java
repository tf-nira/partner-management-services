package io.mosip.pms.payment.request.dto;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PrnRequest {

    public String service;

    public String nin;

    public String fullName;

    @NotBlank(message="value is empty or null")
    public String partnerId;

    @NotBlank(message="value is empty or null")
    String serviceCode;

}
