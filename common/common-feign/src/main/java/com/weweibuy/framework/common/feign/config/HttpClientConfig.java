package com.weweibuy.framework.common.feign.config;

import com.weweibuy.framework.common.core.concurrent.LogExceptionThreadFactory;
import com.weweibuy.framework.common.feign.support.CustomHttpClientLogInterceptor;
import com.weweibuy.framework.common.feign.support.DelegateFeignClient;
import com.weweibuy.framework.common.feign.support.NoSwitchHttpClientConnectionOperator;
import feign.Client;
import feign.httpclient.ApacheHttpClient;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultHttpClientConnectionOperator;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.ExtractingResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
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
        Client client = new ApacheHttpClient(httpClient);
        client = DelegateFeignClient.delegateChain(delegateFeignClientList, client);
        return client;
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
                    .addInterceptorLast((HttpRequestInterceptor) customHttpClientLogInterceptor)
                    .addInterceptorFirst((HttpResponseInterceptor) customHttpClientLogInterceptor);
        }

        if (httpClientProperties.isUseSSL()) {
            TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
            SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
                    .loadTrustMaterial(null, acceptingTrustStrategy)
                    .build();
            SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);
            httpClientBuilder.setSSLSocketFactory(csf);

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
        return RequestConfig.custom().setConnectTimeout(httpClientProperties.getConnectTimeout())
                .setConnectionRequestTimeout(httpClientProperties.getConnectionRequestTimeout())
                .setSocketTimeout(httpClientProperties.getSocketTimeout())
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
            this.schedule.scheduleWithFixedDelay(connectionManager::closeExpiredConnections,
                    30000, httpClientProperties.getCheckExpiredConnectionInterval(), TimeUnit.MILLISECONDS);
        }
        return connectionManager;
    }

    private PoolingHttpClientConnectionManager poolingHttpClientConnectionManager() {
        if (!httpClientProperties.getSwitchNodeWhenConnectionTimeout()) {
            // 控制httpclient connection-timeout 时不切换节点重试
            return new PoolingHttpClientConnectionManager(
                    new NoSwitchHttpClientConnectionOperator(getDefaultRegistry(), null, null),
                    null,
                    httpClientProperties.getMaxLifeTime(),
                    TimeUnit.MICROSECONDS);
        }
        return new PoolingHttpClientConnectionManager(
                new DefaultHttpClientConnectionOperator(getDefaultRegistry(), null, null),
                null,
                httpClientProperties.getMaxLifeTime(),
                TimeUnit.MICROSECONDS);
    }

    private static Registry<ConnectionSocketFactory> getDefaultRegistry() {
        return RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", SSLConnectionSocketFactory.getSocketFactory())
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
