package io.mosip.pms.payment.request.dto;

import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PrnRequest {

    public String service;

    public String nin;

    public String fullName;

    public String remarks;
    
    private BigDecimal amount;

    @NotBlank(message="value is empty or null")
    public String partnerId;

    private String serviceCode;

}
