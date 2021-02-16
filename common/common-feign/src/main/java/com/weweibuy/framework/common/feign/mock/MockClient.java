package com.weweibuy.framework.common.feign.mock;

import com.fasterxml.jackson.databind.JavaType;
import com.weweibuy.framework.common.core.exception.Exceptions;
import com.weweibuy.framework.common.core.utils.HttpRequestUtils;
import com.weweibuy.framework.common.core.utils.JackJsonUtils;
import feign.Client;
import feign.Request;
import feign.Response;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * feign mock
 *
 * @author durenhao
 * @date 2020/12/27 22:36
 **/
@DependsOn("jackJsonUtils")
public class MockClient implements Client, InitializingBean {

    private final Client delegate;

    private String configFile;

    private String mockDataDir;

    private JavaType configJavaType;

    public MockClient(Client delegate) {
        this.delegate = delegate;
        this.configJavaType = JackJsonUtils.javaType(List.class, MockConfig.class);
    }

    @Override
    public Response execute(Request request, Request.Options options) throws IOException {
        String url = request.url();
        Request.HttpMethod httpMethod = request.httpMethod();
        List<MockConfig> configList = JackJsonUtils.readValueWithMvc(new File(configFile), configJavaType);

        Optional<MockConfig> mockDataOpt = configList.stream()
                .filter(config -> StringUtils.equalsIgnoreCase(httpMethod.toString(), config.getMethod()))
                .filter(config -> !Objects.equals(config.enable, false))
                .filter(config -> StringUtils.equalsIgnoreCase(getHost(url), getHost(config.getHost())))
                .filter(config -> HttpRequestUtils.isEqualsOrMatchPath(config.getPath(), getPath(url)))
                .findFirst();
        MockConfig mockConfig = mockDataOpt.orElse(null);
        if (mockConfig != null) {
            // 读取mock数据
            String target = mockConfig.getTarget();
            String dataFile = mockDataDir + target;
            MockData mockData = JackJsonUtils.readValueWithMvc(new File(dataFile), MockData.class);
            Map<String, String> mcoKHeader = mockData.getHeader();
            Map<String, List<String>> header = Optional.ofNullable(mcoKHeader)
                    .map(h -> h.entrySet().stream()
                            .collect(Collectors.groupingBy(Map.Entry::getKey,
                                    Collectors.mapping(Map.Entry::getValue,
                                            Collectors.toList()))))
                    .orElse(new HashMap<>());

            if (header.get(HttpHeaders.CONTENT_TYPE) == null) {
                header.put(HttpHeaders.CONTENT_TYPE, Collections.singletonList(MediaType.APPLICATION_JSON_VALUE));
            }

            byte[] body = Optional.ofNullable(mockData.getBody())
                    .map(o -> JackJsonUtils.writeAsByteWithMvc(o))
                    .orElse("".getBytes());

            Integer status = Optional.ofNullable(mockData.getStatus())
                    .orElse(HttpStatus.OK.value());

            return Response.builder()
                    .request(request)
                    .status(status)
                    .headers((Map<String, Collection<String>>) (Object) header)
                    .body(body)
                    .build();
        }

        return delegate.execute(request, options);
    }


    public String getHost(String url) {
        try {
            return new URI(url).getAuthority();
        } catch (URISyntaxException e) {
            throw Exceptions.formatSystem(e, "请求地址:%s 错误", url);
        }
    }

    public String getPath(String url) {
        try {
            return new URI(url).getPath();
        } catch (URISyntaxException e) {
            throw Exceptions.formatSystem(e, "请求地址:%s 错误", url);
        }
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        String baseDir = System.getProperty("feign.mock.dir");
        if (StringUtils.isBlank(baseDir)) {
            File classPathFile = new File(MockClient.class.getResource("/").getPath());
            String classPath = classPathFile.getCanonicalPath();
            String substringAfterLast = StringUtils.substringBeforeLast(classPath, File.separator);

            String projectBaseDir = StringUtils.substringBeforeLast(substringAfterLast, File.separator);
            baseDir = projectBaseDir + "/src/test/resources/feign";
        }

        File file = new File(baseDir + "/mock.json");
        if (!file.exists()) {
            throw new IllegalArgumentException("请先在" + baseDir + " 目录下创建feign mock配置文件 mock.json");
        }
        configFile = file.getCanonicalPath();
        mockDataDir = baseDir + "/mock/";
    }

    @Data
    static class MockConfig {

        private String host;

        private String path;

        private String method;

        private String target;

        /**
         * 是否使用, 默认: true
         */
        private Boolean enable;

    }


    @Data
    static class MockData {

        private Integer status;

        private Map<String, String> header;

        private Object body;

    }
}
