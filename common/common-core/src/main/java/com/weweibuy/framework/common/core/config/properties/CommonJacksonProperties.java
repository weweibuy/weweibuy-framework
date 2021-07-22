package com.weweibuy.framework.common.core.config.properties;

import com.weweibuy.framework.common.core.model.constant.CommonConstant;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author durenhao
 * @date 2021/7/22 21:08
 **/
@Data
@ConfigurationProperties(prefix = "common.jackson")
public class CommonJacksonProperties {

    /**
     * 时间格式化
     */
    private String localDateFormat = CommonConstant.DateConstant.STANDARD_DATE_FORMAT_STR;

    /**
     * 时间日期格式化
     */
    private String localDateTimeFormat = CommonConstant.DateConstant.STANDARD_DATE_TIME_FORMAT_STR;

}
