package com.weweibuy.framework.rocketmq.utils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @author durenhao
 * @date 2019/12/30 23:07
 **/
public class MethodUtils {

    public static boolean isDefault(Method method) {
        final int SYNTHETIC = 0x00001000;
        return ((method.getModifiers() & (Modifier.ABSTRACT | Modifier.PUBLIC | Modifier.STATIC | SYNTHETIC)) ==
                Modifier.PUBLIC) && method.getDeclaringClass().isInterface();
    }


}
