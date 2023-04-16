package com.weweibuy.framework.common.log.mvc;

import com.weweibuy.framework.common.core.support.ReadableBodyRequestHandler;
import com.weweibuy.framework.common.log.config.CommonLogProperties;
import com.weweibuy.framework.common.log.logger.HttpLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

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
        CommonLogProperties.CommonLogHttpProperties logProperties = mvcPathMappingOperator.findLogProperties(request);

        if (logProperties != null && Boolean.TRUE.equals(logProperties.getDisableReq())) {
            return;
        }

        List<String> headerKeyList = Optional.ofNullable(logProperties)
                .map(CommonLogProperties.CommonLogHttpProperties::getLogReqHeader)
                .orElse(null);

        HttpLogger.logForRequest(request, headerKeyList);

    }


}
