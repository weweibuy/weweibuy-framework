package com.weweibuy.framework.common.util.csv;

import com.weweibuy.framework.common.core.exception.Exceptions;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author durenhao
 * @date 2021/1/14 22:07
 **/
public class SimpleCsvTypeConverter implements CsvTypeConverter {

    static final SimpleCsvTypeConverter INSTANCE = new SimpleCsvTypeConverter();

    private static final String NAME = SimpleCsvTypeConverter.class.getName();

    private static Function<String, Object>[] typeFunction;

    private static Map<String, Integer> typeIndexMap;

    static {
        TypeFunctionEum[] functionEnums = TypeFunctionEum.values();
        typeFunction = new Function[functionEnums.length];
        typeIndexMap = Arrays.stream(functionEnums)
                .collect(Collectors.toMap(TypeFunctionEum::getName, TypeFunctionEum::getIndex));

        for (int i = 0; i < functionEnums.length; i++) {
            typeFunction[i] = functionEnums[i].getFunction();
        }

    }

    @Override
    public String convert(Object o) {
        if (o == null) {
            return null;
        }
        return o.toString();
    }

    @Override
    public Object convert(String value, Class fieldType, int typeIndex) {
        if (-1 == typeIndex) {
            throw Exceptions.business("不支持的CSV数据类型转化: ", fieldType.getName());
        }
        return Optional.ofNullable(typeFunction[typeIndex])
                .map(f -> f.apply(value))
                .orElse(null);
    }

    @Override
    public int typeIndex(Class fieldType) {
        return Optional.ofNullable(typeIndexMap.get(fieldType.getName())).orElse(-1);
    }

    @Override
    public void setPattern(String pattern) {

    }


    static boolean isSimpleCsvTypeConverter(Class<? extends CsvTypeConverter> clazz) {
        return NAME.equals(clazz.getName());
    }


    static Object emptyOrDefault(String value, Object defaultValue, Function<String, Object> function) {
        if (StringUtils.EMPTY.equals(value)) {
            return defaultValue;
        }
        return function.apply(value);
    }


    @Getter
    static enum TypeFunctionEum {

        INT(0, int.class.getName(), s -> emptyOrDefault(s, 0, Integer::valueOf)),

        BYTE(1, byte.class.getName(), s -> emptyOrDefault(s, (byte) 0, Byte::valueOf)),

        LONG(2, long.class.getName(), s -> emptyOrDefault(s, 0L, Long::valueOf)),

        DOUBLE(3, double.class.getName(), s -> emptyOrDefault(s, 0.0d, Double::valueOf)),

        FLOAT(4, float.class.getName(), s -> emptyOrDefault(s, 0.0f, Float::valueOf)),

        SHORT(5, short.class.getName(), s -> emptyOrDefault(s, (short) 0, Short::valueOf)),

        CHAR(6, char.class.getName(), s -> emptyOrDefault(s, '\u0000', s1 -> s1.toCharArray()[0])),

        BOOLEAN(7, boolean.class.getName(), s -> emptyOrDefault(s, false, Boolean::valueOf)),

        INTEGER(8, Integer.class.getName(), s -> emptyOrDefault(s, null, Integer::valueOf)),

        LONG_BOX(9, Long.class.getName(), s -> emptyOrDefault(s, null, Long::valueOf)),

        BYTE_BOX(10, Byte.class.getName(), s -> emptyOrDefault(s, null, Byte::valueOf)),

        DOUBLE_BOX(11, Double.class.getName(), s -> emptyOrDefault(s, null, Double::valueOf)),

        FLOAT_BOX(12, Float.class.getName(), s -> emptyOrDefault(s, null, Float::valueOf)),

        CHARACTER(13, Character.class.getName(), s -> emptyOrDefault(s, null, s1 -> s1.toCharArray()[0])),

        SHORT_BOX(14, Short.class.getName(), s -> emptyOrDefault(s, null, Short::valueOf)),

        BOOLEAN_BOX(15, Boolean.class.getName(), s -> emptyOrDefault(s, null, Boolean::valueOf)),

        STRING(16, String.class.getName(), s -> s),

        BIG_DECIMAL(17, BigDecimal.class.getName(), s -> emptyOrDefault(s, null, BigDecimal::new)),

        OBJECT(18, Object.class.getName(), s -> s),;

        private Integer index;

        private String name;

        private Function<String, Object> function;

        TypeFunctionEum(Integer index, String name, Function<String, Object> function) {
            this.index = index;
            this.name = name;
            this.function = function;
        }
    }

}
