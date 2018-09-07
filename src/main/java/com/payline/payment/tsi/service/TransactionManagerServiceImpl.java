package com.payline.payment.tsi.service;

import com.payline.payment.tsi.response.TsiStatusCheckResponse;
import com.payline.pmapi.service.TransactionManagerService;

import java.util.HashMap;
import java.util.Map;

public class TransactionManagerServiceImpl implements TransactionManagerService {

    @Override
    public Map<String, String> readAdditionalData(final String data, final String version) {
        final Map<String, String> addData = new HashMap<>();
        if (null != data) {
            final TsiStatusCheckResponse statusCheck = new TsiStatusCheckResponse.Builder().fromJson(data);
            addData.put("authId", statusCheck.getAuthId());
        }
        return addData;
    }
}
