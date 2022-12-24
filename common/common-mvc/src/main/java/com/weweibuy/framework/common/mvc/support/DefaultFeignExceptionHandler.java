package com.weweibuy.framework.common.mvc.support;

import com.weweibuy.framework.common.core.exception.MethodKeyFeignException;
import com.weweibuy.framework.common.core.model.ResponseCodeAndMsg;
import com.weweibuy.framework.common.core.model.constant.CommonConstant;
import com.weweibuy.framework.common.core.model.dto.CommonCodeResponse;
import com.weweibuy.framework.common.core.model.eum.CommonErrorCodeEum;
import com.weweibuy.framework.common.core.utils.HttpRequestUtils;
import com.weweibuy.framework.common.core.utils.ResponseCodeUtils;
import com.weweibuy.framework.common.mvc.advice.FeignExceptionHandler;
import com.weweibuy.framework.common.mvc.advice.FeignMethodKeyMappingConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.HttpServletRequest;
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
        String systemId = e.getSystemId();


        FeignMethodKeyMappingConverter feignMethodKeyMappingConverter = feignMethodKeyMappingConverterList.stream()
                .filter(f -> f.match(methodKey))
                .findFirst().orElse(null);

        if (feignMethodKeyMappingConverter != null) {
            return feignMethodKeyMappingConverter.convertResponse(status, content);
        }
        try {
            ResponseCodeAndMsg responseCodeAndMsg = HttpRequestUtils.convertJsonStrToCodeAndMsg(content);

            if (StringUtils.isBlank(responseCodeAndMsg.getCode())) {
                responseCodeAndMsg = HttpRequestUtils.httpHttpStatus(status)
                        .map(s -> ResponseCodeUtils.newResponseCodeAndMsg(s.value() + "", content))
                        .orElseGet(() ->
                                ResponseCodeUtils.newResponseCodeAndMsg(CommonErrorCodeEum.UNKNOWN_EXCEPTION.getCode(),
                                        "调用第三方接口异常: " + content));
            }

            return ResponseEntity.status(status)
                    .header(CommonConstant.HttpResponseConstant.RESPONSE_HEADER_FIELD_SYSTEM_ID, systemId)
                    .body(CommonCodeResponse.response(responseCodeAndMsg));
        } catch (Exception ex) {
            log.warn("Feign 异常报文: {}, 无法转为 code ,msg 形式", content);

            return ResponseEntity.status(status)
                    .header(CommonConstant.HttpResponseConstant.RESPONSE_HEADER_FIELD_SYSTEM_ID, systemId)
                    .body(CommonCodeResponse.response(status + "", "调用第三方接口异常: " + content));
        }

    }


}
