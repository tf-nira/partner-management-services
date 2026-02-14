package io.mosip.pms.payment.service;

import io.mosip.pms.common.dto.PageResponseDto;
import io.mosip.pms.common.dto.SearchDto;
import io.mosip.pms.common.entity.PartnerPaymentTransactions;
import io.mosip.pms.common.entity.PartnerPrn;
import io.mosip.pms.payment.request.dto.PrnRequest;
import io.mosip.pms.payment.request.dto.ValidatePrnRequest;
import io.mosip.pms.payment.response.dto.PrnResponse;
import io.mosip.pms.payment.response.dto.ValidatePrnResponse;
/**
 *
 * @author Karthik S
 *
 */
public interface PaymentService {

    public PrnResponse generatePrn(PrnRequest request);

    public ValidatePrnResponse validatePrn(ValidatePrnRequest request);

    public PageResponseDto<PartnerPaymentTransactions> searchPayment(SearchDto dto);

	public PageResponseDto<PartnerPrn> searchPartnerPrn(SearchDto request);
	
}
