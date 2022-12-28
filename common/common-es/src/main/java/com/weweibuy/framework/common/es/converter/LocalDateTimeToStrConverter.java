package com.weweibuy.framework.common.es.converter;

import com.weweibuy.framework.common.core.utils.DateTimeUtils;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDateTime;

/**
 * @author durenhao
 * @date 2021/6/19 17:14
 **/
public class LocalDateTimeToStrConverter implements Converter<LocalDateTime, String> {

    @Override
    public String convert(LocalDateTime source) {
        return DateTimeUtils.toStringDate(source);
    }
}
