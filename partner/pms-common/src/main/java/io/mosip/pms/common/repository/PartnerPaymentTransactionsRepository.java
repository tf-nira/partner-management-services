package io.mosip.pms.common.repository;
import io.mosip.pms.common.entity.PartnerPaymentTransactions;
import org.springframework.data.jpa.repository.JpaRepository;

public  interface PartnerPaymentTransactionsRepository extends JpaRepository<PartnerPaymentTransactions, String> {
}
