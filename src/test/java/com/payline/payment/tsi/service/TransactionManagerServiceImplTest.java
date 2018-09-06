package com.payline.payment.tsi.service;

import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

public class TransactionManagerServiceImplTest {

    @Test
    public void readAdditionalData() {
        final TransactionManagerServiceImpl tmsi = new TransactionManagerServiceImpl();
        final Map<String, String> addData = tmsi.readAdditionalData("{\"authId\":\"9289145\",\"tid\":\"9bc267fba6ccad33fc46a9b74411ad2b\",\"status\":\"OK\",\"ercode\":\"0\",\"message\":\"SUCCESSFUL TRANSACTION FOUND\",\"amount\":\"0,01\",\"multi\":\"f\",\"dtime\":\"2018-09-06 14:24:14\",\"country\":\"FRA\"}", "1.0");
        Assert.assertEquals("9bc267fba6ccad33fc46a9b74411ad2b", addData.get("tid"));
        Assert.assertEquals("9289145", addData.get("authId"));
    }

    @Test
    public void readAdditionalDataNull() {
        final TransactionManagerServiceImpl tmsi = new TransactionManagerServiceImpl();
        final Map<String, String> addData = tmsi.readAdditionalData(null, null);
        Assert.assertTrue(addData.isEmpty());
    }
}
