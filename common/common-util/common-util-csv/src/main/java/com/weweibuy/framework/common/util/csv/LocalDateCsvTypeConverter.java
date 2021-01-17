package com.weweibuy.framework.common.util.csv;

import com.weweibuy.framework.common.core.utils.DateTimeUtils;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.util.Optional;

/**
 * @author durenhao
 * @date 2021/1/15 17:55
 **/
public class LocalDateCsvTypeConverter implements CsvTypeConverter<LocalDate> {


    @Override
    public String convert(LocalDate localDate) {
        return Optional.ofNullable(localDate)
                .map(DateTimeUtils::toStringDate)
                .orElse(StringUtils.EMPTY);
    }

    @Override
    public LocalDate convert(String value, Class<LocalDate> fieldType, int typeIndex) {
        if (StringUtils.EMPTY.equals(value)) {
            return null;
        }
        return DateTimeUtils.stringToLocalDate(value);
    }

    @Override
    public int typeIndex(Class<LocalDate> fieldType) {
        return -1;
    }

}
