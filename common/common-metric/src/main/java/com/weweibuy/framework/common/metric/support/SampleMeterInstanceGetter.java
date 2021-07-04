package com.weweibuy.framework.common.metric.support;

import com.weweibuy.framework.common.core.utils.IpUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Optional;

/**
 * @author durenhao
 * @date 2021/7/4 10:05
 **/
@RequiredArgsConstructor
public class SampleMeterInstanceGetter implements MeterInstanceGetter {

    private final Environment environment;


    @Override
    public String getInstance() {
        String port = Optional.ofNullable(environment.getProperty("server.port"))
                .orElse("8080");
        String localIP = null;
        try {
            localIP = IpUtils.getLocalIP();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        if (StringUtils.isBlank(localIP)) {
            localIP = "127.0.0.1";
        }
        return localIP + ":" + port;
    }


}
