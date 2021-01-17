package com.weweibuy.framework.common.util.csv;

import com.weweibuy.framework.common.core.utils.DateTimeUtils;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author durenhao
 * @date 2021/1/15 17:55
 **/
public class LocalDateTimeCsvTypeConverter implements CsvTypeConverter<LocalDateTime> {

    @Override
    public String convert(LocalDateTime localDateTime) {
        return Optional.ofNullable(localDateTime)
                .map(DateTimeUtils::toStringDate)
                .orElse(StringUtils.EMPTY);
    }

    @Override
    public LocalDateTime convert(String value, Class<LocalDateTime> fieldType, int typeIndex) {
        if (StringUtils.EMPTY.equals(value)) {
            return null;
        }
        return DateTimeUtils.stringToLocalDateTime(value);
    }

    @Override
    public int typeIndex(Class<LocalDateTime> fieldType) {
        return -1;
    }

}
