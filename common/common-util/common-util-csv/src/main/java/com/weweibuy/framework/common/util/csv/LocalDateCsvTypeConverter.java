package com.weweibuy.framework.common.util.csv;

import com.weweibuy.framework.common.core.model.constant.CommonConstant;
import com.weweibuy.framework.common.core.utils.DateTimeUtils;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.util.Optional;

/**
 * @author durenhao
 * @date 2021/1/15 17:55
 **/
public class LocalDateCsvTypeConverter implements CsvTypeConverter<LocalDate> {

    private String pattern;

    @Override
    public String convert(LocalDate localDate) {
        return Optional.ofNullable(localDate)
                .map(d -> DateTimeUtils.toStringDate(d, pattern))
                .orElse(StringUtils.EMPTY);
    }

    @Override
    public LocalDate convert(String value, Class<LocalDate> fieldType, int typeIndex) {
        if (StringUtils.EMPTY.equals(value)) {
            return null;
        }
        return DateTimeUtils.stringToLocalDate(value, pattern);
    }

    @Override
    public int typeIndex(Class<LocalDate> fieldType) {
        return -1;
    }

    @Override
    public void setPattern(String pattern) {
        this.pattern = Optional.ofNullable(pattern)
                .filter(StringUtils::isNotBlank)
                .orElse(CommonConstant.DateConstant.STANDARD_DATE_FORMAT_STR);
    }

}
