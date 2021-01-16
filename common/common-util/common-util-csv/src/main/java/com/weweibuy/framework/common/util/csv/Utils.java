package com.weweibuy.framework.common.util.csv;

import java.lang.reflect.Field;

/**
 * @author durenhao
 * @date 2021/1/16 13:57
 **/
class Utils {

    static String fieldGetter(Field field) {
        return "get" + captureName(field.getName());
    }

    static String fieldSetter(Field field) {
        return "set" + captureName(field.getName());
    }

    static String captureName(String str) {
        char[] cs = str.toCharArray();
        cs[0] -= 32;
        return String.valueOf(cs);
    }

    static CsvTypeConverter typeConverter(Class<? extends CsvTypeConverter> convertType, Class fieldType) {
        boolean simpleCsvTypeConverter = SimpleCsvTypeConverter.isSimpleCsvTypeConverter(convertType);
        if (simpleCsvTypeConverter) {
            return SimpleCsvTypeConverter.INSTANCE;
        }

        try {
            return convertType.newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException("Can not instance custom converter:" + convertType.getName());
        }
    }

}
