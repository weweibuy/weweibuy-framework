package com.weweibuy.framework.common.log.mvc;

import com.weweibuy.framework.common.core.support.ReadableBodyResponseHandler;
import com.weweibuy.framework.common.core.utils.HttpRequestUtils;
import com.weweibuy.framework.common.log.config.CommonLogProperties;
import com.weweibuy.framework.common.log.logger.HttpLogger;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.util.List;
import java.util.Optional;

/**
 * 输出MVC 响应日志
 *
 * @author durenhao
 * @date 2023/2/12 17:52
 **/
@Order(10000)
public class MvcLogResponseHandler implements ReadableBodyResponseHandler {


    private MvcPathMappingOperator mvcPathMappingOperator;

    public MvcLogResponseHandler(MvcPathMappingOperator mvcPathMappingOperator) {
        this.mvcPathMappingOperator = mvcPathMappingOperator;
    }

    @Override
    public boolean handlerReadableBodyResponse(HttpServletRequest request, ContentCachingResponseWrapper response) {
        CommonLogProperties.CommonLogHttpProperties logProperties = mvcPathMappingOperator.findLogProperties(request);

        if (logProperties != null && Boolean.TRUE.equals(logProperties.getDisableResp())) {
            return true;
        }
        String contentType = response.getContentType();
        boolean notBoundaryBody = StringUtils.isBlank(contentType)
                || HttpRequestUtils.notBoundaryBody(contentType);

        List<String> headerKeyList = Optional.ofNullable(logProperties)
                .map(CommonLogProperties.CommonLogHttpProperties::getLogReqHeader)
                .orElse(null);

        HttpLogger.logResponseBody(response, notBoundaryBody, headerKeyList);
        return true;
    }


}
