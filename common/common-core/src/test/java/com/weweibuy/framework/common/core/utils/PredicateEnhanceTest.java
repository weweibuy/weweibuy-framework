package com.weweibuy.framework.common.core.utils;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

public class PredicateEnhanceTest {


    @Test
    public void test01() {
        PredicateEnhance.of("")
                .withPredicate(StringUtils::isBlank)
                .trueThenAction(() -> System.err.println("为空"));

    }


}