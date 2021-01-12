package com.weweibuy.framework.common.util.csv;

import java.util.Collection;
import java.util.List;

/**
 * @author durenhao
 * @date 2021/1/5 22:22
 **/
public class ReflectCsvContentConverter implements CsvContentConverter {

    private final Class clazz;

    public ReflectCsvContentConverter(Class clazz) {
        this.clazz = clazz;
    }

    @Override
    public Collection<String[]> convert(String[] header, List body) {


        return null;
    }


}
