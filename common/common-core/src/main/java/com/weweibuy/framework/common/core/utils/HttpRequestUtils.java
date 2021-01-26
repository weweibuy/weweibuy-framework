package com.weweibuy.framework.common.core.utils;

import com.weweibuy.framework.common.core.model.ResponseCodeAndMsg;
import com.weweibuy.framework.common.core.model.constant.CommonConstant;
import com.weweibuy.framework.common.core.model.dto.CommonCodeResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
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

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

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
    public static String readRequestBodyForJson(HttpServletRequest request, boolean useWrapper) {
        if (request instanceof ContentCachingRequestWrapper && useWrapper) {
            return readFromRequestWrapper((ContentCachingRequestWrapper) request);
        } else if (!useWrapper) {
            return readFromRequest(request);
        }
        return StringUtils.EMPTY;
    }

    public static String readFromRequestWrapper(ContentCachingRequestWrapper requestWrapper) {
        String charEncoding = requestWrapper.getCharacterEncoding() != null ? requestWrapper.getCharacterEncoding() : CommonConstant.CharsetConstant.UTF8_STR;
        try {
            return new String(requestWrapper.getContentAsByteArray(), charEncoding);
        } catch (UnsupportedEncodingException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static String parameterMapToString(Map<String, String[]> parameterMap) {
        return parameterMap.entrySet().stream()
                .map(e ->
                        e.getKey() + CommonConstant.ASSIGNOR_STR +
                                Arrays.stream(e.getValue())
                                        .collect(Collectors.joining(CommonConstant.COMMA_STR, StringUtils.EMPTY, StringUtils.EMPTY)))
                .collect(Collectors.joining(CommonConstant.CONNECTOR_STR, StringUtils.EMPTY, StringUtils.EMPTY));
    }


    public static String readFromRequest(HttpServletRequest request) {
        String str = null;
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = request.getReader();
            while ((str = reader.readLine()) != null) {
                stringBuilder.append(str).append(LINE_SEPARATOR);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        if (stringBuilder.length() > 0) {
            return stringBuilder.substring(0, stringBuilder.length() - LINE_SEPARATOR.length());
        }
        return StringUtils.EMPTY;
    }

    public static boolean isIncludePayload(HttpServletRequest request) {
        return request.getContentLength() > 0;
    }

    /**
     * 路径是否匹配
     *
     * @param pattern
     * @param path
     * @return
     */
    public static boolean isMatchPath(String pattern, String path) {
        return ANT_PATH_MATCHER.match(pattern, path);
    }

    /**
     * 是否全相同(没有* 通配), 或匹配
     *
     * @param pattern
     * @param path
     * @return
     */
    public static boolean isEqualsOrMatchPath(String pattern, String path) {
        return PredicateEnhance.of(pattern)
                .withPredicate(p -> p.indexOf('*') != -1)
                .map(p -> StringUtils.equals(p, path),
                        p -> isMatchPath(p, path));
    }

    /**
     * 转为 Json 格式的Code msg 为 ResponseCodeAndMsg
     *
     * @param msgJsonStr
     * @return
     */
    public static ResponseCodeAndMsg convertJsonStrToCodeAndMsg(String msgJsonStr) {
        return JackJsonUtils.readValue(msgJsonStr, CommonCodeResponse.class);
    }


    /**
     * 出去多余的 /
     *
     * @param path
     * @return
     */
    public static String sanitizedPath(final String path) {
        String sanitized = path;
        while (true) {
            int index = sanitized.indexOf("//");
            if (index < 0) {
                break;
            } else {
                sanitized = sanitized.substring(0, index) + sanitized.substring(index + 1);
            }
        }
        return sanitized;
    }


}
