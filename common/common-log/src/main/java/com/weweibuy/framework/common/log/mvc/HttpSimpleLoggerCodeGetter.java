package com.weweibuy.framework.common.log.mvc;

import com.weweibuy.framework.common.core.model.constant.CommonConstant;
import com.weweibuy.framework.common.core.support.LogTraceCodeGetter;
import com.weweibuy.framework.common.core.utils.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * @author durenhao
 * @date 2020/7/10 16:15
 **/
@Slf4j
public class HttpSimpleLoggerCodeGetter implements LogTraceCodeGetter<HttpServletRequest> {


    @Override
    public String getUserCode(HttpServletRequest request) {
        return tryGetFromUserHeader(request)
                .filter(StringUtils::isNotBlank)
                .orElse(getUserIp(request));
    }

    @Override
    public String getTraceCode(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(CommonConstant.LogTraceConstant.HTTP_TRACE_CODE_HEADER))
                .filter(StringUtils::isNotBlank)
                .orElse(IdWorker.nextStringId());
    }

    /**
     * 从上游的 header中获取
     *
     * @param request
     * @return
     */
    private Optional<String> tryGetFromUserHeader(HttpServletRequest request) {
        String header = request.getHeader(CommonConstant.LogTraceConstant.HTTP_USER_CODE_HEADER);
        return Optional.ofNullable(header);
    }


    /**
     * 当无法获取用户信息时 使用用户ip代替
     *
     * @param request
     * @return
     */
    private String getUserIp(HttpServletRequest request) {
        return getIp(request);
    }

    private String getIp(HttpServletRequest request) {
        String ip = "";
        if (StringUtils.isNotBlank(ip = request.getHeader("X-Forwarded-For"))) {
            // X-FORWARDED-FOR 的第一个ip为真实ip
            return ip.split(",")[0];
        }

        if (StringUtils.isNotBlank(ip = request.getHeader("Proxy-Client-IP"))) {
            return ip;
        }

        if (StringUtils.isNotBlank(ip = request.getHeader("WL-Proxy-Client-IP"))) {
            return ip;
        }

        if (StringUtils.isNotBlank(ip = request.getHeader("HTTP_CLIENT_IP"))) {
            return ip;
        }

        if (StringUtils.isNotBlank(ip = request.getHeader("HTTP_X_FORWARDED_FOR"))) {
            return ip;
        }
        return request.getRemoteAddr();
    }


}
