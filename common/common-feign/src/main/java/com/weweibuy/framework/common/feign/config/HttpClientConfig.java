package com.weweibuy.framework.common.feign.config;

import com.weweibuy.framework.common.core.concurrent.LogExceptionThreadFactory;
import com.weweibuy.framework.common.feign.support.CustomHttpClientLogInterceptor;
import com.weweibuy.framework.common.feign.support.DelegateFeignClient;
import feign.Client;
import feign.hc5.ApacheHttp5Client;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.core5.http.HttpRequestInterceptor;
import org.apache.hc.core5.http.HttpResponseInterceptor;
import org.apache.hc.core5.util.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * httpclient 配置
 *
 * @author durenhao
 * @date 2019/10/29 10:51
 **/
@AutoConfiguration
@EnableConfigurationProperties(HttpClientProperties.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class HttpClientConfig {

    @Autowired
    private HttpClientProperties httpClientProperties;

    @Autowired(required = false)
    private List<DelegateFeignClient> delegateFeignClientList;

    @Autowired(required = false)
    private CustomHttpClientLogInterceptor customHttpClientLogInterceptor;

    private ScheduledExecutorService schedule;

    /**
     * LB 时 {@link 'HttpClientFeignLoadBalancedConfiguration'}
     *
     * @param httpClient
     * @return
     */
    @Bean
    @ConditionalOnMissingClass(value = {"org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory"})
    public Client feignClient(HttpClient httpClient) {
        Client client = new ApacheHttp5Client(httpClient);
        return DelegateFeignClient.delegateChain(delegateFeignClientList, client);
    }


    @Bean
    public CloseableHttpClient httpClient() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {

        HttpClientBuilder httpClientBuilder = HttpClients.custom()
                .setConnectionManager(httpClientConnectionManager())
                .disableAutomaticRetries()
                .setDefaultRequestConfig(requestConfig());
        // 日志
        if (customHttpClientLogInterceptor != null) {
            httpClientBuilder = httpClientBuilder
                    .addRequestInterceptorLast((HttpRequestInterceptor) customHttpClientLogInterceptor)
                    .addResponseInterceptorLast((HttpResponseInterceptor) customHttpClientLogInterceptor);
        }

        return httpClientBuilder.build();
    }


    /**
     * feign 的 超时配置会作为每次请求的 option,
     * 因为feign有默认的超时配置,所以此处的超时配置 只在单独使用 httpclient 时生效
     *
     * @return
     */
    public RequestConfig requestConfig() {
        return RequestConfig.custom().setConnectTimeout(Timeout.of(httpClientProperties.getConnectTimeout(), TimeUnit.MILLISECONDS))
                .setConnectionRequestTimeout(Timeout.of(httpClientProperties.getConnectionRequestTimeout(), TimeUnit.MILLISECONDS))
                .setResponseTimeout(Timeout.of(httpClientProperties.getSocketTimeout(), TimeUnit.MILLISECONDS))
                .build();
    }


    private synchronized HttpClientConnectionManager httpClientConnectionManager() {

        PoolingHttpClientConnectionManager connectionManager = poolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(httpClientProperties.getMaxTotal());
        connectionManager.setDefaultMaxPerRoute(httpClientProperties.getDefaultMaxPerRoute());
        if (this.schedule == null) {
            this.schedule = new ScheduledThreadPoolExecutor(1,
                    new LogExceptionThreadFactory("close-expired-schedule-"),
                    new ThreadPoolExecutor.DiscardPolicy());
            this.schedule.scheduleWithFixedDelay(connectionManager::closeExpired,
                    30000, httpClientProperties.getCheckExpiredConnectionInterval(), TimeUnit.MILLISECONDS);
        }
        return connectionManager;
    }

    private PoolingHttpClientConnectionManager poolingHttpClientConnectionManager() {
//        if (!httpClientProperties.getSwitchNodeWhenConnectionTimeout()) {
//            // 控制httpclient connection-timeout 时不切换节点重试
//            return new PoolingHttpClientConnectionManager(
//                    new NoSwitchHttpClientConnectionOperator(getDefaultRegistry(), null, null),
//                    null,
//                    httpClientProperties.getMaxLifeTime(),
//                    TimeUnit.MICROSECONDS);
//        }
        return PoolingHttpClientConnectionManagerBuilder.create()
                .setMaxConnPerRoute(httpClientProperties.getDefaultMaxPerRoute())
                .setMaxConnTotal(httpClientProperties.getMaxTotal())
                .setDefaultConnectionConfig(connectionConfig())
                .build();
    }


    private ConnectionConfig connectionConfig() {
        return ConnectionConfig.custom()
                .setTimeToLive(Timeout.of(httpClientProperties.getMaxLifeTime(), TimeUnit.MILLISECONDS))
                .setValidateAfterInactivity(Timeout.of(httpClientProperties.getValidateAfterInactivitySec(), TimeUnit.SECONDS))
                .build();
    }


//    @Bean
//    @ConditionalOnClass(name = {"org.apache.http.impl.client.CloseableHttpClient"})
//    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
//        ExtractingResponseErrorHandler errorHandler = new ExtractingResponseErrorHandler();
//
//        HttpComponentsClientHttpRequestFactory requestFactory =
//                new HttpComponentsClientHttpRequestFactory(httpClient());
//
//        return restTemplateBuilder.errorHandler(errorHandler)
//                .requestFactory(() -> requestFactory)
//                .build();
//
//    }


}
