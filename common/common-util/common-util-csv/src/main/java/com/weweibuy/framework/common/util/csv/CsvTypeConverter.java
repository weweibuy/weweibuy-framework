package com.weweibuy.framework.common.util.csv;

/**
 * 类型转化
 *
 * @author durenhao
 * @date 2021/1/14 21:17
 **/
public interface CsvTypeConverter<T> {

    /**
     * 转CSV单元格内容
     *
     * @param t
     * @return
     */
    String convert(T t);

    /**
     * 转字段
     *
     * @param value
     * @param fieldType
     * @return
     */
    T convert(String value, Class<T> fieldType);


}
