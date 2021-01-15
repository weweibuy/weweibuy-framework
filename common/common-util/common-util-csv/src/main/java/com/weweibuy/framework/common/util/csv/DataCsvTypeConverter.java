package com.weweibuy.framework.common.util.csv;

import com.weweibuy.framework.common.core.utils.DateTimeUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.Optional;

/**
 * @author durenhao
 * @date 2021/1/15 17:53
 **/
public class DataCsvTypeConverter implements CsvTypeConverter<Date> {

    @Override
    public String convert(Date date) {
        return Optional.ofNullable(date)
                .map(DateTimeUtils::toStringDate)
                .orElse(StringUtils.EMPTY);
    }
}
