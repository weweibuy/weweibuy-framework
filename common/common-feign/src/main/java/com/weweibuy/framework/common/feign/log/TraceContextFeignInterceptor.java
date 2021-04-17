package com.weweibuy.framework.common.feign.log;

import com.weweibuy.framework.common.core.model.constant.CommonConstant;
import com.weweibuy.framework.common.core.utils.IdWorker;
import com.weweibuy.framework.common.log.support.LogTraceContext;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 传递 traceCode
 *
 * @author durenhao
 * @date 2020/7/11 11:41
 **/
@Slf4j
@Order(Integer.MIN_VALUE + 100)
public class TraceContextFeignInterceptor implements RequestInterceptor {

    private static String ip;

    static {
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.warn("获取本机ip异常: {}", e.getMessage());
            ip = StringUtils.EMPTY;
        }
    }

    @Override
    public void apply(RequestTemplate template) {
        addTraceHeader(template);
    }

    private static void addTraceHeader(RequestTemplate request) {
        String traceCode = LogTraceContext.getTraceCode()
                .orElse(IdWorker.nextStringId());
        String userCode = LogTraceContext.getUserCode()
                .orElse(ip);

        request.header(CommonConstant.LogTraceConstant.HTTP_TRACE_CODE_HEADER, traceCode);

        request.header(CommonConstant.LogTraceConstant.HTTP_USER_CODE_HEADER, userCode);
    }
}
