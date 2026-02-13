package io.mosip.pms.payment.request.dto;
import java.math.BigDecimal;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
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

    @Min(value = 0, message = "value must be >= 0")
    private double amount;

    @NotBlank(message="value is empty or null")
    public String partnerId;

    private String serviceCode;

}
