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
    String writeConvert(T t);

    /**
     * 转字段
     *
     * @param value
     * @param fieldType
     * @return
     */
    T readConvert(String value, Class<T> fieldType, int typeIndex);

    /**
     * converter 对应的类型索引; 如果不支持索引返回-1
     *
     * @param fieldType
     * @return
     */
    int typeIndex(Class<T> fieldType);

    /**
     * 设置时间格式
     *
     * @param pattern
     */
    void setPattern(String pattern);


}
