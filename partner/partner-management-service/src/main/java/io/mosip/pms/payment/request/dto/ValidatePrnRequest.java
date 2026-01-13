package io.mosip.pms.payment.request.dto;

import java.math.BigDecimal;
import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ValidatePrnRequest {

	@NotBlank(message="value is empty or null")
    private String prn;

	@NotBlank(message="value is empty or null")
    private String partnerId;

    private String serviceCode;

    private BigDecimal amount;
}
