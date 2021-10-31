package com.weweibuy.framework.common.feign.support;

import feign.Response;
import feign.Util;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;

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
        int status = response.status();
        Response.Body body = response.body();
        String bodyStr = "";
        if (body != null && !(status == 204 || status == 205)) {
            byte[] bodyData = Util.toByteArray(body.asInputStream());
            bodyStr = new String(bodyData);
        }
        return bodyStr;
    }


    public static Response reeBufferResponse(Response response, byte[] bodyData) {
        Response.Body body = response.body();
        if (!body.isRepeatable()) {
            response = response.toBuilder().body(bodyData).build();
        }
        return response;
    }

}
