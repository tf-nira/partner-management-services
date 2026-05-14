package io.mosip.pms.common.repository;

import io.mosip.pms.common.entity.PartnerBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 *
 * @author Karthik S
 *
 */
public  interface PartnerBalanceRepository extends JpaRepository<PartnerBalance, String> {

    @Query(value = "SELECT EXISTS(SELECT 1 FROM partner_current_balance WHERE partner_id = :partnerId)", nativeQuery = true)
    public Boolean isPartnerAlreadyExist(@Param("partnerId") String partnerId);

    @Query(value =
            "SELECT * FROM partner_current_balance " +
                    "WHERE balance < :threshold",
            nativeQuery = true)
    List<PartnerBalance> findPartnersBelowThreshold(@Param("threshold") Double threshold);
}