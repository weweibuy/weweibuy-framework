package com.weweibuy.framework.common.log.desensitization;

import com.weweibuy.framework.common.log.constant.LogSensitizationEum;

import java.util.Set;

/**
 * @author durenhao
 * @date 2020/6/6 18:06
 **/
public interface SensitizationMapping {

    LogSensitizationEum getType();

    String getLogger();

    Set<String> getSensitizationField();

}
