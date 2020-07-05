package com.weweibuy.framework.common.core.utils;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class HttpRequestUtilsTest {

    @Test
    public void sanitizedPath() {
        String s = HttpRequestUtils.sanitizedPath("");
        Assert.assertEquals(s, "");

        String s1 = HttpRequestUtils.sanitizedPath("/");
        Assert.assertEquals(s1, "/");

        String s2 = HttpRequestUtils.sanitizedPath("//");
        Assert.assertEquals(s2, "/");

        String s3 = HttpRequestUtils.sanitizedPath("///hello");
        Assert.assertEquals(s3, "/hello");

        String s4 = HttpRequestUtils.sanitizedPath("///hello///world");
        Assert.assertEquals(s4, "/hello/world");
    }
}