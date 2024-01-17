
package com.weweibuy.framework.common.feign.config;

import com.weweibuy.framework.common.feign.log.FeignLogger;
import com.weweibuy.framework.common.feign.support.FeignFilter;
import com.weweibuy.framework.common.feign.support.FeignLogConfigurer;
import com.weweibuy.framework.common.feign.support.FeignLogSetting;
import com.weweibuy.framework.common.feign.support.LogFeignFilter;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author durenhao
 * @date 2022/10/15 11:45
 **/
@Slf4j
@AutoConfiguration
@ConditionalOnProperty(prefix = "common.feign.log", name = "position", havingValue = "feign")
public class FeignLogConfig {

    @Autowired(required = false)
    private List<FeignLogConfigurer> feignLogConfigurerList;

    @Bean
    public FeignLogger feignLogger() {
        List<FeignLogSetting> arrayList = new ArrayList<>();
        Optional.ofNullable(feignLogConfigurerList)
                .ifPresent(l -> l.forEach(f -> f.configurer(arrayList)));
        List<FeignLogSetting> settings = arrayList.stream()
                .collect(Collectors.toList());

        return new FeignLogger(settings);
    }

    @Bean
    public FeignFilter logFeignFilter() {
        return new LogFeignFilter();
    }


    public static void main(String[] args) {

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            IntStream.range(0, 10000).forEach(i -> {
                executor.submit(() -> {
                    Thread.sleep(Duration.ofSeconds(1));
                    log.info(Thread.currentThread().getName() + " : xxx");
                    return i;
                });
            });
        }


        String result = get("http://httpbin.org/get");
        System.out.println(result);
    }

    public static String get(String url) {

        String resultContent = null;
        HttpGet httpGet = new HttpGet(url);
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            try (CloseableHttpResponse response = httpclient.execute(httpGet)) {
                // 获取状态码
                System.out.println(response.getVersion()); // HTTP/1.1
                System.out.println(response.getCode()); // 200
                System.out.println(response.getReasonPhrase()); // OK
                HttpEntity entity = response.getEntity();
                // 获取响应信息
                resultContent = EntityUtils.toString(entity);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return resultContent;
    }

}
