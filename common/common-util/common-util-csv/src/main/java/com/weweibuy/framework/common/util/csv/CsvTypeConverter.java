package com.weweibuy.framework.common.util.csv;

import com.weweibuy.framework.common.util.csv.annotation.CsvProperty;

import java.util.function.Function;

/**
 * 类型转化
 *
 * @author durenhao
 * @date 2021/1/14 21:17
 **/
public interface CsvTypeConverter<T> {


    Function<Object, String> writeConvert(Class fieldType, CsvProperty csvProperty);


    Function<String, Object> readConvert(Class fieldType, CsvProperty csvProperty);


}
