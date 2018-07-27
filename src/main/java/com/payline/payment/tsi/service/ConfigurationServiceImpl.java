package com.payline.payment.tsi.service;

import com.payline.pmapi.bean.configuration.AbstractParameter;
import com.payline.pmapi.bean.configuration.ContractParametersCheckRequest;
import com.payline.pmapi.bean.configuration.ReleaseInformation;
import com.payline.pmapi.service.ConfigurationService;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ConfigurationServiceImpl implements ConfigurationService {

    @Override
    public List<AbstractParameter> getParameters( Locale locale ){
        // TODO
        return null;
    }

    @Override
    public Map<String, String> check( ContractParametersCheckRequest contractParametersCheckRequest ){
        // TODO
        return null;
    }

    @Override
    public ReleaseInformation getReleaseInformation(){
        // TODO
        return null;
    }

    @Override
    public String getName( Locale locale ){
        // TODO
        return null;
    }
}
