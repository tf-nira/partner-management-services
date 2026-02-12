package io.mosip.pms.common.repository;
import io.mosip.pms.common.entity.PartnerPrn;
import io.mosip.pms.common.entity.PartnerPrnId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
/**
 *
 * @author Karthik S
 *
 */

public  interface PartnerPrnRepository extends JpaRepository<PartnerPrn, PartnerPrnId> {

    @Query(value = "SELECT * FROM partner_prn ppr WHERE ppr.prn = ?1 AND ppr.partner_id = ?2", nativeQuery = true)
    public PartnerPrn findByPrn(String prn);

}
