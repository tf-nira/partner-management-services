package io.mosip.pms.common.entity;

import java.io.Serializable;
import java.util.Objects;
/**
 *
 * @author Karthik S
 *
 */
public class PartnerPrnId implements Serializable {

    private String partnerId;
    private String prn;

    public PartnerPrnId() {}

    public PartnerPrnId(String partnerId, String prn) {
        this.partnerId = partnerId;
        this.prn = prn;
    }

    // Must implement equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PartnerPrnId that = (PartnerPrnId) o;
        return Objects.equals(partnerId, that.partnerId) &&
                Objects.equals(prn, that.prn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(partnerId, prn);
    }
}