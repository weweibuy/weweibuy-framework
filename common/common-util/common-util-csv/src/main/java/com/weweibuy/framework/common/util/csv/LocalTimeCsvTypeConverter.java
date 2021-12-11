package com.weweibuy.framework.common.util.csv;

import com.weweibuy.framework.common.core.model.constant.CommonConstant;
import com.weweibuy.framework.common.core.utils.DateTimeUtils;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalTime;
import java.util.Optional;

/**
 * @author durenhao
 * @date 2021/1/15 17:55
 **/
public class LocalTimeCsvTypeConverter implements CsvTypeConverter<LocalTime> {

    private String pattern;

    @Override
    public String convert(LocalTime localTime) {
        return Optional.ofNullable(localTime)
                .map(d -> DateTimeUtils.toStringDate(d, pattern))
                .orElse(StringUtils.EMPTY);
    }

    @Override
    public LocalTime convert(String value, Class<LocalTime> fieldType, int typeIndex) {
        if (StringUtils.EMPTY.equals(value)) {
            return null;
        }
        return DateTimeUtils.stringToLocalTime(value, pattern);
    }

    @Override
    public int typeIndex(Class<LocalTime> fieldType) {
        return -1;
    }

    @Override
    public void setPattern(String pattern) {
        this.pattern = Optional.ofNullable(pattern)
                .filter(StringUtils::isNotBlank)
                .orElse(CommonConstant.DateConstant.STANDARD_TIME_FORMAT_STR);
    }

}
