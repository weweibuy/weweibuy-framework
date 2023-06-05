package com.weweibuy.framework.common.feign.support;

import com.weweibuy.framework.common.core.utils.HttpRequestUtils;
import com.weweibuy.framework.common.feign.config.HttpClientProperties;
import com.weweibuy.framework.common.feign.log.HttpClientLogger;
import com.weweibuy.framework.common.log.config.CommonLogProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpRequestWrapper;
import org.apache.http.protocol.HttpContext;
import org.springframework.http.HttpMethod;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author durenhao
 * @date 2022/10/13 21:23
 **/
@Slf4j
public class CustomHttpClientLogInterceptor implements HttpResponseInterceptor, HttpRequestInterceptor {

    private static final String REQ_TIME_KEY = "req_time_key";

    private static final String REQ_LOG_PROPERTIES_KEY = "req_log_properties_key";

    /**
     * 精确匹配  host --> path+method --> 配置
     */
    private Map<String, Map<String, HttpClientProperties.LogHttpProperties>> hostPathMethodExactPropertiesMap = new HashMap<>();

    /**
     * 路径匹配  host --> method --> path配置集合
     */
    private Map<String, Map<String, List<HttpClientProperties.HttpReqProperties>>> hostMethodPatternPropertiesMap = new HashMap<>();


    public CustomHttpClientLogInterceptor(List<HttpClientProperties.HttpReqProperties> httpReqPropertiesList) {
        initProperties(httpReqPropertiesList);
    }

    @Override
    public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
        try {
            URI uri = reqURI(request);
            String method = request.getRequestLine().getMethod();
            HttpClientProperties.LogHttpProperties logProperties = findLogProperties(uri, method);
            context.setAttribute(REQ_LOG_PROPERTIES_KEY, logProperties);
            context.setAttribute(REQ_TIME_KEY, System.currentTimeMillis());
            logHttpReq(request, uri, method, logProperties);
        } catch (Exception e) {
            log.error("Httpclient 输出请求日志异常: ", e);
        }
    }

    @Override
    public void process(HttpResponse response, HttpContext context) throws HttpException, IOException {
        Long time = (Long) context.getAttribute(REQ_TIME_KEY);
        long rt = System.currentTimeMillis() - time;
        HttpClientProperties.LogHttpProperties logProperties = (HttpClientProperties.LogHttpProperties) context.getAttribute(REQ_LOG_PROPERTIES_KEY);
        try {
            if (logProperties != null && Boolean.TRUE.equals(logProperties.getDisableResp())) {
                return;
            }
            logHttpResp(response, logProperties, rt);
        } catch (Exception e) {
            log.error("Httpclient 输出响应日志异常: ", e);
        }
    }


    private void logHttpReq(HttpRequest request, URI uri, String method, HttpClientProperties.LogHttpProperties logProperties) throws IOException {
        if (logProperties != null && Boolean.TRUE.equals(logProperties.getDisableReq())) {
            return;
        }

        HttpClientLogger.logReq(request, uri, method, logProperties);
    }

    private void logHttpResp(HttpResponse response, HttpClientProperties.LogHttpProperties logProperties, Long time) throws IOException {
        HttpClientLogger.logResp(response, logProperties, time);
    }


    private HttpClientProperties.LogHttpProperties findLogProperties(URI uri, String method) {
        String host = uri.getHost();
        if (StringUtils.isBlank(host)) {
            return null;
        }
        String path = Optional.ofNullable(uri.getPath())
                .filter(StringUtils::isNotBlank)
                .orElse("/");
        if (uri.getPort() > 0) {
            host = host + ":" + uri.getPort();
        }
        String hostPort = host;

        return Optional.ofNullable(hostPathMethodExactPropertiesMap.get(hostPort))
                .map(m -> m.get(key(path, method)))
                .orElseGet(() -> Optional.ofNullable(hostMethodPatternPropertiesMap.get(hostPort))
                        .map(m -> m.get(method))
                        .flatMap(l -> l.stream()
                                .filter(p -> HttpRequestUtils.isMatchPath(p.getPath(), path))
                                .map(HttpClientProperties.HttpReqProperties::getLog)
                                .findFirst()
                        ).orElse(null));
    }

    private void initProperties(List<HttpClientProperties.HttpReqProperties> httpReqPropertiesList) {
        if (CollectionUtils.isEmpty(httpReqPropertiesList)) {
            return;
        }

        for (HttpClientProperties.HttpReqProperties httpReqProperties : httpReqPropertiesList) {
            if (StringUtils.isBlank(httpReqProperties.getHost()) || httpReqProperties.getLog() == null) {
                continue;
            }
            if (StringUtils.isBlank(httpReqProperties.getPath())) {
                httpReqProperties.setPath("/**");
            }
            String path = httpReqProperties.getPath();
            if (path.indexOf("*") != -1) {
                addHostMethodPattern(httpReqProperties);
            } else {
                hostPathMethodExact(httpReqProperties);
            }

        }
    }

    private static URI reqURI(HttpRequest request) {
        if (request instanceof HttpRequestWrapper) {
            HttpRequest original = ((HttpRequestWrapper) request).getOriginal();
            if (original instanceof HttpRequestBase) {
                return ((HttpRequestBase) original).getURI();
            }
            HttpHost target = ((HttpRequestWrapper) request).getTarget();
            URI uri = ((HttpRequestWrapper) request).getURI();
            String url = target.toString() + uri.toString();
            return URI.create(url);
        }
        return URI.create(request.getRequestLine().getUri());
    }


    private void hostPathMethodExact(HttpClientProperties.HttpReqProperties httpReqProperties) {
        Set<HttpMethod> methods = httpReqProperties.getMethods();
        if (CollectionUtils.isEmpty(methods)) {
            return;
        }

        String host = httpReqProperties.getHost();
        Map<String, HttpClientProperties.LogHttpProperties> pathMethodMap = hostPathMethodExactPropertiesMap.get(host);
        if (pathMethodMap == null) {
            pathMethodMap = new HashMap<>();
            hostPathMethodExactPropertiesMap.put(host, pathMethodMap);
        }
        for (HttpMethod method : methods) {
            pathMethodMap.put(key(httpReqProperties.getPath(), method.toString()), httpReqProperties.getLog());
        }
    }

    private String key(String path, String httpMethod) {
        return path + httpMethod;
    }

    private void addHostMethodPattern(HttpClientProperties.HttpReqProperties httpReqProperties) {
        Set<HttpMethod> methods = httpReqProperties.getMethods();
        if (CollectionUtils.isEmpty(methods)) {
            return;
        }
        String host = httpReqProperties.getHost();
        Map<String, List<HttpClientProperties.HttpReqProperties>> methodPatternListMap = hostMethodPatternPropertiesMap.get(host);
        if (methodPatternListMap == null) {
            methodPatternListMap = new HashMap<>();
            hostMethodPatternPropertiesMap.put(host, methodPatternListMap);
        }
        for (HttpMethod method : methods) {
            String methodStr = method.toString();
            List<HttpClientProperties.HttpReqProperties> logPropertiesList = methodPatternListMap.get(methodStr);
            if (logPropertiesList == null) {
                logPropertiesList = new ArrayList<>();
                methodPatternListMap.put(methodStr, logPropertiesList);
            }
            logPropertiesList.add(httpReqProperties);
        }

    }


}
