package com.weweibuy.framework.samples.config;

import com.weweibuy.framework.common.feign.support.FeignLogConfigurer;
import com.weweibuy.framework.common.feign.support.FeignLogSetting;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * @author durenhao
 * @date 2021/10/30 21:49
 **/
@Component
public class CustomFeignLogConfigurer implements FeignLogConfigurer {


    @Override
    public void configurer(List<FeignLogSetting> feignLogSettingList) {
        feignLogSettingList.add(FeignLogSetting.builder()
                .host("localhost:9000")
                .reqHeaderList(Collections.singletonList("token"))
                .respHeaderList(Collections.singletonList(HttpHeaders.CONTENT_TYPE))
                .build());
    }
}
