package com.payline.payment.tsi.request;

import com.payline.payment.tsi.exception.InvalidRequestException;
import com.payline.payment.tsi.request.mock.TsiRedirectionPaymentRequestMock;
import com.payline.pmapi.bean.payment.request.RedirectionPaymentRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Parameterized test class which purpose is to test each case in which the method
 * {@link TsiStatusCheckRequest.Builder#checkInputRequest(RedirectionPaymentRequest)} should throw an exception.
 */
@RunWith( Parameterized.class )
public class TsiStatusCheckRequestCheckInputTest {

    private TsiStatusCheckRequest.Builder builder;

    @Parameterized.Parameter //(0)
    public String testName; // Never used, but required by the test initialization
    @Parameterized.Parameter(1)
    public RedirectionPaymentRequest redirectionPaymentRequest;

    @Before
    public void setup(){
        this.builder = new TsiStatusCheckRequest.Builder();
    }

    /**
     * Creates the test data set: each element added to the list contains :
     * - a string to identify the test case
     * - a PaymentRequest object that matches a case in which the request is not valid
     * and the ckeck method should throw an exception.
     *
     * @return The test data set, as a collection of objects.
     */
    @Parameterized.Parameters( name = "{0}" )
    public static Collection<Object[]> data(){
        List<Object[]> dataList = new ArrayList<>();
        TsiRedirectionPaymentRequestMock mocker = new TsiRedirectionPaymentRequestMock();

        dataList.add( new Object[]{
                "Missing contract property: key id",
                mocker.reset().withKeyId( null ).mock()
        });

        dataList.add( new Object[]{
                "No tid passed through redirection context",
                mocker.reset().withRedirectionContext( null ).mock()
        });

        return dataList;
    }

    @Test(expected = InvalidRequestException.class)
    public void testBuilder_checkInputRequest_invalid() throws InvalidRequestException {
        // given: the parameterized RedirectionPaymentRequest
        // when: checking the request validity, then: an exception is thrown
        builder.checkInputRequest( redirectionPaymentRequest );
    }

}
