package com.weweibuy.framework.common.util.csv;

import de.siegmar.fastcsv.reader.CsvRow;

/**
 * @author durenhao
 * @date 2021/1/16 22:29
 **/
public interface CsvReadListener<T> {


    void onOneLineRead(T t, CsvRow csvRow);


}
