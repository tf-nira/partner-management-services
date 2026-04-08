package io.mosip.pms.common.repository;

import io.mosip.pms.common.entity.AuthTransaction;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 *
 * @author Karthik S
 *
 */
public  interface AuthTransactionRepository extends JpaRepository<AuthTransaction, String> {

}