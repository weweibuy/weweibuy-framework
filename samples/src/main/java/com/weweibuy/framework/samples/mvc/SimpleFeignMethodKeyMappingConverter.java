package com.weweibuy.framework.samples.mvc;

import com.weweibuy.framework.common.core.model.dto.CommonCodeResponse;
import com.weweibuy.framework.common.mvc.advice.FeignMethodKeyMappingConverter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * @author durenhao
 * @date 2020/7/6 21:30
 **/
@Component
public class SimpleFeignMethodKeyMappingConverter implements FeignMethodKeyMappingConverter {

    @Override
    public ResponseEntity<CommonCodeResponse> convertResponse(Integer httpStatus, String content) {
        return ResponseEntity.status(401).body(CommonCodeResponse.requestLimit());
    }

    @Override
    public boolean match(String methodKey) {
        return methodKey.startsWith("MyFeignClient");
    }
}
