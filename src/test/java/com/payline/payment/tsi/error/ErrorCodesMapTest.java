package com.payline.payment.tsi.error;

import com.payline.pmapi.bean.common.FailureCause;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test some randomly chosen error codes. The purpose is not to test all the codes but
 * to ensure that the {@link ErrorCodesMap} class works as expected.
 */
public class ErrorCodesMapTest {

    @Test
    public void testErrorCode_emptyMac(){
        // TSI error code for "EMPTY HMAC" is 44. Expected failure cause is INVALID_DATA.
        Assert.assertEquals( FailureCause.INVALID_DATA, ErrorCodesMap.getFailureCause( 44 ) );
    }

    @Test
    public void testErrorCode_tidAlreadyExists(){
        // TSI error code for "TID ALREADY EXISTS" is 17. Expected failure cause is REFUSED.
        Assert.assertEquals( FailureCause.REFUSED, ErrorCodesMap.getFailureCause( 17 ) );
    }

    @Test
    public void testErrorCode_databaseProblem(){
        // TSI error code for "DATABASE PROBLEM" is between 200 and 209. Expected failure cause is PAYMENT_PARTNER_ERROR.
        Assert.assertEquals( FailureCause.PAYMENT_PARTNER_ERROR, ErrorCodesMap.getFailureCause( 208 ) );
    }

    @Test
    public void testErrorCode_expiredTransaction(){
        // TSI error code for "TRANSACTION EXPIREE -1" is 314. Expected failure cause is CANCEL.
        Assert.assertEquals( FailureCause.CANCEL, ErrorCodesMap.getFailureCause( 314 ) );
    }

    @Test
    public void testErrorCode_unknown(){
        // TSI error code 666 is not supposed to exist. Expected failure cause is
        Assert.assertEquals( FailureCause.PARTNER_UNKNOWN_ERROR, ErrorCodesMap.getFailureCause( 666 ) );
    }

}
