package com.weweibuy.framework.common.core.utils;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

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

    @Test
    public void test01() {
        PathPatternParser pathPatternParser = WebMvcAutoConfiguration.pathPatternParser;
        PathContainer pathContainer = PathContainer.parsePath("/112/hello");
        PathPattern parse = pathPatternParser.parse("/*/hello");
        boolean matches = parse.matches(pathContainer);
        System.err.println(matches);

    }

}