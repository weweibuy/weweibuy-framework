package com.weiweibuy.framework.common.log.context;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * @author durenhao
 * @date 2020/3/1 10:08
 **/
@Data
@EqualsAndHashCode
public class RequestLogContext {

    private static final ThreadLocal<RequestLogContext> THREAD_LOCAL = new ThreadLocal<>();

    private String path;

    private String httpMethod;

    private Long timestamp;

    private Boolean logSensitization;

    private String queryString;

    private Set<String> sensitizationFieldSet;

    public static void put(HttpServletRequest request, Map<String, Set<String>> stringSetMap) {
        String path = request.getRequestURI();
        String method = request.getMethod();

        Set<String> stringSet = stringSetMap.get(path + "_" + method);

        RequestLogContext requestLogContext = new RequestLogContext();
        requestLogContext.setPath(path);
        requestLogContext.setTimestamp(System.currentTimeMillis());
        requestLogContext.setQueryString(request.getQueryString());
        requestLogContext.setHttpMethod(method);
        requestLogContext.setLogSensitization(CollectionUtils.isEmpty(stringSet) ? false : true);
        requestLogContext.setSensitizationFieldSet(CollectionUtils.isEmpty(stringSet) ? Collections.emptySet() : stringSet);
        THREAD_LOCAL.set(requestLogContext);

    }

    public static RequestLogContext getRequestContext() {
        return THREAD_LOCAL.get();
    }

    public static void clear() {
        THREAD_LOCAL.remove();
    }

}
