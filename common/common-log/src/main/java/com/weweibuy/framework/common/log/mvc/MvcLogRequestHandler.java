package com.weweibuy.framework.common.log.mvc;

import com.weweibuy.framework.common.core.support.ReadableBodyRequestHandler;
import com.weweibuy.framework.common.core.utils.HttpRequestUtils;
import com.weweibuy.framework.common.log.config.CommonLogProperties;
import com.weweibuy.framework.common.log.logger.HttpLogger;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;

import java.util.List;
import java.util.Optional;

/**
 * 输出MVC 请求日志
 *
 * @author durenhao
 * @date 2023/2/12 17:52
 **/
@Order(0)
public class MvcLogRequestHandler implements ReadableBodyRequestHandler {

    private MvcPathMappingOperator mvcPathMappingOperator;

    public MvcLogRequestHandler(MvcPathMappingOperator mvcPathMappingOperator) {
        this.mvcPathMappingOperator = mvcPathMappingOperator;
    }


    @Override
    public boolean handlerReadableBodyRequest(HttpServletRequest request, HttpServletResponse response) {
        CommonLogProperties.CommonLogHttpProperties logProperties = mvcPathMappingOperator.findLogProperties(request);

        if (logProperties != null && Boolean.TRUE.equals(logProperties.getDisableReq())) {
            return true;
        }
        String contentType = request.getContentType();

        boolean notBoundaryBody = StringUtils.isBlank(contentType)
                || HttpRequestUtils.notBoundaryBody(contentType);

        List<String> headerKeyList = Optional.ofNullable(logProperties)
                .map(CommonLogProperties.CommonLogHttpProperties::getLogReqHeader)
                .orElse(null);

        HttpLogger.logForRequest(request, notBoundaryBody, headerKeyList);
        return true;
    }


}
