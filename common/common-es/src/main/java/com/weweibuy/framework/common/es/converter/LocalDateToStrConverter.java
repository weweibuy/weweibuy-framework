package com.weweibuy.framework.common.es.converter;

import com.weweibuy.framework.common.core.utils.DateTimeUtils;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author durenhao
 * @date 2021/6/19 17:14
 **/
public class LocalDateToStrConverter implements Converter<LocalDate, String> {

    @Override
    public String convert(LocalDate source) {
        return DateTimeUtils.toStringDate(source, DateTimeFormatter.ISO_LOCAL_DATE);
    }
}
