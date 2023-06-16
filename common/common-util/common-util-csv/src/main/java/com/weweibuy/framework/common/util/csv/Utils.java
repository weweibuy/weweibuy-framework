package com.weweibuy.framework.common.util.csv;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

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

    static CsvTypeConverter typeConverter(Class<? extends CsvTypeConverter> convertType, String pattern, Class fieldType) {
        boolean simpleCsvTypeConverter = SimpleCsvTypeConverter.isSimpleCsvTypeConverter(convertType);
        boolean hasPattern = StringUtils.isNotBlank(pattern);
        if (hasPattern && simpleCsvTypeConverter && ClassUtils.isAssignable(fieldType, Date.class)) {
            DataCsvTypeConverter dataCsvTypeConverter = new DataCsvTypeConverter();
            dataCsvTypeConverter.setPattern(pattern);
            return dataCsvTypeConverter;
        }

        if (hasPattern && simpleCsvTypeConverter && ClassUtils.isAssignable(fieldType, LocalDate.class)) {
            CsvTypeConverter dataCsvTypeConverter = new LocalDateCsvTypeConverter();
            dataCsvTypeConverter.setPattern(pattern);
            return dataCsvTypeConverter;
        }

        if (hasPattern && simpleCsvTypeConverter && ClassUtils.isAssignable(fieldType, LocalDateTime.class)) {
            CsvTypeConverter dataCsvTypeConverter = new LocalDateTimeCsvTypeConverter();
            dataCsvTypeConverter.setPattern(pattern);
            return dataCsvTypeConverter;
        }

        if (simpleCsvTypeConverter && ClassUtils.isAssignable(fieldType, LocalTime.class)) {
            CsvTypeConverter dataCsvTypeConverter = new LocalTimeCsvTypeConverter();
            dataCsvTypeConverter.setPattern(pattern);
            return dataCsvTypeConverter;
        }

        if (simpleCsvTypeConverter && ClassUtils.isAssignable(fieldType, Date.class)) {
            DataCsvTypeConverter dataCsvTypeConverter = new DataCsvTypeConverter();
            dataCsvTypeConverter.setPattern(pattern);
            return dataCsvTypeConverter;
        }

        if (simpleCsvTypeConverter) {
            return SimpleCsvTypeConverter.INSTANCE;
        }

        try {
            CsvTypeConverter csvTypeConverter = convertType.newInstance();
            csvTypeConverter.setPattern(pattern);
            return csvTypeConverter;
        } catch (Exception e) {
            throw new IllegalArgumentException("Can not instance custom converter:" + convertType.getName());
        }
    }

}
