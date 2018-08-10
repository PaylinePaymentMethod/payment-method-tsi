package com.payline.payment.tsi.security;

/**
 * Lists the valid Mac algorithms as defined in the documentation.
 * @see <a href="https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#Mac">https://docs.oracle.com/.../StandardNames.html</a>
 */
public enum HmacAlgorithm {

    MD5("HmacMD5"),
    SHA1("HmacSHA1"),
    SHA224("HmacSHA224"),
    SHA256("HmacSHA256"),
    SHA384("HmacSHA384"),
    SHA512("HmacSHA512");

    private String name;

    HmacAlgorithm( String name ){
        this.name = name;
    }

    @Override
    public String toString(){
        return name;
    }
}
