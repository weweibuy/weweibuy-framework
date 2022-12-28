package com.weweibuy.framework.common.es.converter;

import com.weweibuy.framework.common.core.utils.DateTimeUtils;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;

/**
 * @author durenhao
 * @date 2021/6/19 17:14
 **/
public class StrToLocalDateConverter implements Converter<String, LocalDate> {

    @Override
    public LocalDate convert(String source) {
        return DateTimeUtils.stringToLocalDate(source);
    }
}
