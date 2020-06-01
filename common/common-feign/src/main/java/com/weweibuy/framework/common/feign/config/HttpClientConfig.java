package com.weweibuy.framework.common.feign.config;

import feign.Client;
import feign.httpclient.ApacheHttpClient;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.ExtractingResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.security.cert.X509Certificate;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author durenhao
 * @date 2019/10/29 10:51
 **/
@Configuration
@EnableConfigurationProperties(HttpClientProperties.class)
@ConditionalOnClass(CloseableHttpClient.class)
public class HttpClientConfig {

    @Autowired
    private HttpClientProperties httpClientProperties;

    private Timer connectionManagerTimer = null;

    /**
     * LB 时 {@link 'HttpClientFeignLoadBalancedConfiguration'}
     *
     * @param httpClient
     * @return
     */
    @Bean
    public Client feignClient(HttpClient httpClient) {
        return new ApacheHttpClient(httpClient);
    }


    @Bean
    public CloseableHttpClient httpClient() throws Exception {

        HttpClientBuilder httpClientBuilder = HttpClients.custom()
                .setConnectionManager(httpClientConnectionManager())
                .disableAutomaticRetries()
                .setDefaultRequestConfig(requestConfig());

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


    private HttpClientConnectionManager httpClientConnectionManager() {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(httpClientProperties.getMaxTotal());
        connectionManager.setDefaultMaxPerRoute(httpClientProperties.getDefaultMaxPerRoute());

        if (this.connectionManagerTimer == null) {
            this.connectionManagerTimer = new Timer(
                    "HttpClientConfigTimer", true);
            this.connectionManagerTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    connectionManager.closeExpiredConnections();
                }
            }, 30000, httpClientProperties.getCheckExpiredConnectionInterval());
        }

        return connectionManager;
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) throws Exception {
        ExtractingResponseErrorHandler errorHandler = new ExtractingResponseErrorHandler();
//        Map<HttpStatus, Class<? extends RestClientException>> mappingMap = new HashMap<>();
//        // 不处理的http 异常code码
//        mappingMap.put(HttpStatus.UNAUTHORIZED, null);
//        mappingMap.put(HttpStatus.FORBIDDEN, null);
//
//        errorHandler.setStatusMapping(mappingMap);

        HttpComponentsClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactory(httpClient());


        return restTemplateBuilder.errorHandler(errorHandler)
                .requestFactory(() -> requestFactory)
                .build();

    }


}
