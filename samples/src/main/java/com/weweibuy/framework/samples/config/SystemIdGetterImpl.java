package com.weweibuy.framework.samples.config;

import com.weweibuy.framework.common.core.support.SystemIdGetter;
import org.springframework.stereotype.Component;

/**
 * @author durenhao
 * @date 2020/9/6 15:43
 **/
@Component
public class SystemIdGetterImpl implements SystemIdGetter {

    @Override
    public String getSystemId() {
        return "01";
    }
}
