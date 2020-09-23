package com.weweibuy.framework.compensate.model;

import com.weweibuy.framework.common.core.exception.BusinessException;
import com.weweibuy.framework.common.core.exception.Exceptions;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.junit.Test;

public class CompensateResultTest {


    @Test
    public void testEx() {
        BusinessException businessException = Exceptions.badRequestParam();
        String message = ExceptionUtils.getMessage(businessException);
        System.err.println(message);
    }

}