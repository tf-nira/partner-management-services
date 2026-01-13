package io.mosip.pms.common.repository;
import io.mosip.pms.common.entity.PartnerPrn;
import io.mosip.pms.common.entity.PartnerPrnId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public  interface PartnerPrnRepository extends JpaRepository<PartnerPrn, PartnerPrnId> {

    @Query(value = "select * from partner_prn ppr where ppr.prn=?", nativeQuery = true)
    public PartnerPrn findByPrn(String prn);

}
