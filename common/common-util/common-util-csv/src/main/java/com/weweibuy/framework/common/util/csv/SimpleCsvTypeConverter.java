package com.weweibuy.framework.common.util.csv;

import lombok.Getter;

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
        TypeFunctionEum[] functionEums = TypeFunctionEum.values();
        typeFunction = new Function[functionEums.length];
        typeIndexMap = Arrays.stream(functionEums)
                .collect(Collectors.toMap(TypeFunctionEum::getName, TypeFunctionEum::getIndex));

        for (int i = 0; i < functionEums.length; i++) {
            typeFunction[i] = functionEums[i].getFunction();
        }

    }

    @Override
    public String convert(Object o) {
        if (o == null) {
            return "";
        }
        return o.toString();
    }

    @Override
    public Object convert(String value, Class fieldType, int typeIndex) {
        return Optional.ofNullable(typeFunction[typeIndex])
                .map(f -> f.apply(value))
                .orElse(null);
    }

    @Override
    public int typeIndex(Class fieldType) {
        return Optional.ofNullable(typeIndexMap.get(fieldType.getName())).orElse(-1);
    }


    static boolean isSimpleCsvTypeConverter(Class<? extends CsvTypeConverter> clazz) {
        return NAME.equals(clazz.getName());
    }


    @Getter
    static enum TypeFunctionEum {

        INT(0, int.class.getName(), Integer::valueOf),

        BYTE(1, byte.class.getName(), Byte::valueOf),

        LONG(2, long.class.getName(), Long::valueOf),

        DOUBLE(3, double.class.getName(), Double::valueOf),

        FLOAT(4, float.class.getName(), Float::valueOf),

        SHORT(5, short.class.getName(), Short::valueOf),

        CHAR(6, char.class.getName(), s -> s.toCharArray()[0]),

        BOOLEAN(7, boolean.class.getName(), Boolean::valueOf),

        INTEGER(8, Integer.class.getName(), Integer::valueOf),

        LONG_BOX(9, Long.class.getName(), Long::valueOf),

        BYTE_BOX(10, Byte.class.getName(), Byte::valueOf),

        DOUBLE_BOX(11, Double.class.getName(), Double::valueOf),

        FLOAT_BOX(12, Float.class.getName(), Float::valueOf),

        CHARACTER(13, Character.class.getName(), s -> s.toCharArray()[0]),

        SHORT_BOX(14, Short.class.getName(), Short::valueOf),

        BOOLEAN_BOX(15, Boolean.class.getName(), Boolean::valueOf),

        STRING(16, String.class.getName(), s -> s),

        BIG_DECIMAL(17, BigDecimal.class.getName(), BigDecimal::new),

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
