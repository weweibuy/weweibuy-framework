package com.weweibuy.framework.common.log.mvc;

import com.weweibuy.framework.common.core.model.constant.CommonConstant;
import com.weweibuy.framework.common.core.support.ReadableBodyResponseHandler;
import com.weweibuy.framework.common.log.config.CommonLogProperties;
import com.weweibuy.framework.common.log.logger.HttpLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.Set;

/**
 * 输出MVC 响应日志
 *
 * @author durenhao
 * @date 2023/2/12 17:52
 **/
@Slf4j
@Order(10000)
public class MvcLogResponseHandler implements ReadableBodyResponseHandler {


    private MvcPathMappingOperator mvcPathMappingOperator;

    public MvcLogResponseHandler(MvcPathMappingOperator mvcPathMappingOperator) {
        this.mvcPathMappingOperator = mvcPathMappingOperator;
    }

    @Override
    public boolean handlerReadableBodyResponse(HttpServletRequest request, ContentCachingResponseWrapper response) {
        try {
            handlerReadableBodyResponse0(request, response);
        } catch (Exception e) {
            log.error("输出响应日志异常: ", e);
        }
        return true;
    }


    private void handlerReadableBodyResponse0(HttpServletRequest request, ContentCachingResponseWrapper response) {
        CommonLogProperties.LogProperties logProperties = mvcPathMappingOperator.findLogProperties(request);

        if (logProperties != null && Boolean.TRUE.equals(logProperties.getDisableResp())) {
            return;
        }

        Set<String> headerKeyList = Optional.ofNullable(logProperties)
                .map(CommonLogProperties.LogProperties::getLogReqHeader)
                .orElse(null);

        Boolean disableRespBody = Optional.ofNullable(logProperties)
                .map(CommonLogProperties.LogProperties::getDisableRespBody)
                .orElse(false);
        Long reqTime = (Long) Optional.ofNullable(request.getAttribute(CommonConstant.HttpServletConstant.REQUEST_TIMESTAMP))
                .orElse(0L);
        HttpLogger.logResponseBody(response, headerKeyList, disableRespBody, reqTime);
    }


}
