package com.weweibuy.framework.common.util.csv;

import com.weweibuy.framework.common.core.model.constant.CommonConstant;
import com.weweibuy.framework.common.core.utils.DateTimeUtils;
import com.weweibuy.framework.common.util.csv.annotation.CsvProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalQuery;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author durenhao
 * @date 2021/1/14 22:07
 **/
public class SimpleCsvTypeConverter implements CsvTypeConverter {

    static final SimpleCsvTypeConverter INSTANCE = new SimpleCsvTypeConverter();

    private static final String NAME = SimpleCsvTypeConverter.class.getName();


    private static Map<String, CsvPropertyDataConvert<Object, String>> writeFunctionMap;

    private static Map<String, CsvPropertyDataConvert<String, Object>> readFunctionMap;

    private static final CsvPropertyDataConvert<Object, String> SIMPLE_WRITE_FUNCTION = (o, csv) -> simpleWrite(o, csv);


    static {
        TypeFunctionEum[] functionEnums = TypeFunctionEum.values();
        readFunctionMap = new HashMap<>(functionEnums.length);
        writeFunctionMap = new HashMap<>(functionEnums.length);
        for (int i = 0; i < functionEnums.length; i++) {
            TypeFunctionEum functionEnum = functionEnums[i];
            String name = functionEnum.getName();
            readFunctionMap.put(name, functionEnum.getReadFunction());
            writeFunctionMap.put(name, functionEnum.getWriteFunction());
        }
        readFunctionMap = Collections.unmodifiableMap(readFunctionMap);
        writeFunctionMap = Collections.unmodifiableMap(writeFunctionMap);
    }


    public Function<Object, String> writeConvert(Class fieldType, CsvProperty csvProperty) {
        CsvPropertyDataConvert<Object, String> dataConvert = writeFunctionMap.get(fieldType.getName());
        return t -> dataConvert.convert(t, csvProperty);
    }


    public Function<String, Object> readConvert(Class fieldType, CsvProperty csvProperty) {
        CsvPropertyDataConvert<String, Object> dataConvert = readFunctionMap.get(fieldType.getName());
        return t -> dataConvert.convert(t, csvProperty);
    }


    static boolean isSimpleCsvTypeConverter(Class<? extends CsvTypeConverter> clazz) {
        return NAME.equals(clazz.getName());
    }


    static Object simpleRead(String value, CsvProperty csv, Object defaultValue, Function<String, Object> function) {
        if (StringUtils.EMPTY.equals(value)) {
            return defaultValue;
        }
        return function.apply(value);
    }


    static String simpleWrite(Object value, CsvProperty csv) {
        if (value == null) {
            return null;
        }
        return value.toString();
    }


    static String date8Write(TemporalAccessor value, CsvProperty csv, String defaultPattern) {
        if (value == null) {
            return null;
        }
        String patten = Optional.ofNullable(csv.pattern())
                .filter(StringUtils::isNotBlank)
                .orElse(defaultPattern);
        return DateTimeUtils.toStringDate(value, patten);
    }

    static <T> T date8Read(String str, CsvProperty csv, TemporalQuery<T> query, String defaultPattern) {
        if (StringUtils.EMPTY.equals(str)) {
            return null;
        }
        String patten = Optional.ofNullable(csv.pattern())
                .filter(StringUtils::isNotBlank)
                .orElse(defaultPattern);
        DateTimeFormatter dateTimeFormatter = DateTimeUtils.dateTimeFormatter(patten);
        return dateTimeFormatter.parse(str, query);
    }

    static String dateWrite(Date date, CsvProperty csv) {
        if (date == null) {
            return null;
        }
        String patten = Optional.ofNullable(csv.pattern())
                .filter(StringUtils::isNotBlank)
                .orElse(CommonConstant.DateConstant.STANDARD_DATE_TIME_FORMAT_STR);
        return DateTimeUtils.toStringDate(date, patten);
    }

    static Date dateRead(String str, CsvProperty csv) {
        if (StringUtils.EMPTY.equals(str)) {
            return null;
        }
        String patten = Optional.ofNullable(csv.pattern())
                .filter(StringUtils::isNotBlank)
                .orElse(CommonConstant.DateConstant.STANDARD_DATE_TIME_FORMAT_STR);
        return DateTimeUtils.strToDate(str, patten);
    }

    @Getter
    @RequiredArgsConstructor
    static enum TypeFunctionEum {

        INT(int.class.getName(), (s, csv) -> simpleRead(s, csv, 0, Integer::valueOf), SIMPLE_WRITE_FUNCTION),

        BYTE(byte.class.getName(), (s, csv) -> simpleRead(s, csv, (byte) 0, Byte::valueOf), SIMPLE_WRITE_FUNCTION),

        LONG(long.class.getName(), (s, csv) -> simpleRead(s, csv, 0L, Long::valueOf), SIMPLE_WRITE_FUNCTION),

        DOUBLE(double.class.getName(), (s, csv) -> simpleRead(s, csv, 0.0d, Double::valueOf), SIMPLE_WRITE_FUNCTION),

        FLOAT(float.class.getName(), (s, csv) -> simpleRead(s, csv, 0.0f, Float::valueOf), SIMPLE_WRITE_FUNCTION),

        SHORT(short.class.getName(), (s, csv) -> simpleRead(s, csv, (short) 0, Short::valueOf), SIMPLE_WRITE_FUNCTION),

        CHAR(char.class.getName(), (s, csv) -> simpleRead(s, csv, '\u0000', s1 -> s1.toCharArray()[0]), SIMPLE_WRITE_FUNCTION),

        BOOLEAN(boolean.class.getName(), (s, csv) -> simpleRead(s, csv, false, Boolean::valueOf), SIMPLE_WRITE_FUNCTION),

        INTEGER(Integer.class.getName(), (s, csv) -> simpleRead(s, csv, null, Integer::valueOf), SIMPLE_WRITE_FUNCTION),

        LONG_BOX(Long.class.getName(), (s, csv) -> simpleRead(s, csv, null, Long::valueOf), SIMPLE_WRITE_FUNCTION),

        BYTE_BOX(Byte.class.getName(), (s, csv) -> simpleRead(s, csv, null, Byte::valueOf), SIMPLE_WRITE_FUNCTION),

        DOUBLE_BOX(Double.class.getName(), (s, csv) -> simpleRead(s, csv, null, Double::valueOf), SIMPLE_WRITE_FUNCTION),

        FLOAT_BOX(Float.class.getName(), (s, csv) -> simpleRead(s, csv, null, Float::valueOf), SIMPLE_WRITE_FUNCTION),

        CHARACTER(Character.class.getName(), (s, csv) -> simpleRead(s, csv, null, s1 -> s1.toCharArray()[0]), SIMPLE_WRITE_FUNCTION),

        SHORT_BOX(Short.class.getName(), (s, csv) -> simpleRead(s, csv, null, Short::valueOf), SIMPLE_WRITE_FUNCTION),

        BOOLEAN_BOX(Boolean.class.getName(), (s, csv) -> simpleRead(s, csv, null, Boolean::valueOf), SIMPLE_WRITE_FUNCTION),

        STRING(String.class.getName(), (s, csv) -> s, SIMPLE_WRITE_FUNCTION),

        BIG_DECIMAL(BigDecimal.class.getName(), (s, csv) -> simpleRead(s, csv, null, BigDecimal::new), SIMPLE_WRITE_FUNCTION),

        DATE(Date.class.getName(), (s, csv) -> dateRead(s, csv), (s, csv) -> dateWrite((Date) s, csv)),

        LOCAL_DATE(LocalDate.class.getName(), (s, csv) -> date8Read(s, csv, LocalDate::from, CommonConstant.DateConstant.STANDARD_DATE_FORMAT_STR), (s, csv) -> date8Write((LocalDate) s, csv, CommonConstant.DateConstant.STANDARD_DATE_FORMAT_STR)),

        LOCAL_TIME(LocalTime.class.getName(), (s, csv) -> date8Read(s, csv, LocalTime::from, CommonConstant.DateConstant.STANDARD_TIME_FORMAT_STR), (s, csv) -> date8Write((LocalTime) s, csv, CommonConstant.DateConstant.STANDARD_TIME_FORMAT_STR)),

        LOCAL_DATE_TIME(LocalDateTime.class.getName(), (s, csv) -> date8Read(s, csv, LocalDateTime::from, CommonConstant.DateConstant.STANDARD_DATE_TIME_FORMAT_STR), (s, csv) -> date8Write((LocalDateTime) s, csv, CommonConstant.DateConstant.STANDARD_DATE_TIME_FORMAT_STR)),

        OBJECT(Object.class.getName(), (s, csv) -> s, SIMPLE_WRITE_FUNCTION),

        ;


        private final String name;

        /**
         * 读转化
         */
        private final CsvPropertyDataConvert<String, Object> readFunction;

        /**
         * 写转化
         */
        private final CsvPropertyDataConvert<Object, String> writeFunction;


    }


}
