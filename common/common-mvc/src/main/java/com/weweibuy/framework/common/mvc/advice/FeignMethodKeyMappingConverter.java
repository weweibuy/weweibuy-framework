package com.weweibuy.framework.common.mvc.advice;

import com.weweibuy.framework.common.core.model.dto.CommonCodeResponse;
import feign.Feign;
import org.springframework.http.ResponseEntity;

/**
 * feign
 *
 * @author durenhao
 * @date 2020/7/6 17:32
 **/
public interface FeignMethodKeyMappingConverter {

    /**
     * 报文转化
     *
     * @param httpStatus
     * @param content    feign响应报文
     * @return
     */
    ResponseEntity<CommonCodeResponse> convertResponse(Integer httpStatus, String content);


    /**
     * feign的 methodKey
     * {@link Feign#configKey(java.lang.Class, java.lang.reflect.Method)}
     *
     * @param methodKey
     * @return
     */
    boolean match(String methodKey);

}
