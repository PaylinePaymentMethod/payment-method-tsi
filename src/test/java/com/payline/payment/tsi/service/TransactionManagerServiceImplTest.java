package com.payline.payment.tsi.service;

import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

public class TransactionManagerServiceImplTest {

    @Test
    public void readAdditionalData() {
        final TransactionManagerServiceImpl tmsi = new TransactionManagerServiceImpl();
        final Map<String, String> addData = tmsi.readAdditionalData("authId: 9289086, tid: 239c72ad88693a1c395023d337830a24, multi: f, dtime: 2018-09-03 15:14:57, country: FRA", "1.0");
        Assert.assertEquals("239c72ad88693a1c395023d337830a24", addData.get("tid"));
        Assert.assertEquals("9289086", addData.get("authId"));
    }
}
