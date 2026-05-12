package io.mosip.pms.common.repository;

import io.mosip.pms.common.entity.PartnersTransaction;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 *
 * @author Karthik S
 *
 */
public  interface PartnersTransactionRepository extends JpaRepository<PartnersTransaction, String> {

}