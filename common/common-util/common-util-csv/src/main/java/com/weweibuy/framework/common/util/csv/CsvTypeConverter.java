package com.weweibuy.framework.common.util.csv;

/**
 * 类型转化
 *
 * @author durenhao
 * @date 2021/1/14 21:17
 **/
public interface CsvTypeConverter<T> {


    String convert(T t);


}
