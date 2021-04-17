package com.weweibuy.framework.common.core.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 异常工具
 *
 * @author durenhao
 * @date 2020/9/23 18:34
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionUtils {

    /**
     * 拼接全部异常信息
     *
     * @param throwable
     * @return
     */
    public static String exceptionMsg(Throwable throwable) {
        List<Throwable> throwableList = org.apache.commons.lang3.exception.ExceptionUtils.getThrowableList(throwable);
        return throwableList.stream()
                .map(org.apache.commons.lang3.exception.ExceptionUtils::getMessage)
                .collect(Collectors.joining(";"));
    }

}
