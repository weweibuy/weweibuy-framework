package com.weiweibuy.framework.common.feign.config;

import feign.Client;
import feign.httpclient.ApacheHttpClient;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
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

    @Bean
    public Client feignClient(HttpClient httpClient) {
        return new ApacheHttpClient(httpClient);
    }


    @Bean
    public CloseableHttpClient httpClient() throws Exception {
        TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

        SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
                .loadTrustMaterial(null, acceptingTrustStrategy)
                .build();

        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);
        return HttpClients.custom()
                .setSSLSocketFactory(csf)
                .setConnectionManager(httpClientConnectionManager())
                .disableAutomaticRetries()
                .setDefaultRequestConfig(requestConfig())
                .build();
    }

    public RequestConfig requestConfig() {
        return RequestConfig.custom().setConnectTimeout(httpClientProperties.getConnectTimeout())
                .setConnectionRequestTimeout(httpClientProperties.getConnectionRequestTimeout())
                .setSocketTimeout(httpClientProperties.getSocketTimeout())
                .build();
    }


    public HttpClientConnectionManager httpClientConnectionManager() {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(httpClientProperties.getMaxTotal());
        connectionManager.setDefaultMaxPerRoute(httpClientProperties.getDefaultMaxPerRoute());
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
