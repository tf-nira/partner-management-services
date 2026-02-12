package io.mosip.pms.common.repository;
import io.mosip.pms.common.entity.PartnerPaymentTransactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
/**
 *
 * @author Karthik S
 *
 */
public  interface PartnerPaymentTransactionsRepository extends JpaRepository<PartnerPaymentTransactions, String> {

    @Query(value = "SELECT EXISTS(SELECT 1 FROM partner_payment_transactions WHERE transaction_id = :transactionId)", nativeQuery = true)
    public Boolean isTransactionAlreadyExist(@Param("transactionId") String transactionId);
}
