package com.payline.payment.tsi.error;

import com.payline.pmapi.bean.common.FailureCause;

import java.util.HashMap;
import java.util.Map;

/**
 * A mapping class between TSI error code (integers, described in their API documentation)
 * and the Payline failure causes.
 */
public class ErrorCodesMap {

    private static Map<Integer, FailureCause> map;

    /**
     * Initializes the static map.
     */
    private static void initMap(){
        map = new HashMap<>();
        map.put( 44, FailureCause.INVALID_DATA );
        for( int i = 2; i <= 16; i++ ){
            map.put( i, FailureCause.INVALID_DATA );
        }
        map.put( 17, FailureCause.REFUSED );
        map.put( 101, FailureCause.PAYMENT_PARTNER_ERROR );
        map.put( 43, FailureCause.PAYMENT_PARTNER_ERROR );
        map.put( 111, FailureCause.INVALID_DATA );
        map.put( 112, FailureCause.INVALID_DATA );
        for( int i = 200; i <= 209; i++ ){
            map.put( i, FailureCause.PAYMENT_PARTNER_ERROR );
        }
        for( int i = 300; i <= 304; i++ ){
            map.put( i, FailureCause.REFUSED );
        }
        for( int i = 308; i <= 313; i++ ){
            map.put( i, FailureCause.REFUSED );
        }
        map.put( 314, FailureCause.CANCEL );
        map.put( 315, FailureCause.REFUSED );
        map.put( 316, FailureCause.REFUSED );
    }

    /**
     * Get the Payline failure cause corresponding to the given TSI error code.
     *
     * @param tsiErrorCode The TSI error code
     * @return The Payline failure cause
     */
    public static FailureCause getFailureCause( int tsiErrorCode ){
        if( map == null ){
            initMap();
        }
        FailureCause failureCause = map.get( tsiErrorCode );
        if( failureCause == null ){
            failureCause = FailureCause.PARTNER_UNKNOWN_ERROR;
        }
        return failureCause;
    }

}
