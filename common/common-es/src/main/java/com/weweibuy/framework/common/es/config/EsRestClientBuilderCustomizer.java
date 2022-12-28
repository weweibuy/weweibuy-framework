package com.weweibuy.framework.common.es.config;

import com.weweibuy.framework.common.core.exception.Exceptions;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.boot.autoconfigure.elasticsearch.RestClientBuilderCustomizer;

import javax.net.ssl.SSLContext;

/**
 * @author durenhao
 * @date 2022/12/26 22:05
 **/
public class EsRestClientBuilderCustomizer implements RestClientBuilderCustomizer {
    @Override
    public void customize(RestClientBuilder builder) {
    }

    @Override
    public void customize(HttpAsyncClientBuilder builder) {
        try {
            SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
                    .loadTrustMaterial(null, TrustAllStrategy.INSTANCE)
                    .build();

            builder.setSSLHostnameVerifier((s, sslSession) -> true);
            builder.setSSLContext(sslContext);
        } catch (Exception e) {
            throw Exceptions.system(e);
        }
    }
}
