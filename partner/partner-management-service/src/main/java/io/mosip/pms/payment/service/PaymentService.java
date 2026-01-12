package io.mosip.pms.payment.service;

import io.mosip.pms.payment.request.dto.PrnRequest;
import io.mosip.pms.payment.response.dto.PrnResponse;

public interface PaymentService {

    public PrnResponse generatePrn(PrnRequest request);

    public PrnResponse validatePrn(PrnRequest request);
}
