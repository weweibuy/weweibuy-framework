package com.weweibuy.framework.common.util.csv;

import de.siegmar.fastcsv.reader.CsvRow;

import java.util.List;

/**
 * @author durenhao
 * @date 2019/8/20 10:47
 **/
@FunctionalInterface
public interface CsvReaderConverter<T> {

    /**
     * csv 内容转化
     *
     * @param header header
     * @param csvRow       csvRow
     * @return
     */
    T convert(List<String> header, CsvRow csvRow);


}
