package com.weweibuy.framework.common.util.csv;

import de.siegmar.fastcsv.reader.CsvRow;

import java.util.List;

/**
 * @author durenhao
 * @date 2023/6/16 18:39
 **/
public abstract class AbstractCsvBeanConverter<T> implements CsvBeanConverter<T> {

    private List<CsvReadListener> csvReadListenerList;


    public void init(List<String> header) {

    }

    @Override
    public T convert(List<String> header, CsvRow csvRow) {

        return null;
    }
}
