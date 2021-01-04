package com.weweibuy.framework.common.util.csv;

import java.util.Collection;
import java.util.List;

/**
 * @author durenhao
 * @date 2019/8/20 10:47
 **/
@FunctionalInterface
public interface CsvContentConverter<T> {

    /**
     * csv 内容转化
     *
     * @param header
     * @param body
     * @return
     */
    Collection<String[]> convert(String[] header, List<T> body);


}
