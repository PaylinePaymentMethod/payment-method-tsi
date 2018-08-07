package com.payline.payment.tsi.utils.config;

public enum ConfigEnvironment {

    TEST("test"),
    PROD("prod");

    private String prefix;

    ConfigEnvironment( String prefix ) {
        this.prefix = prefix;
    }

    public String getPrefix(){
        return this.prefix;
    }

}
