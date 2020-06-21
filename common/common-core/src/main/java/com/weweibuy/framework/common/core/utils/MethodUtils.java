package com.weweibuy.framework.common.core.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * 方法工具
 *
 * @author durenhao
 * @date 2019/12/30 23:07
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MethodUtils {

    /**
     * 判断方法是否为 default
     *
     * @param method
     * @return
     */
    public static boolean isDefault(Method method) {
        final int SYNTHETIC = 0x00001000;
        return ((method.getModifiers() & (Modifier.ABSTRACT | Modifier.PUBLIC | Modifier.STATIC | SYNTHETIC)) ==
                Modifier.PUBLIC) && method.getDeclaringClass().isInterface();
    }


}
