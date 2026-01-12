package io.mosip.pms.common.repository;

import io.mosip.pms.common.entity.PartnerBalance;
import org.springframework.data.jpa.repository.JpaRepository;

public  interface PartnerBalanceRepository extends JpaRepository<PartnerBalance, String> {
}