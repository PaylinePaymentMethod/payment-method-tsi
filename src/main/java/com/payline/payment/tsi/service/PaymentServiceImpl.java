package com.payline.payment.tsi.service;

import com.payline.payment.tsi.TsiConstants;
import com.payline.payment.tsi.error.ErrorCodesMap;
import com.payline.payment.tsi.exception.ExternalCommunicationException;
import com.payline.payment.tsi.exception.InvalidRequestException;
import com.payline.payment.tsi.request.TsiGoRequest;
import com.payline.payment.tsi.response.TsiGoResponse;
import com.payline.payment.tsi.utils.PaymentResponseUtil;
import com.payline.payment.tsi.utils.config.ConfigEnvironment;
import com.payline.payment.tsi.utils.config.ConfigProperties;
import com.payline.payment.tsi.utils.http.StringResponse;
import com.payline.pmapi.bean.payment.RequestContext;
import com.payline.pmapi.bean.payment.request.PaymentRequest;
import com.payline.pmapi.bean.payment.response.PaymentResponse;
import com.payline.pmapi.bean.payment.response.impl.PaymentResponseRedirect;
import com.payline.pmapi.service.PaymentService;
import com.payline.pmapi.logger.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

public class PaymentServiceImpl extends AbstractPaymentHttpService<PaymentRequest> implements PaymentService {

    private static final Logger logger = LogManager.getLogger( PaymentServiceImpl.class );

    private TsiGoRequest.Builder requestBuilder;
    private PaymentResponseUtil paymentResponseUtil;

    public PaymentServiceImpl() {
        super();
        this.requestBuilder = new TsiGoRequest.Builder();
        this.paymentResponseUtil = PaymentResponseUtil.getInstance();
    }

    @Override
    public PaymentResponse paymentRequest( PaymentRequest paymentRequest ) {
        return processRequest( paymentRequest );
    }

    @Override
    public StringResponse createSendRequest(PaymentRequest paymentRequest ) throws IOException, InvalidRequestException, GeneralSecurityException, URISyntaxException, ExternalCommunicationException {
        // Create Go request from Payline request
        TsiGoRequest tsiGoRequest = requestBuilder.fromPaymentRequest( paymentRequest );

        // Send Go request
        ConfigEnvironment env = Boolean.FALSE.equals( paymentRequest.getEnvironment().isSandbox() ) ? ConfigEnvironment.PROD : ConfigEnvironment.TEST;
        String scheme = ConfigProperties.get( "tsi.scheme", env );
        String host = ConfigProperties.get( "tsi.host", env );
        String path = ConfigProperties.get( "tsi.go.path", env );
        return getHttpClient().doPost( scheme, host, path, tsiGoRequest.buildBody() );
    }

    @Override
    public PaymentResponse processResponse(final StringResponse response, final String tid) throws IOException {
        // Parse response
        final TsiGoResponse tsiGoResponse = (new TsiGoResponse.Builder()).fromJson(response.getContent());

        // If status == 1, proceed with the redirection
        if( tsiGoResponse.getStatus() == 1 ){
            final String redirectUrl = tsiGoResponse.getUrl();
            final PaymentResponseRedirect.RedirectionRequest redirectionRequest = PaymentResponseRedirect.RedirectionRequest.RedirectionRequestBuilder.aRedirectionRequest()
                    .withUrl(new URL(redirectUrl))
                    .withRequestType(PaymentResponseRedirect.RedirectionRequest.RequestType.GET)
                    .build();

            final Map<String, String> requestContextData = new HashMap<>();
            requestContextData.put(TsiConstants.REQUEST_CONTEXT_KEY_TID, tsiGoResponse.getTid());
            final RequestContext requestContext = RequestContext.RequestContextBuilder.aRequestContext()
                    .withRequestData(requestContextData)
                    .build();

            return PaymentResponseRedirect.PaymentResponseRedirectBuilder.aPaymentResponseRedirect()
                    .withRedirectionRequest( redirectionRequest )
                    .withPartnerTransactionId( tsiGoResponse.getTid() )
                    .withRequestContext(requestContext)
                    .build();
        }
        else {
            logger.error( "TSI Go request returned an error: " + tsiGoResponse.getMessage() + "(" + Integer.toString( tsiGoResponse.getStatus() ) + ")" );
            return paymentResponseUtil.buildPaymentResponseFailure( tsiGoResponse.getMessage(), ErrorCodesMap.getFailureCause( tsiGoResponse.getStatus()), tid);
        }
    }
}
