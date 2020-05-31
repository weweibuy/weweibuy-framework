package com.weiweibuy.framework.common.core.model.constant;

import java.time.format.DateTimeFormatter;

/**
 * @author durenhao
 * @date 2020/2/27 20:56
 **/
public interface CommonConstant {

    String NONE_VIEW_STR = "N/A";

    public interface DateConstant {

        String TIME_OFFSET_ID = "+8";

        String STANDARD_DATE_TIME_FORMAT_STR = "yyyy-MM-dd HH:mm:ss";

        String STANDARD_DATE_FORMAT_STR = "yyyy-MM-dd";

        DateTimeFormatter STANDARD_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(STANDARD_DATE_TIME_FORMAT_STR);

        DateTimeFormatter STANDARD_DATE_FORMATTER = DateTimeFormatter.ofPattern(STANDARD_DATE_FORMAT_STR);


    }


    public interface CharsetConstant {

        String UTF8_STR = "UTF-8";


    }

    public interface SignConstant {

        public static final String HMAC_SHA256 = "HmacSHA256";


    }


}
