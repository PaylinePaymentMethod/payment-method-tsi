package com.payline.payment.tsi.request;

import com.payline.payment.tsi.TsiConstants;
import com.payline.payment.tsi.exception.InvalidRequestException;
import com.payline.payment.tsi.request.mock.TsiRedirectionPaymentRequestMock;
import com.payline.pmapi.bean.payment.ContractConfiguration;
import com.payline.pmapi.bean.payment.ContractProperty;
import com.payline.pmapi.bean.payment.request.RedirectionPaymentRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TsiStatusCheckRequestCheckInputTest {

    private TsiStatusCheckRequest.Builder builder;

    @Before
    public void setup(){
        this.builder = new TsiStatusCheckRequest.Builder();
    }

    public void testBuilder_checkInputRequest_ok() throws InvalidRequestException {
        final Map<String, ContractProperty> contractProperties = new HashMap<>();
        contractProperties.put(TsiConstants.CONTRACT_KEY_VALUE, new ContractProperty("A"));
        contractProperties.put(TsiConstants.CONTRACT_KEY_ID, new ContractProperty("B"));
        builder.checkInputRequest(new ContractConfiguration("123", contractProperties));
    }

    @Test(expected = InvalidRequestException.class)
    public void testBuilder_checkInputRequest_null() throws InvalidRequestException {
        final Map<String, ContractProperty> contractProperties = null;
        builder.checkInputRequest(new ContractConfiguration("123", contractProperties));
    }

    @Test(expected = InvalidRequestException.class)
    public void testBuilder_checkInputRequest_invalid_value() throws InvalidRequestException {
        final Map<String, ContractProperty> contractProperties = new HashMap<>();
        contractProperties.put(TsiConstants.CONTRACT_KEY_VALUE, new ContractProperty("A"));
        builder.checkInputRequest(new ContractConfiguration("123", contractProperties));
    }

    @Test(expected = InvalidRequestException.class)
    public void testBuilder_checkInputRequest_invalid_key() throws InvalidRequestException {
        final Map<String, ContractProperty> contractProperties = new HashMap<>();
        contractProperties.put(TsiConstants.CONTRACT_KEY_ID, new ContractProperty("B"));
        builder.checkInputRequest(new ContractConfiguration("123", contractProperties));
    }
}
