package com.payline.payment.tsi.service;

import com.payline.payment.tsi.utils.i18n.I18nService;
import com.payline.pmapi.bean.paymentform.bean.PaymentFormLogo;
import com.payline.pmapi.bean.paymentform.request.PaymentFormConfigurationRequest;
import com.payline.pmapi.bean.paymentform.request.PaymentFormLogoRequest;
import com.payline.pmapi.bean.paymentform.response.configuration.PaymentFormConfigurationResponse;
import com.payline.pmapi.bean.paymentform.response.configuration.PaymentFormConfigurationResponseProvided;
import com.payline.pmapi.bean.paymentform.response.logo.PaymentFormLogoResponse;
import com.payline.pmapi.bean.paymentform.response.logo.PaymentFormLogoResponseFile;
import com.payline.pmapi.service.PaymentFormConfigurationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;

public class PaymentFormConfigurationServiceImpl implements PaymentFormConfigurationService {

    private static final String LOGO_CONTENT_TYPE = "image/jpeg";
    private static final int LOGO_HEIGHT = 92;
    private static final int LOGO_WIDTH = 390;

    @Override
    public PaymentFormConfigurationResponse getPaymentFormConfiguration( PaymentFormConfigurationRequest paymentFormConfigurationRequest ) {
        return PaymentFormConfigurationResponseProvided.PaymentFormConfigurationResponseBuilder.aPaymentFormConfigurationResponse()
                .withContextPaymentForm(new HashMap<>())
                .build();
    }

    @Override
    public PaymentFormLogoResponse getPaymentFormLogo( PaymentFormLogoRequest paymentFormLogoRequest ) {
        I18nService i18n = I18nService.getInstance();

        return PaymentFormLogoResponseFile.PaymentFormLogoResponseFileBuilder.aPaymentFormLogoResponseFile()
                .withContentType( LOGO_CONTENT_TYPE )
                .withHeight( LOGO_HEIGHT )
                .withWidth( LOGO_WIDTH )
                .withTitle( i18n.getMessage( "formConfiguration.logo.title", paymentFormLogoRequest.getLocale() ) )
                .withAlt( i18n.getMessage( "formConfiguration.logo.alt", paymentFormLogoRequest.getLocale() ) )
                .build();
    }

    @Override
    public PaymentFormLogo getLogo( Locale locale ) throws IOException {
        // Read logo file
        InputStream input = PaymentFormConfigurationServiceImpl.class.getClassLoader().getResourceAsStream( "ticketpremium-logo.jpg" );
        BufferedImage logo = ImageIO.read( input );

        // Recover byte array from image
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write( logo, "jpg", baos );

        return PaymentFormLogo.PaymentFormLogoBuilder.aPaymentFormLogo()
                .withFile( baos.toByteArray() )
                .withContentType( LOGO_CONTENT_TYPE )
                .build();
    }
}