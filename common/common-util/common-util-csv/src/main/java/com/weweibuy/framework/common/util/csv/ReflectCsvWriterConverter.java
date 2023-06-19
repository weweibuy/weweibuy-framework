package com.weweibuy.framework.common.util.csv;

import com.weweibuy.framework.common.util.csv.annotation.CsvProperty;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.cglib.beans.BulkBean;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

/**
 * 写 csv
 *
 * @author durenhao
 * @date 2021/1/5 22:22
 **/
public class ReflectCsvWriterConverter<T> implements CsvWriterConverter<T> {

    private final Class<? extends T> type;

    private String[] header;

    private Function<Object, String>[] converters;

    private BulkBean bulkBean;

    public ReflectCsvWriterConverter(Class<? extends T> type) {
        this.type = type;
        init();
    }

    private void init() {
        Field[] fieldsWithAnnotation = FieldUtils.getFieldsWithAnnotation(type, CsvProperty.class);

        if (ArrayUtils.isEmpty(fieldsWithAnnotation)) {
            throw new IllegalArgumentException(type.getName() + "没有 @CsvProperty 标记的属性");
        }

        AtomicInteger sortAtomicInteger = new AtomicInteger(0);
        int length = fieldsWithAnnotation.length;
        header = new String[length];
        converters = new Function[length];

        String[] getters = new String[length];
        String[] setters = new String[length];
        Class[] types = new Class[length];

        Arrays.stream(fieldsWithAnnotation)
                .sorted(Comparator.comparing(field -> field.getAnnotation(CsvProperty.class).order()))
                .peek(field -> header[sortAtomicInteger.get()] =
                        Optional.ofNullable(field.getAnnotation(CsvProperty.class).name())
                                .filter(StringUtils::isNotBlank)
                                .orElse(field.getName()))
                .peek(field -> getters[sortAtomicInteger.get()] = Utils.fieldGetter(field))
                .peek(field -> setters[sortAtomicInteger.get()] = Utils.fieldSetter(field))
                .peek(field -> {
                    CsvProperty annotation = field.getAnnotation(CsvProperty.class);
                    converters[sortAtomicInteger.get()] =
                            Utils.typeConverter(annotation.converter()).writeConvert(field.getType(), field.getAnnotation(CsvProperty.class));
                })
                .forEach(field -> types[sortAtomicInteger.getAndIncrement()] = field.getType());

        bulkBean = BulkBean.create(type, getters, setters, types);
    }


    @Override
    public Collection<String[]> convert(String[] header, List<T> body) {
        List<String[]> content = new ArrayList<>(body.size() + 1);
        if (ArrayUtils.isNotEmpty(header)) {
            content.add(header);
        }
        body.stream()
                .map(this::oneLine)
                .forEach(content::add);
        return content;
    }


    private String[] oneLine(T t) {
        Object[] propertyValues = bulkBean.getPropertyValues(t);
        String[] strings = new String[header.length];
        for (int i = 0; i < header.length; i++) {
            strings[i] = converters[i].apply(propertyValues[i]);
        }
        return strings;
    }

    public String[] getHeader() {
        return header;
    }
}
