package com.weweibuy.framework.common.util.csv;

import com.weweibuy.framework.common.core.utils.PredicateEnhance;
import com.weweibuy.framework.common.util.csv.annotation.CsvProperty;
import de.siegmar.fastcsv.reader.CsvRow;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.cglib.beans.BulkBean;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 读CSV
 * <p>
 * CSV 数据转 Java bean  非线程安全
 *
 * @author durenhao
 * @date 2021/1/5 22:22
 **/
public class ReflectCsvBeanConverter<T> implements CsvBeanConverter<T> {

    private boolean hasInit = false;

    private final Class<? extends T> type;

    private List<CsvReadListener<T>> listener;

    private BulkBean bulkBean;

    private Integer[] fieldIndex;

    private CsvTypeConverter[] converters;

    private Integer[] converterTypeIndex;

    private Class[] types;

    public ReflectCsvBeanConverter(Class<? extends T> type) {
        this(type, null);
    }

    public ReflectCsvBeanConverter(Class<? extends T> type, List<CsvReadListener<T>> listener) {
        this.type = type;
        this.listener = listener;
    }

    public ReflectCsvBeanConverter(Class<? extends T> type, List<CsvReadListener<T>> listener, List<String> header) {
        this.type = type;
        this.listener = listener;
        init(header);
    }


    private void init(List<String> header) {
        Map<String, Integer> headIndexMap = CsvUtils.headIndexMap(header);
        Field[] fieldsWithAnnotation = FieldUtils.getFieldsWithAnnotation(type, CsvProperty.class);
        validate(fieldsWithAnnotation);

        Map<Boolean, Set<Field>> fieldMap = Arrays.stream(fieldsWithAnnotation)
                .collect(Collectors.groupingBy(f -> f.getAnnotation(CsvProperty.class).index() != Integer.MAX_VALUE,
                        Collectors.toSet()));

        Set<Field> userIndexList = fieldMap.get(true);
        Set<Field> userNameList = fieldMap.get(false);

        //  length = 使用index 属性  + 名称包含的属性 总个数
        int length = usedFieldLength(fieldMap, headIndexMap);

        fieldIndex = new Integer[length];
        converters = new CsvTypeConverter[length];
        String[] getters = new String[length];
        String[] setters = new String[length];
        types = new Class[length];
        converterTypeIndex = new Integer[length];

        AtomicInteger indexAtomicInteger = new AtomicInteger(0);

        // 使用index的字段
        PredicateEnhance.of(userIndexList)
                .withPredicate(CollectionUtils::isNotEmpty)
                .trueThenConsumer(list -> list.stream()
                        .peek(field -> setBulkBeanInfo(field, getters, setters, types, converters, indexAtomicInteger))
                        .map(field -> field.getAnnotation(CsvProperty.class))
                        .forEach(annotation -> fieldIndex[indexAtomicInteger.getAndIncrement()] = annotation.index()));

        PredicateEnhance.of(userNameList)
                .withPredicate(CollectionUtils::isNotEmpty)
                .trueThenConsumer(list -> list.stream()
                        .map(field -> {
                            Integer index = headIndexMap.get(field.getAnnotation(CsvProperty.class).name());
                            if (index != null) {
                                setBulkBeanInfo(field, getters, setters, types, converters, indexAtomicInteger);
                            }
                            return index;
                        })
                        .filter(Objects::nonNull)
                        .forEach(index -> fieldIndex[indexAtomicInteger.getAndIncrement()] = index));
        bulkBean = BulkBean.create(type, getters, setters, types);
        hasInit = true;
    }


    private void validate(Field[] fieldsWithAnnotation) {
        if (ArrayUtils.isEmpty(fieldsWithAnnotation)) {
            throw new IllegalArgumentException(type.getName() + "没有 @CsvProperty 标记的属性");
        }
        // 校验
        boolean match = Arrays.stream(fieldsWithAnnotation)
                .map(field -> field.getAnnotation(CsvProperty.class))
                .anyMatch(annotation -> annotation.index() != Integer.MAX_VALUE && StringUtils.isNotBlank(annotation.name()));
        if (match) {
            throw new IllegalArgumentException("读取Csv, @CsvProperty, index属性与name属性不能同时选择");
        }
    }


    private Integer usedFieldLength(Map<Boolean, Set<Field>> listMap, Map<String, Integer> headIndexMap) {
        //  length = 使用index 属性的 + 名称包含的
        Set<Field> userIndexList = listMap.get(true);
        Set<Field> userNameList = listMap.get(false);
        int length = 0;
        if (CollectionUtils.isNotEmpty(userIndexList)) {
            length += userIndexList.size();
        }
        if (CollectionUtils.isNotEmpty(userNameList)) {
            length += userNameList.stream()
                    .filter(field ->
                            headIndexMap.get(field.getAnnotation(CsvProperty.class).name()) != null)
                    .collect(Collectors.toList())
                    .size();
        }
        return length;
    }


    @Override
    public T convert(List<String> header, CsvRow csvRow) {
        if (!hasInit) {
            init(header);
        }

        T instance = newInstance();
        if (fieldIndex.length == 0) {
            return instance;
        }
        List<String> fieldList = csvRow.getFields();
        int size = fieldList.size();
        int length = fieldIndex.length;
        Object[] values = new Object[length];
        Object value = null;
        Integer csvIndex = null;
        for (int i = 0; i < length; i++) {
            csvIndex = fieldIndex[i];
            if (csvIndex < size) {
                value = converters[i].readConvert(fieldList.get(csvIndex), types[i], converterTypeIndex[i]);
            }
            values[i] = value;
        }
        bulkBean.setPropertyValues(instance, values);
        if (CollectionUtils.isNotEmpty(listener)) {
            listener.forEach(l -> l.onOneLineRead(header, instance, csvRow));
        }
        return instance;
    }


    private T newInstance() {
        try {
            return type.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException(type.getName() + " 必须含有公共空参构造");
        }
    }

    private void setBulkBeanInfo(Field field, String[] getters, String[] setters, Class[] types, CsvTypeConverter[] converters,
                                 AtomicInteger indexAtomicInteger) {
        getters[indexAtomicInteger.get()] = Utils.fieldGetter(field);
        setters[indexAtomicInteger.get()] = Utils.fieldSetter(field);
        types[indexAtomicInteger.get()] = field.getType();
        converters[indexAtomicInteger.get()] = Utils.typeConverter(field.getAnnotation(CsvProperty.class).converter(), field.getAnnotation(CsvProperty.class).pattern(), field.getType());
        converterTypeIndex[indexAtomicInteger.get()] = converters[indexAtomicInteger.get()].typeIndex(field.getType());

    }


}
