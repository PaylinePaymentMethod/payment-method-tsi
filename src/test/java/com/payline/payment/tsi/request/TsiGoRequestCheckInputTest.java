package com.payline.payment.tsi.request;

import com.payline.payment.tsi.exception.InvalidRequestException;
import com.payline.payment.tsi.request.mock.TsiPaymentRequestMock;
import com.payline.pmapi.bean.payment.request.PaymentRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Parameterized test class which purpose is to test each case in which the method
 * {@link TsiGoRequest.Builder#checkInputRequest(PaymentRequest)} should throw an exception.
 */
@RunWith( Parameterized.class )
public class TsiGoRequestCheckInputTest {

    private TsiGoRequest.Builder builder;

    @Parameter //(0)
    public String testName; // Never used, but required by the test initialization
    @Parameter(1)
    public PaymentRequest paymentRequest;

    @Before
    public void setup(){
        this.builder = new TsiGoRequest.Builder();
    }

    /**
     * Creates the test data set: each element added to the list contains :
     * - a string to identify the test case
     * - a PaymentRequest object that matches a case in which the request is not valid
     * and the ckeck method should throw an exception.
     *
     * @return The test data set, as a collection of objects.
     */
    @Parameters( name = "{0}" )
    public static Collection<Object[]> data(){
        List<Object[]> dataList = new ArrayList<>();
        TsiPaymentRequestMock mocker = new TsiPaymentRequestMock();

        dataList.add( new Object[]{
                "Mission contract property: merchant id",
                mocker.withMerchantId( null ).mock()
        });

        dataList.add( new Object[]{
                "Mission contract property: secret key",
                mocker.withKeyValue( null ).mock()
        });

        dataList.add( new Object[]{
                "Missing contract property: key id",
                mocker.reset().withKeyId( null ).mock()
        });

        dataList.add( new Object[]{
                "Missing order reference",
                mocker.reset().withOrderReference( null ).mock()
        });

        dataList.add( new Object[]{
                "No amount",
                mocker.reset().withAmount( null ).mock()
        });

        dataList.add( new Object[]{
                "No currency",
                mocker.reset().withCurrency( null ).mock()
        });

        dataList.add( new Object[]{
                "No redirection return URL",
                mocker.reset().withSuccessUrl( null ).mock()
        });

        dataList.add( new Object[]{
                "No redirection cancel URL",
                mocker.reset().withCancelUrl( null ).mock()
        });

        dataList.add( new Object[]{
                "No notification URL",
                mocker.reset().withNotificationUrl( null ).mock()
        });

        return dataList;
    }

    @Test(expected = InvalidRequestException.class)
    public void testBuilder_checkInputRequest_invalid() throws InvalidRequestException {
        // given: the parameterized PaymentRequest
        // when: checking the request validity, then: an exception is thrown
        builder.checkInputRequest( paymentRequest );
    }

}
