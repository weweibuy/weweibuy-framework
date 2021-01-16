package com.weweibuy.framework.common.util.csv;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author durenhao
 * @date 2021/1/14 22:07
 **/
public class SimpleCsvTypeConverter implements CsvTypeConverter {

    private static final Map<String, Function<String, Object>> BOXING_MAP = new HashMap<>(16);

    static {
        BOXING_MAP.put(int.class.getName(), Integer::valueOf);
        BOXING_MAP.put(byte.class.getName(), Byte::valueOf);
        BOXING_MAP.put(long.class.getName(), Long::valueOf);
        BOXING_MAP.put(double.class.getName(), Double::valueOf);
        BOXING_MAP.put(float.class.getName(), Float::valueOf);
        BOXING_MAP.put(char.class.getName(), s -> s.toCharArray()[0]);
        BOXING_MAP.put(short.class.getName(), Short::valueOf);
        BOXING_MAP.put(boolean.class.getName(), Boolean::valueOf);

        BOXING_MAP.put(Integer.class.getName(), Integer::valueOf);
        BOXING_MAP.put(Byte.class.getName(), Byte::valueOf);
        BOXING_MAP.put(Long.class.getName(), Long::valueOf);
        BOXING_MAP.put(Double.class.getName(), Double::valueOf);
        BOXING_MAP.put(Float.class.getName(), Float::valueOf);
        BOXING_MAP.put(Character.class.getName(), s -> s.toCharArray()[0]);
        BOXING_MAP.put(Short.class.getName(), Short::valueOf);
        BOXING_MAP.put(Boolean.class.getName(), Boolean::valueOf);
        BOXING_MAP.put(String.class.getName(), s -> s);

    }

    @Override
    public String convert(Object o) {
        if (o == null) {
            return "";
        }
        return o.toString();
    }

    @Override
    public Object convert(String value, Class fieldType) {
        return Optional.ofNullable(BOXING_MAP.get(fieldType.getName()))
                .map(f -> f.apply(value))
                .orElse(null);
    }

}
