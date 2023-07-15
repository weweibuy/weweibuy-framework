package com.weweibuy.framework.common.util.csv;

import de.siegmar.fastcsv.reader.CsvRow;

import java.util.List;

/**
 * @author durenhao
 * @date 2021/1/16 22:29
 **/
public interface CsvReadListener<T> {

    /**
     * 行被读取
     *
     * @param header
     * @param csvRow
     */
    void onOneLineRead(List<String> header, CsvRow csvRow);

    /**
     * java 实体创建, 并赋值完成
     *
     * @param t      实例
     * @param header
     * @param csvRow
     */
    void onInstanceCreate(T t, List<String> header, CsvRow csvRow);

}
