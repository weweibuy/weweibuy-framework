package com.weweibuy.framework.common.feign.config;

import com.weweibuy.framework.common.feign.support.NoSwitchHttpClientConnectionOperator;
import feign.Client;
import feign.httpclient.ApacheHttpClient;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultHttpClientConnectionOperator;
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
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

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
    public CloseableHttpClient httpClient() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {

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
        PoolingHttpClientConnectionManager connectionManager = poolingHttpClientConnectionManager();
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

    private PoolingHttpClientConnectionManager poolingHttpClientConnectionManager() {
        if (!httpClientProperties.getSwitchNodeWhenConnectionTimeout()) {
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


    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        ExtractingResponseErrorHandler errorHandler = new ExtractingResponseErrorHandler();

        HttpComponentsClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactory(httpClient());

        return restTemplateBuilder.errorHandler(errorHandler)
                .requestFactory(() -> requestFactory)
                .build();

    }


}
