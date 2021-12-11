package com.weweibuy.framework.common.core.utils;

import org.junit.Test;

public class DesensitizationUtilsTest {

    @Test
    public void phoneNo() throws Exception {
        String s = DesensitizationUtils.phoneNo("1380000");
        System.err.println(s);
    }

    @Test
    public void idNo() throws Exception {
        String s = DesensitizationUtils.idNo("62314587451421");
        System.err.println(s);
    }

    @Test
    public void name() throws Exception {
        String s = DesensitizationUtils.name("账好");
        System.err.println(s);
    }

    @Test
    public void bankCardNo() throws Exception {
        String s = DesensitizationUtils.bankCardNo("62151111114789611");
        System.err.println(s);
    }

}