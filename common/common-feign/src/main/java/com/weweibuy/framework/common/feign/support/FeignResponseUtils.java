package com.weweibuy.framework.common.feign.support;

import com.weweibuy.framework.common.core.model.constant.CommonConstant;
import feign.Response;
import feign.Util;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * feign 响应工具
 *
 * @author durenhao
 * @date 2021/10/30 22:44
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FeignResponseUtils {

    /**
     * 响应体
     *
     * @param response
     * @return
     * @throws IOException
     */
    public static String responseBody(Response response) throws IOException {
        return responseBody(response, CommonConstant.CharsetConstant.UT8);
    }

    /**
     * 响应体
     *
     * @param response
     * @return
     * @throws IOException
     */
    public static String responseBody(Response response, Charset charset) throws IOException {
        int status = response.status();
        Response.Body body = response.body();
        String bodyStr = "";
        if (body != null && !(status == 204 || status == 205)) {
            byte[] bodyData = Util.toByteArray(body.asInputStream());
            bodyStr = new String(bodyData, charset);
        }
        return bodyStr;
    }


    public static Response rebufferResponse(Response response, byte[] bodyData) {
        Response.Body body = response.body();
        if (!body.isRepeatable()) {
            response = response.toBuilder().body(bodyData).build();
        }
        return response;
    }

    /**
     * 获取响应体, 并 reBuffer 响应体
     *
     * @param response
     * @param charset
     * @return
     * @throws IOException
     */
    public static Pair<Response, String> bodyAndReBufferResponse(Response response, Charset charset) throws IOException {
        int status = response.status();
        String bodyStr = "";
        Response.Body body = response.body();
        if (body != null && !(status == 204 || status == 205)) {
            byte[] bodyData = Util.toByteArray(body.asInputStream());
            bodyStr = new String(bodyData, charset);
            if (!body.isRepeatable()) {
                response = response.toBuilder().body(bodyData).build();
            }
        }
        return Pair.of(response, bodyStr);
    }

}
