package com.weweibuy.framework.common.util.csv;

import com.weweibuy.framework.common.util.csv.annotation.CsvProperty;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author durenhao
 * @date 2021/1/5 22:22
 **/
public class ReflectCsvContentConverter<T> implements CsvContentConverter<T> {

    private final Class<? extends T> type;

    private Integer[] indexArr;

    private String[] header;

    public ReflectCsvContentConverter(Class<? extends T> type) {
        this.type = type;
        init();
    }


    private void init() {
        // a 1;   b  0;  c  3  -->  b a c    0 b      1 a     2 c
        Field[] fieldsWithAnnotation = FieldUtils.getFieldsWithAnnotation(type, CsvProperty.class);

        AtomicInteger arrAtomicInteger = new AtomicInteger(0);

        AtomicInteger sortAtomicInteger = new AtomicInteger(0);

        header = new String[fieldsWithAnnotation.length];
        indexArr = new Integer[fieldsWithAnnotation.length];

        // 数组中索引
        Map<Field, Integer> arrFieldIndexMap = Arrays.stream(fieldsWithAnnotation)
                .collect(Collectors.toMap(Function.identity(), f -> arrAtomicInteger.getAndIncrement()));


        Arrays.stream(fieldsWithAnnotation)
                .sorted(Comparator.comparing(field -> field.getAnnotation(CsvProperty.class).order()))
                .peek(field -> header[sortAtomicInteger.get()] = field.getAnnotation(CsvProperty.class).name())
                .forEach(field -> indexArr[sortAtomicInteger.getAndIncrement()] = arrFieldIndexMap.get(field));

    }

    @Override
    public Collection<String[]> convert(String[] header, List<T> body) {
        List<String[]> content = new ArrayList<>(body.size() + 1);
        content.add(this.header);
        body.stream()
                .map(this::oneLine)
                .forEach(content::add);
        return content;
    }


    private String[] oneLine(T t) {
        Field[] fieldsWithAnnotation = FieldUtils.getFieldsWithAnnotation(t.getClass(), CsvProperty.class);
        String[] strings = new String[header.length];
        for (int i = 0; i < header.length; i++) {
            Field field = fieldsWithAnnotation[indexArr[i]];
            strings[i] = fieldValue(field, t);
        }
        return strings;
    }


    private String fieldValue(Field field, T t) {
        try {
            return FieldUtils.readField(field, t, true).toString();
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }

    }


}
