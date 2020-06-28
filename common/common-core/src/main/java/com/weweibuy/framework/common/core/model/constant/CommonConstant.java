package com.weweibuy.framework.common.core.model.constant;

import java.time.format.DateTimeFormatter;

/**
 * @author durenhao
 * @date 2020/2/27 20:56
 **/
public interface CommonConstant {

    /**
     * 控制符
     */
    String NONE_VIEW_STR = "N/A";

    /**
     * 赋值符
     */
    String ASSIGNOR_STR = "=";

    /**
     * 连接符
     */
    String CONNECTOR_STR = "&";

    /**
     * 逗号
     */
    String COMMA_STR = ",";

    /**
     * 下划线
     */
    String UNDERLINE_STR = "_";

    /**
     * null  String
     */
    String NULL_STR = "null";

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

    /**
     * Http常量
     */
    public interface HttpServletConstant {

        String REQUEST_PATH = "http_request_path";

        /**
         * 请求方法
         */
        String REQUEST_METHOD = "http_request_method";

        /**
         * Content-Type
         */
        String REQUEST_CONTENT_TYPE = "http_request_content_type";


        /**
         * query string
         */
        String REQUEST_PARAMETER_MAP = "http_request_parameter_map";

        /**
         * 请求时间戳
         */
        String REQUEST_TIMESTAMP = "http_request_timestamp";


    }


}
