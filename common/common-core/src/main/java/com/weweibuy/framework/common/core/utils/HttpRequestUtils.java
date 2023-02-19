package com.weweibuy.framework.common.core.utils;

import com.weweibuy.framework.common.core.exception.Exceptions;
import com.weweibuy.framework.common.core.model.ResponseCodeAndMsg;
import com.weweibuy.framework.common.core.model.constant.CommonConstant;
import com.weweibuy.framework.common.core.model.dto.CommonCodeResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.PathContainer;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.pattern.PathPatternParser;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

    private static final PathPatternParser PATH_MATCHER = WebMvcAutoConfiguration.pathPatternParser;

    private static final Pattern QUERY_PATTERN = Pattern.compile("([^&=]+)(=?)([^&]+)?");

    private static final Pattern CAN_LOG_PATTERN = Pattern.compile("text|json|xml|html|plain|form-urlencoded");

    public static final String BOUNDARY_BODY = "Binary data";


    private static final Map<Integer, HttpStatus> HTTP_STATUS_MAP = Arrays.stream(HttpStatus.values())
            .collect(Collectors.toMap(HttpStatus::value, Function.identity(), (o, n) -> n));

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
        int contentLength = request.getContentLength();
        return contentLength != 0;
    }

    /**
     * 路径是否匹配
     *
     * @param pattern
     * @param path
     * @return
     */
    public static boolean isMatchPath(String pattern, String path) {
        return PATH_MATCHER.parse(pattern)
                .matches(PathContainer.parsePath(path));
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
        return JackJsonUtils.readValueWithMvc(msgJsonStr, CommonCodeResponse.class);
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


    /**
     * URL 解码
     *
     * @param str
     * @return
     */
    public static String urlDecode(String str) {
        try {
            return URLDecoder.decode(str, CommonConstant.CharsetConstant.UTF8_STR);
        } catch (UnsupportedEncodingException e) {
            throw Exceptions.uncheckedIO(e);
        }
    }


    public static String readRequestBodyForJson(HttpServletRequest request) {
        if (request instanceof ContentCachingRequestWrapper) {
            return readFromRequestWrapper((ContentCachingRequestWrapper) request);
        } else {
            return readFromRequest(request);
        }
    }


    public static URI uri(String url) {
        try {
            return new URI(url);
        } catch (URISyntaxException e) {
            throw Exceptions.business("错误的url: " + url);
        }
    }

    public static Optional<HttpStatus> httpHttpStatus(Integer code) {
        return Optional.ofNullable(HTTP_STATUS_MAP.get(code));
    }

    public static Map<String, String> parseQueryParamsToMap(URI uri) {
        return parseQueryParamsToMap(uriQuery(uri));
    }

    public static Map<String, String> parseQueryParamsToMap(String uri) {
        return parseQueryParams(uri).toSingleValueMap();
    }

    public static MultiValueMap<String, String> parseQueryParams(URI uri) {
        return parseQueryParams(uriQuery(uri));
    }

    public static String uriQuery(String uri) {
        return StringUtils.substringAfter(uri, "?");
    }

    public static String uriQuery(URI uri) {
        return uri.getRawQuery();
    }


    public static MultiValueMap<String, String> parseQueryParams(String uri) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        String query = uriQuery(uri);
        if (StringUtils.isBlank(query)) {
            return queryParams;
        }
        if (query != null) {
            Matcher matcher = QUERY_PATTERN.matcher(query);
            while (matcher.find()) {
                String name = UriUtils.decode(matcher.group(1), StandardCharsets.UTF_8);
                String eq = matcher.group(2);
                String value = matcher.group(3);
                if (value != null) {
                    value = UriUtils.decode(value, StandardCharsets.UTF_8);
                } else {
                    value = (org.springframework.util.StringUtils.hasLength(eq) ? "" : null);
                }
                queryParams.add(name, value);
            }
        }
        return queryParams;
    }


    public static boolean notBoundaryBody(String contentType) {
        Matcher matcher = CAN_LOG_PATTERN.matcher(contentType);
        return matcher.find();
    }



}
