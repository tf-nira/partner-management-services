package io.mosip.pms.payment.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.mosip.kernel.core.logger.spi.Logger;
import io.mosip.pms.common.util.PMSLogger;
import io.mosip.pms.common.util.RestUtil;
import io.mosip.pms.partner.response.dto.CACertificateResponseDto;
import io.mosip.pms.partner.service.impl.PartnerServiceImpl;
import io.mosip.pms.payment.request.dto.PrnRequest;
import io.mosip.pms.payment.response.dto.PrnResponse;
import io.mosip.pms.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

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

        // Log full API response safely
        try {
            LOGGER.info("PRN API full response:\n{}",
                    mapper.writerWithDefaultPrettyPrinter()
                            .writeValueAsString(apiResponse));
        } catch (JsonProcessingException e) {
            LOGGER.error("Failed to serialize PRN API response for logging", e);
        }

        // Convert Map -> PrnResponse
        PrnResponse prnResponse = mapper.convertValue(apiResponse, PrnResponse.class);

        // Optional safety check
        if (prnResponse.getResponse() == null) {
            LOGGER.error("Failed to serialize PRN API response for logging");
        }

        return prnResponse;
    }

//public PrnResponse generatePrn(PrnRequest request) {
//
//    Map<String, Object> apiResponse = restUtil.postApi(
//            environment.getProperty("pmp.prn.generate.rest.uri"),
//            null,
//            "",
//            "",
//            MediaType.APPLICATION_JSON,
//            request,
//            Map.class
//    );
//    try {
//        LOGGER.info("PRN API full response:\n{}",
//                mapper.writerWithDefaultPrettyPrinter()
//                        .writeValueAsString(apiResponse));
//    } catch (JsonProcessingException e) {
//        LOGGER.error("Failed to serialize PRN API response for logging", e);
//    }
//
//
//    LOGGER.info("Calling PRN generation API");
//
//    Object responseObj = apiResponse.get("response");
//
//    return mapper.convertValue(responseObj, PrnResponse.class);
//}



    public PrnResponse validatePrn(PrnRequest request){
        return null;
    }

}
