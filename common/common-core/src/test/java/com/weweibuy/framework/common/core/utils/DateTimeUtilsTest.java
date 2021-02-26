package com.weweibuy.framework.common.core.utils;

import org.junit.Test;

import java.time.LocalDate;

public class DateTimeUtilsTest {


    @Test
    public void timestampMilliToLocalDate() throws Exception {
        LocalDate localDate = DateTimeUtils.timestampMilliToLocalDate(System.currentTimeMillis());
        System.err.println(localDate);

    }

}