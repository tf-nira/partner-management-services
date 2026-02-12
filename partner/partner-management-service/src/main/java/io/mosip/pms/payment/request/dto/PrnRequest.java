package io.mosip.pms.payment.request.dto;
import java.math.BigDecimal;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 *
 * @author Karthik S
 *
 */
@Data
public class PrnRequest {

    public String service;

    public String nin;

    public String fullName;

    public String remarks;

    @NotNull(message = "value is null")
    @DecimalMin(value = "0.0", inclusive = true, message = "value must be >= 0")
    private BigDecimal amount;

    @NotBlank(message="value is empty or null")
    public String partnerId;

    private String serviceCode;

}
