package com.weweibuy.framework.common.codec;

import org.junit.Test;

public class PasswordUtilsTest {
    @Test
    public void generate() throws Exception {
        String generate = PasswordGenerateUtils.generate(32);
        System.err.println(generate);
    }

}