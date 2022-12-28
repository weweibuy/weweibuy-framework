package com.weweibuy.framework.common.es.converter;

import com.weweibuy.framework.common.core.utils.DateTimeUtils;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * @author durenhao
 * @date 2021/6/19 17:14
 **/
public class LocalTimeToStrConverter implements Converter<LocalTime, String> {

    @Override
    public String convert(LocalTime source) {
        return DateTimeUtils.toStringDate(source);
    }
}
