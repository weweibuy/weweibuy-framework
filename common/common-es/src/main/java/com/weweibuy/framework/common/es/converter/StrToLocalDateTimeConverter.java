package com.weweibuy.framework.common.es.converter;

import com.weweibuy.framework.common.core.utils.DateTimeUtils;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDateTime;

/**
 * @author durenhao
 * @date 2021/6/19 17:14
 **/
public class StrToLocalDateTimeConverter implements Converter<String, LocalDateTime> {

    @Override
    public LocalDateTime convert(String source) {
        return DateTimeUtils.stringToLocalDateTime(source);
    }
}
