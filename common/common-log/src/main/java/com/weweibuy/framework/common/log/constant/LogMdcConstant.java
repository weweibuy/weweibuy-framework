package com.weweibuy.framework.common.log.constant;

/**
 * @author durenhao
 * @date 2020/3/1 21:33
 **/
public interface LogMdcConstant {

    /**
     * 请求路径
     */
    String REQUEST_PATH = "requestPath";

    /**
     * 请求时间戳
     */
    String REQUEST_TIMESTAMP = "requestTimestamp";

    String REQUEST_METHOD = "requestMethod";

    String REQUEST_QUERY_STRING = "requestQueryString";

    String LOG_SENSITIZATION = "logSensitization";

    /**
     * 敏感字段
     */
    String SENSITIZATION_FIELDS = "sensitization_fields";

    /**
     * 脱敏logger
     */
    String SENSITIZATION_LOGGER = "sensitization_logger";

    String TID_FIELD_NAME = "tid";

    String UID_FIELD_NAME = "uid";



}
