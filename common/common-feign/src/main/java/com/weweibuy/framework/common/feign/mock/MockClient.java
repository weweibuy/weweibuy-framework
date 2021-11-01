package com.weweibuy.framework.common.feign.mock;

import com.fasterxml.jackson.databind.JavaType;
import com.weweibuy.framework.common.core.exception.Exceptions;
import com.weweibuy.framework.common.core.utils.HttpRequestUtils;
import com.weweibuy.framework.common.core.utils.JackJsonUtils;
import feign.Client;
import feign.Request;
import feign.RequestTemplate;
import feign.Response;
import feign.Target;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * feign mock
 *
 * @author durenhao
 * @date 2020/12/27 22:36
 **/
public class MockClient implements Client {

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

        String name = lbName(request);

        MockConfig mockConfig = matchMockConfig(name, request);

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
                    .orElseGet(() -> new HashMap<>());

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


    private MockConfig matchMockConfig(String lbName, Request request) {
        String url = request.url();
        Request.HttpMethod httpMethod = request.httpMethod();
        List<MockConfig> configList = JackJsonUtils.readValueWithMvc(new File(configFile), configJavaType);

        Optional<MockConfig> mockDataOpt = configList.stream()
                .filter(config -> !Objects.equals(config.enable, false))
                .filter(config -> StringUtils.equalsIgnoreCase(httpMethod.toString(), config.getMethod()))
                .filter(config -> isMatchMockConfig(lbName, config, url))
                .findFirst();
        return mockDataOpt.orElse(null);
    }


    private boolean isMatchMockConfig(String lbName, MockConfig config, String requestUrl) {
        if (StringUtils.isNotBlank(config.getName()) && config.getName().equals(lbName)
                && HttpRequestUtils.isEqualsOrMatchPath(Optional.ofNullable(config.getPath()).orElse(""), getPath(requestUrl))) {
            // LB 模式匹配
            return true;
        }

        if (StringUtils.isBlank(config.getName()) && StringUtils.isNotBlank(config.getHost())
                && StringUtils.equalsIgnoreCase(getHost(requestUrl), getHost(config.getHost()))
                && HttpRequestUtils.isEqualsOrMatchPath(Optional.ofNullable(config.getPath()).orElse(""), getPath(requestUrl))) {
            // url 模式匹配
            return true;
        }
        return false;
    }

    /**
     * LB 情况下, 对应feign的 name {@link FeignClient#name()}
     *
     * @param request
     * @return
     */
    private String lbName(Request request) {
        RequestTemplate target = request.requestTemplate().target(null);
        Target<?> feignTarget = target.feignTarget();
        FeignClient annotation = feignTarget.type().getAnnotation(FeignClient.class);
        String url = annotation.url();
        if (StringUtils.isBlank(url)) {
            return annotation.name();
        }
        return null;
    }



    public String getHost(String url) {
        try {
            return new URI(url).getRawAuthority();
        } catch (URISyntaxException e) {
            throw Exceptions.formatSystem(e, "请求地址:%s 错误", url);
        }
    }

    public String getPath(String url) {
        try {
            return new URI(url).getRawPath();
        } catch (URISyntaxException e) {
            throw Exceptions.formatSystem(e, "请求地址:%s 错误", url);
        }
    }


    public void init() throws IOException {
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

        /**
         * feign的名字 适用于LB的情况下, 填写FeignClient的Name
         * 与 host 互斥
         * {@link FeignClient#name()}
         */
        private String name;

        /**
         * 使用与非LB的情况下, 配置请求url; 可以不用填写 schema;
         * 与 name 互斥
         * <p>
         * eg: localhost:8080
         * eg: localhost:8080
         */
        private String host;

        /**
         * 请求路径
         * eg: /user/list
         * eg: /user/detail/**
         */
        private String path;


        /**
         * 请求方法
         * {@link  org.springframework.http.HttpMethod}
         */
        private String method;

        /**
         * mock 目标的Json 名称
         */
        private String target;

        /**
         * 是否使用, 默认: true
         */
        private Boolean enable;

    }


    @Data
    static class MockData {

        /**
         * mock 响应Http状态
         */
        private Integer status;

        /**
         * mock 响应Http头
         */
        private Map<String, String> header;

        /**
         * mock 响应 body体
         */
        private Object body;

    }
}
