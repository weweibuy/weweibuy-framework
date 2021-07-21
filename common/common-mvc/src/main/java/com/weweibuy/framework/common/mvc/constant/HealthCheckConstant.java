package com.weweibuy.framework.common.mvc.constant;

import com.weweibuy.framework.common.core.model.constant.CommonConstant;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author durenhao
 * @date 2020/9/25 21:17
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HealthCheckConstant {

    public static final String HEALTH_CHECK_PATH = CommonConstant.HttpServletConstant.ENDPOINT_PATH_PREFIX
            + "/_common/_health";

}
