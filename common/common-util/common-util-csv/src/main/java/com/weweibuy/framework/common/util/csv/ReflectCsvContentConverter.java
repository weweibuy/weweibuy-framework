package com.weweibuy.framework.common.util.csv;

import lombok.AllArgsConstructor;

import java.util.Collection;
import java.util.List;

/**
 * @author durenhao
 * @date 2021/1/5 22:22
 **/
@AllArgsConstructor
public class ReflectCsvContentConverter<T> implements CsvContentConverter<T> {

    private final Class<?> clazz;

    @Override
    public Collection<String[]> convert(String[] header, List<T> body) {
        return null;
    }


}
