package com.weweibuy.framework.common.core.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * 字符串拼接工具
 *
 * @author durenhao
 * @date 2020/7/3 22:47
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringConnectUtils {

    /**
     * 拼接字符串
     *
     * @param delimiter 分割符
     * @param strArr
     * @return
     */
    public static String connect(String delimiter, String... strArr) {
        if (ArrayUtils.isEmpty(strArr)) {
            return StringUtils.EMPTY;
        }
        return Arrays.stream(strArr).collect(Collectors.joining(delimiter));
    }


}
