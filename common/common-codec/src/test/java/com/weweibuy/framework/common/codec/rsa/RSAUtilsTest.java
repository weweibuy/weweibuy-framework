package com.weweibuy.framework.common.codec.rsa;

import org.junit.Test;

public class RSAUtilsTest {


    @Test
    public void generateKeyToBase64Str() throws Exception {
        String[] strings = RSAUtils.generateKeyToBase64Str(2048);
        System.err.println(strings[0]);
        System.err.println(strings[1]);
    }



}