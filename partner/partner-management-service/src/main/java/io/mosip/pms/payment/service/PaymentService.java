package io.mosip.pms.payment.service;

import io.mosip.pms.payment.request.dto.PrnRequest;
import io.mosip.pms.payment.request.dto.ValidatePrnRequest;
import io.mosip.pms.payment.response.dto.PrnResponse;
import io.mosip.pms.payment.response.dto.ValidatePrnResponse;

public interface PaymentService {

    public PrnResponse generatePrn(PrnRequest request);

    public ValidatePrnResponse validatePrn(ValidatePrnRequest request);
}
