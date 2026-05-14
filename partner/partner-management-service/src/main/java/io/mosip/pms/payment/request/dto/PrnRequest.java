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

    @NotBlank(message="value is empty or null")
    public String partnerGroup;

    @NotBlank(message="value is empty or null")
    public String partnerType;

    public String partnerName;

    @NotBlank(message="value is empty or null")
    public String partnerId;

    @NotNull(message="value is empty or null")
    public Integer numberOfRecords;

}
