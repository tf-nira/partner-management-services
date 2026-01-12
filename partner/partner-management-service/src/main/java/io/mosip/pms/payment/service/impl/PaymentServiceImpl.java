package io.mosip.pms.payment.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.mosip.kernel.core.logger.spi.Logger;
import io.mosip.pms.common.entity.Partner;
import io.mosip.pms.common.entity.PartnerPrn;
import io.mosip.pms.common.repository.PartnerPrnRepository;
import io.mosip.pms.common.util.PMSLogger;
import io.mosip.pms.common.util.RestUtil;
import io.mosip.pms.common.util.UserDetailUtil;
import io.mosip.pms.partner.response.dto.CACertificateResponseDto;
import io.mosip.pms.partner.service.impl.PartnerServiceImpl;
import io.mosip.pms.payment.request.dto.PrnRequest;
import io.mosip.pms.payment.request.dto.ValidatePrnRequest;
import io.mosip.pms.payment.response.dto.PrnResponse;
import io.mosip.pms.payment.response.dto.ValidatePrnResponse;
import io.mosip.pms.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {
    private static final Logger LOGGER = PMSLogger.getLogger(PaymentServiceImpl.class);

    @Autowired
    RestUtil restUtil;

    @Autowired
    private Environment environment;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    PartnerPrnRepository partnerPrnRepository;


    public PrnResponse generatePrn(PrnRequest request) {

        Map<String, Object> apiResponse = restUtil.postApi(
                environment.getProperty("pmp.prn.generate.rest.uri"),
                null,
                "",
                "",
                MediaType.APPLICATION_JSON,
                request,
                Map.class
        );
        try {
            LOGGER.info("PRN API full response:\n{}",
                    mapper.writerWithDefaultPrettyPrinter()
                            .writeValueAsString(apiResponse));
        } catch (JsonProcessingException e) {
            LOGGER.error("Failed to serialize PRN API response for logging", e);
        }
        PrnResponse prnResponse = mapper.convertValue(apiResponse, PrnResponse.class);
        if (prnResponse.getResponse() == null) {
            LOGGER.error("Failed to serialize PRN API response for logging");
        }
        else{
            if(prnResponse.getResponse().getData().getPrn() != null){
                PartnerPrn partnerPrn = mapPartnerPrnFromRequest(request,prnResponse);
                partnerPrnRepository.save(partnerPrn);
            }
        }
        return prnResponse;
    }

    public ValidatePrnResponse validatePrn(ValidatePrnRequest request){

        Map<String, Object> apiResponse = restUtil.postApi(environment.getProperty("pmp.prn.validate.rest.uri"), null,
                "", "", MediaType.APPLICATION_JSON, request, Map.class
        );

        ObjectMapper mapper = new ObjectMapper();

        ValidatePrnResponse validatePrnResponse = mapper.convertValue(apiResponse, ValidatePrnResponse.class);
        if(validatePrnResponse.getResponse()== null){
            LOGGER.error("Failed to serialize PRN API response for logging");
        }
        else{
            if(validatePrnResponse.getResponse().getStatusCode().equalsIgnoreCase("A")){
                PartnerPrn partnerPrn = getpartnerprndetails(request.getPrn());
                partnerPrn.setStatus("VALIDATED-NOT_PAID");
                partnerPrn.setRemarks("Amount not paid");
                partnerPrn.setUpdBy((getLoggedInUserId()));
                partnerPrn.setUpdDtimes(Timestamp.valueOf(LocalDateTime.now()).toLocalDateTime());
                partnerPrnRepository.save(partnerPrn);
            } else if (validatePrnResponse.getResponse().getStatusCode().equalsIgnoreCase("T")) {
                PartnerPrn partnerPrn = getpartnerprndetails(request.getPrn());
                partnerPrn.setStatus("VALIDATED-PAID");
                partnerPrn.setRemarks("Amount paid");
                partnerPrn.setUpdBy((getLoggedInUserId()));
                partnerPrn.setUpdDtimes(Timestamp.valueOf(LocalDateTime.now()).toLocalDateTime());
                partnerPrnRepository.save(partnerPrn);
            }
        }
        return validatePrnResponse;
    }

    private PartnerPrn mapPartnerPrnFromRequest(PrnRequest request, PrnResponse response){
        PartnerPrn partnerPrn = new PartnerPrn();
        partnerPrn.setPartnerId(request.getPartnerId());
        partnerPrn.setPrn(response.getResponse().getData().getPrn());
        partnerPrn.setStatus("GENERATED");
        partnerPrn.setAmount(response.getResponse().getData().getAmount());
        partnerPrn.setServiceCode(request.getServiceCode());
        partnerPrn.setRemarks("Prn Generated");
        partnerPrn.setCrBy((getLoggedInUserId()));
        partnerPrn.setCrDtimes(Timestamp.valueOf(LocalDateTime.now()).toLocalDateTime());
        return partnerPrn;
    }

    private PartnerPrn getpartnerprndetails(String prn){
        return partnerPrnRepository.findByPrn(prn);
    }

    private String getLoggedInUserId() {
        return UserDetailUtil.getLoggedInUserId();
    }


}
