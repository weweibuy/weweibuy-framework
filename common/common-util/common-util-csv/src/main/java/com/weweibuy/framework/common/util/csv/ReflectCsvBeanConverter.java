package com.weweibuy.framework.common.util.csv;

import com.weweibuy.framework.common.util.csv.annotation.CsvProperty;
import de.siegmar.fastcsv.reader.CsvRow;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.cglib.beans.BulkBean;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author durenhao
 * @date 2021/1/5 22:22
 **/
public class ReflectCsvBeanConverter<T> implements CsvBeanConverter<T> {

    private final Class<? extends T> type;

    private BulkBean bulkBean;

    private Integer[] fieldIndex;

    private CsvTypeConverter[] converters;

    private Class[] types;

    public ReflectCsvBeanConverter(Class<? extends T> type, Map<String, Integer> headIndexMap) {
        this.type = type;
        init(headIndexMap);
    }

    private void init(Map<String, Integer> headIndexMap) {
        Field[] fieldsWithAnnotation = FieldUtils.getFieldsWithAnnotation(type, CsvProperty.class);

        // 校验
        boolean match = Arrays.stream(fieldsWithAnnotation)
                .map(field -> field.getAnnotation(CsvProperty.class))
                .anyMatch(annotation -> annotation.index() != Integer.MAX_VALUE && StringUtils.isBlank(annotation.name()));
        if (match) {
            throw new IllegalArgumentException("读取Csv, @CsvProperty, index属性与name属性不能同时选择");
        }
        // TODO 长度问题
        int length = fieldsWithAnnotation.length;
        fieldIndex = new Integer[length];
        converters = new CsvTypeConverter[length];

        String[] getters = new String[length];
        String[] setters = new String[length];
        types = new Class[length];

        List<String> nameList = new ArrayList<>();

        Map<Boolean, List<Field>> listMap = Arrays.stream(fieldsWithAnnotation)
                .collect(Collectors.groupingBy(f ->
                        f.getAnnotation(CsvProperty.class).index() != Integer.MAX_VALUE));

        List<Field> userIndexList = listMap.get(true);
        List<Field> userNameList = listMap.get(false);
        AtomicInteger indexAtomicInteger = new AtomicInteger(0);
        if (CollectionUtils.isNotEmpty(userIndexList)) {
            // 注解中直接指定index
            userIndexList.stream()
                    .peek(field -> setBulkBeanInfo(field, getters, setters, types, indexAtomicInteger))
                    .map(field -> field.getAnnotation(CsvProperty.class))
                    .forEach(annotation -> fieldIndex[indexAtomicInteger.getAndIncrement()] = annotation.index());
        }

        if (CollectionUtils.isNotEmpty(userNameList)) {
            // 通过名称匹配index
            userNameList.stream()
                    .map(field -> {
                        Integer index = headIndexMap.get(field.getAnnotation(CsvProperty.class).name());
                        if (index != null) {
                            setBulkBeanInfo(field, getters, setters, types, indexAtomicInteger);
                        }
                        return index;
                    })
                    .filter(Objects::nonNull)
                    .forEach(index -> fieldIndex[indexAtomicInteger.getAndIncrement()] = index);
        }
        bulkBean = BulkBean.create(type, getters, setters, types);
    }


    @Override
    public T convert(Map<String, Integer> nameIndexMap, CsvRow csvRow) {
        List<String> fieldList = csvRow.getFields();
        int size = fieldList.size();
        int length = fieldIndex.length;
        Object[] values = new Object[length];
        Object value = null;
        Integer csvIndex = null;
        for (int i = 0; i < length; i++) {
            csvIndex = fieldIndex[i];
            if (csvIndex < size) {
                value = converters[i].convert(fieldList.get(csvIndex), types[i]);
            }
            values[i] = value;
        }
        T instance = newInstance();
        bulkBean.setPropertyValues(instance, values);
        return instance;
    }


    private T newInstance() {
        try {
            return type.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException(type.getName() + " 必须含有公共空参构造");
        }
    }

    private void setBulkBeanInfo(Field field, String[] getters, String[] setters, Class[] types,
                                 AtomicInteger indexAtomicInteger) {
        getters[indexAtomicInteger.get()] = Utils.fieldGetter(field);
        setters[indexAtomicInteger.get()] = Utils.fieldSetter(field);
        types[indexAtomicInteger.get()] = field.getType();
        converters[indexAtomicInteger.get()] = Utils.typeConverter(field.getAnnotation(CsvProperty.class).converter(), field.getType());
    }

}
