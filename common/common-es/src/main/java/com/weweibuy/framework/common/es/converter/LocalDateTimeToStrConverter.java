package com.weweibuy.framework.common.es.converter;

import com.weweibuy.framework.common.core.utils.DateTimeUtils;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author durenhao
 * @date 2021/6/19 17:14
 **/
public class LocalDateTimeToStrConverter implements Converter<LocalDateTime, String> {

    @Override
    public String convert(LocalDateTime source) {
        return DateTimeUtils.toStringDate(source, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}
