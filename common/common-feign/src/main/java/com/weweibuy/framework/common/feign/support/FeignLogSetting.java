package com.weweibuy.framework.common.feign.support;

import feign.Feign;
import feign.Request;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * feign 日志输出设置
 *
 * @author durenhao
 * @date 2021/10/30 18:23
 **/
@Data
@Builder
public class FeignLogSetting {

    private static final FeignLogSetting DEFAULT = FeignLogSetting.builder()
            .disableReqBody(false).disableRespBody(false).build();


    /**
     * 方法的key
     * {@link Feign#configKey(Class, java.lang.reflect.Method)}
     * eg: MyFeignClient#helloPost(CommonDataResponse,String);
     * <p>
     * methodKey 为单独匹配条件
     */
    private String methodKey;

    /**
     * 请求主机  host + path
     * url, method 为组合匹配条件
     */
    private String url;


    /**
     * 请求方法
     */
    private HttpMethod httpMethod;

    /**
     * 需要输出的请求头
     */
    private List<String> reqHeaderList;

    /**
     * 需要输出的响应头
     */
    private List<String> respHeaderList;

    /**
     * 不输出请求体
     */
    private Boolean disableReqBody;

    /**
     * 不输出响应体
     */
    private Boolean disableRespBody;


    public boolean match(Request request) {
        String configKey = request.requestTemplate().methodMetadata().configKey();
        if (configKey.equals(methodKey)) {
            return true;
        }
        if (sameMethod(request) && sameHostAndPath(request)) {
            return true;
        }
        return false;
    }


    public boolean sameHostAndPath(Request request) {
        // eg:
        String url = request.url();
        URI uri = null;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("非法的请求地址:" + url, e);
        }
        String s = uri.getAuthority() + uri.getPath();
        return s.equals(this.url);
    }


    public boolean sameMethod(Request request) {
        return request.httpMethod().toString().equals(httpMethod.toString());
    }

    public static FeignLogSetting getDEFAULT() {
        return DEFAULT;
    }

    public boolean rightSetting() {
        return StringUtils.isNotBlank(methodKey) ||
                (StringUtils.isNotBlank(url) && httpMethod != null);
    }
}
