package com.weweibuy.framework.common.util.csv;

import com.weweibuy.framework.common.util.csv.annotation.CsvProperty;

/**
 * @author durenhao
 * @date 2023/6/19 16:55
 **/
public interface CsvPropertyDataConvert<T, R> {

    R convert(T data, CsvProperty csvProperty);

}
