package io.mosip.pms.payment.service;

import io.mosip.kernel.core.websub.model.EventModel;
import io.mosip.pms.common.dto.FilterValueDto;
import io.mosip.pms.common.dto.PageResponseDto;
import io.mosip.pms.common.dto.SearchDto;
import io.mosip.pms.common.entity.PartnerBalance;
import io.mosip.pms.common.entity.PartnerPaymentTransactions;
import io.mosip.pms.common.entity.PartnerPrn;
import io.mosip.pms.common.entity.PartnersTransaction;
import io.mosip.pms.device.response.dto.FilterResponseCodeDto;
import io.mosip.pms.payment.request.dto.PrnRequest;
import io.mosip.pms.payment.request.dto.ValidatePrnRequest;
import io.mosip.pms.payment.response.dto.PrnResponse;
import io.mosip.pms.payment.response.dto.ValidatePrnResponse;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author Karthik S, Jagadeesh
 *
 */
public interface PaymentService {

    public PrnResponse generatePrn(PrnRequest request);

    public ValidatePrnResponse validatePrn(ValidatePrnRequest request);

    public PageResponseDto<PartnerPaymentTransactions> searchPayment(SearchDto dto);

	  public PageResponseDto<PartnerPrn> searchPartnerPrn(SearchDto dto);

    public PageResponseDto<PartnerBalance> searchPartnerBalance(SearchDto dto);

    public PageResponseDto<PartnersTransaction> searchPartnerTransaction(SearchDto dto);
	
    public void prnStatusUpdateIda(EventModel eventModel);

    public void partnersBalanceUpdateIda(EventModel eventModel);

    public void insertPartnersAuthTransaction(EventModel eventModel);

    public FilterResponseCodeDto filterValuesPrn(FilterValueDto filterValueDto);

    public ResponseEntity<byte[]> exportPrn(SearchDto dto);

    public ResponseEntity<byte[]> exportTransaction(SearchDto dto);
}
