package com.weweibuy.framework.common.log.mvc;

import com.weweibuy.framework.common.core.support.ReadableBodyRequestHandler;
import com.weweibuy.framework.common.log.config.CommonLogProperties;
import com.weweibuy.framework.common.log.logger.HttpReqLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * 输出MVC 请求日志
 *
 * @author durenhao
 * @date 2023/2/12 17:52
 **/
@Slf4j
@Order(0)
public class MvcLogRequestHandler implements ReadableBodyRequestHandler {

    private MvcPathMappingOperator mvcPathMappingOperator;

    public MvcLogRequestHandler(MvcPathMappingOperator mvcPathMappingOperator) {
        this.mvcPathMappingOperator = mvcPathMappingOperator;
    }


    @Override
    public boolean handlerReadableBodyRequest(HttpServletRequest request, HttpServletResponse response) {
        try {
            handlerReadableBodyRequest0(request, response);
        } catch (Exception e) {
            log.error("输出请求日志异常: ", e);
        }
        return true;
    }

    private void handlerReadableBodyRequest0(HttpServletRequest request, HttpServletResponse response) {
        CommonLogProperties.LogProperties logProperties = mvcPathMappingOperator.findLogProperties(request);

        if (logProperties != null && Boolean.TRUE.equals(logProperties.getDisableReq())) {
            return;
        }

        Set<String> headerKeyList = Optional.ofNullable(logProperties)
                .map(CommonLogProperties.LogProperties::getLogReqHeader)
                .orElse(null);

        Boolean disableReqBody = Optional.ofNullable(logProperties)
                .map(CommonLogProperties.LogProperties::getDisableReqBody)
                .orElse(false);

        HttpReqLogger.logForRequest(request, headerKeyList, disableReqBody);

    }


}
