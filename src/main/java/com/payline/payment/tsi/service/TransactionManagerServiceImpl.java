package com.payline.payment.tsi.service;

import com.google.gson.JsonSyntaxException;
import com.payline.payment.tsi.response.TsiStatusCheckResponse;
import com.payline.pmapi.service.TransactionManagerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class TransactionManagerServiceImpl implements TransactionManagerService {

    private static final Logger LOGGER = LogManager.getLogger( TransactionManagerServiceImpl.class );

    @Override
    public Map<String, String> readAdditionalData(final String data, final String version) {
        final Map<String, String> addData = new HashMap<>();
        if (null != data) {
            try {
                final TsiStatusCheckResponse statusCheck = new TsiStatusCheckResponse.Builder().fromJson(data);
                addData.put("authId", statusCheck.getAuthId());
            } catch(JsonSyntaxException e) {
                LOGGER.error("Additional data syntax incorrect [{}]", data, e);
            }
        }
        return addData;
    }
}
