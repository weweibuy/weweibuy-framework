package com.weweibuy.framework.common.mvc.support;

import com.weweibuy.framework.common.core.exception.MethodKeyFeignException;
import com.weweibuy.framework.common.core.model.ResponseCodeAndMsg;
import com.weweibuy.framework.common.core.model.dto.CommonCodeResponse;
import com.weweibuy.framework.common.core.model.eum.CommonErrorCodeEum;
import com.weweibuy.framework.common.core.utils.HttpRequestUtils;
import com.weweibuy.framework.common.mvc.advice.FeignExceptionHandler;
import com.weweibuy.framework.common.mvc.advice.FeignMethodKeyMappingConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 默认处理器
 *
 * @author durenhao
 * @date 2020/7/6 17:17
 **/
@Slf4j
public class DefaultFeignExceptionHandler implements FeignExceptionHandler {

    private List<FeignMethodKeyMappingConverter> feignMethodKeyMappingConverterList;

    public DefaultFeignExceptionHandler(List<FeignMethodKeyMappingConverter> feignMethodKeyMappingConverterList) {
        this.feignMethodKeyMappingConverterList = Optional.ofNullable(feignMethodKeyMappingConverterList)
                .orElse(Collections.emptyList());
    }

    @Override
    public ResponseEntity<CommonCodeResponse> handlerException(HttpServletRequest request, MethodKeyFeignException e) {
        String methodKey = e.getMethodKey();
        int status = e.status();
        String content = e.contentUTF8();

        FeignMethodKeyMappingConverter feignMethodKeyMappingConverter = feignMethodKeyMappingConverterList.stream()
                .filter(f -> f.match(methodKey))
                .findFirst().orElse(null);

        if (feignMethodKeyMappingConverter != null) {
            return feignMethodKeyMappingConverter.convertResponse(status, content);
        }
        try {
            return ResponseEntity.status(status).body(
                    CommonCodeResponse.response(HttpRequestUtils.convertJsonStrToCodeAndMsg(content)));
        } catch (Exception ex) {
            log.warn("Feign异常报文: {}, 无法转为 code ,msg 形式", content);

            ResponseCodeAndMsg codeAndMsg = null;
            if (e.status() < 500) {
                codeAndMsg = CommonErrorCodeEum.REQUEST_EXCEPTION;
            } else {
                codeAndMsg = CommonErrorCodeEum.UNKNOWN_SERVER_EXCEPTION;
            }

            return ResponseEntity.status(status).body(CommonCodeResponse.response(codeAndMsg));

        }

    }


}
