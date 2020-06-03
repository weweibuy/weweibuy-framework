package com.weweibuy.framework.common.log.utils;

import com.weweibuy.framework.common.core.model.constant.CommonConstant;
import com.weweibuy.framework.common.core.utils.JackJsonUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Http请求工具
 *
 * @author durenhao
 * @date 2020/6/2 23:27
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpRequestUtils {

    /**
     * 是否为json 请求
     *
     * @param contentType
     * @return
     */
    public static boolean isJsonRequest(String contentType) {
        return StringUtils.isNotBlank(contentType) &&
                MediaType.valueOf(contentType).isCompatibleWith(MediaType.APPLICATION_JSON);
    }

    /**
     * 获取请求属性
     *
     * @param requestAttributes
     * @param name
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T getRequestAttribute(RequestAttributes requestAttributes, String name) {
        return (T) requestAttributes.getAttribute(name, RequestAttributes.SCOPE_REQUEST);
    }

    /**
     * 读取json请求流
     *
     * @param request
     * @return
     * @throws IOException
     */
    public static String readRequestBodyForJson(HttpServletRequest request) throws IOException {
        if (request instanceof ContentCachingRequestWrapper) {
            ContentCachingRequestWrapper requestWrapper = (ContentCachingRequestWrapper) request;
            String charEncoding = requestWrapper.getCharacterEncoding() != null ? requestWrapper.getCharacterEncoding() : CommonConstant.CharsetConstant.UTF8_STR;
            return new String(requestWrapper.getContentAsByteArray(), charEncoding);
        }
        return "";
    }


    public static String parameterMapToString(Map<String, String[]> parameterMap) {
        Map<String, String> stringStringHashMap = new HashMap<>(parameterMap.size());
        parameterMap.forEach((k, v) ->
                stringStringHashMap.put(k, Arrays.stream(v)
                        .filter(StringUtils::isNotBlank)
                        .collect(Collectors.joining(",", "", ""))));
        if (stringStringHashMap.isEmpty()) {
            return "";
        }
        return JackJsonUtils.write(stringStringHashMap);
    }


}
