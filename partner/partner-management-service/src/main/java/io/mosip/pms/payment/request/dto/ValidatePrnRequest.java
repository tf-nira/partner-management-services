package io.mosip.pms.payment.request.dto;
import java.math.BigDecimal;
import javax.validation.constraints.NotBlank;
import lombok.Data;
/**
 *
 * @author Jagadeesh
 *
 */
@Data
public class ValidatePrnRequest {

	@NotBlank(message="value is empty or null")
    private String prn;

	@NotBlank(message="value is empty or null")
    private String partnerId;

    private String serviceCode;

    private BigDecimal amount;
    
    public String getPrn() {
        return prn;
    }

    public void setPrn(String prn) {
        this.prn = prn;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
