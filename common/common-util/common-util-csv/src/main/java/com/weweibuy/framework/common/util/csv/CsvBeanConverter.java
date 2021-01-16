package com.weweibuy.framework.common.util.csv;

import de.siegmar.fastcsv.reader.CsvRow;

import java.util.Map;

/**
 * @author durenhao
 * @date 2019/8/20 10:47
 **/
@FunctionalInterface
public interface CsvBeanConverter<T> {

    /**
     * csv 内容转化
     *
     * @param nameIndexMap header
     * @param csvRow       csvRow
     * @return
     */
    T convert(Map<String, Integer> nameIndexMap, CsvRow csvRow);


}
