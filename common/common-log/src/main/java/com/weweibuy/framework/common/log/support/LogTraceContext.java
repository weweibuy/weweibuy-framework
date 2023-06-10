package com.weweibuy.framework.common.log.support;

import com.weweibuy.framework.common.core.exception.Exceptions;
import com.weweibuy.framework.common.core.utils.IdWorker;
import com.weweibuy.framework.common.log.constant.LogMdcConstant;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import java.util.Optional;

/**
 * 日志 trace 上下文
 *
 * @author durenhao
 * @date 2020/7/11 11:45
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LogTraceContext {

    /**
     * 获取 traceCode
     *
     * @return
     */
    public static Optional<String> getTraceCode() {
        return Optional.ofNullable(getTraceCode0());
    }

    /**
     * 获取 traceCode
     *
     * @return
     */
    public static String getTraceCodeOrEmpty() {
        return getTraceCode()
                .orElse("");
    }

    public static String getTraceCodeOrNew() {
        String code = getTraceCode0();
        if (StringUtils.isBlank(code)) {
            code = IdWorker.nextStringId();
            setTraceCode(code);
        }
        return code;
    }

    private static String getTraceCode0() {
        return MDC.get(LogMdcConstant.TID_FIELD_NAME);
    }


    /**
     * 获取 userCode
     *
     * @return
     */
    public static Optional<String> getUserCode() {
        return Optional.ofNullable(userCode0());
    }

    public static String getUserCodeOrThrow() {
        return getUserCode()
                .orElseThrow(() -> Exceptions.business("用户信息异常"));
    }

    public static void setTraceCode(String traceCode) {
        MDC.put(LogMdcConstant.TID_FIELD_NAME, traceCode);
    }

    public static void setUserCode(String userCode) {
        MDC.put(LogMdcConstant.UID_FIELD_NAME, userCode);
    }

    private static String userCode0() {
        return MDC.get(LogMdcConstant.UID_FIELD_NAME);
    }

    public static void setTraceCodeAndUserCode(String traceCode, String userCode) {
        MDC.put(LogMdcConstant.TID_FIELD_NAME, traceCode);
        MDC.put(LogMdcConstant.UID_FIELD_NAME, userCode);
    }

    /**
     * 清除
     */
    public static void clear() {
        MDC.remove(LogMdcConstant.TID_FIELD_NAME);
        MDC.remove(LogMdcConstant.UID_FIELD_NAME);
    }

}
