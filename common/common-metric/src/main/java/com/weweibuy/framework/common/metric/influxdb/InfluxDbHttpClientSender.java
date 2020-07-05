package com.weweibuy.framework.common.metric.influxdb;

import com.izettle.metrics.influxdb.InfluxDbHttpSender;
import com.izettle.metrics.influxdb.utils.TimeUtils;
import com.weweibuy.framework.common.core.model.constant.CommonConstant;
import com.weweibuy.framework.common.metric.MetricInfluxDbProperties;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpHeaders;

import java.io.IOException;
import java.net.URLEncoder;

/**
 * HttpClientSender
 *
 * @author durenhao
 * @date 2020/7/5 22:09
 **/
public class InfluxDbHttpClientSender extends InfluxDbHttpSender {

    private final CloseableHttpClient closeableHttpClient;

    private final MetricInfluxDbProperties properties;

    private String url;

    public InfluxDbHttpClientSender(MetricInfluxDbProperties properties, CloseableHttpClient closeableHttpClient) throws Exception {
        super("http", "localhost", 0, properties.getDatabase(), properties.getAuthString(), properties.getTimePrecision(), 0, 0, "");
        this.closeableHttpClient = closeableHttpClient;
        this.properties = properties;
        StringBuilder stringBuilder = new StringBuilder();
        String host = properties.getHost();
        if (!host.startsWith("http://") && !host.startsWith("https://")) {
            host = "http://" + host;
        }
        stringBuilder.append(host);
        stringBuilder.append("/write?db=");
        stringBuilder.append(URLEncoder.encode(properties.getDatabase(), CommonConstant.CharsetConstant.UTF8_STR));
        String queryPrecision = String.format("precision=%s", TimeUtils.toTimePrecision(properties.getTimePrecision()));
        stringBuilder.append("&");
        stringBuilder.append(queryPrecision);
        url = stringBuilder.toString();
    }

    @Override
    protected int writeData(byte[] line) throws Exception {
        HttpPost httpPost = new HttpPost(url);
        if (StringUtils.isNotBlank(properties.getAuthString())) {
            httpPost.addHeader(HttpHeaders.AUTHORIZATION, "Basic " + properties.getAuthString());
        }
        RequestConfig config = httpPost.getConfig();
        RequestConfig.Builder builder = RequestConfig.custom();
        if (config == null) {
            builder = RequestConfig.custom();
        } else {
            builder = RequestConfig.copy(httpPost.getConfig());
        }
        config = builder.setConnectTimeout(properties.getConnectionTimeout())
                .setSocketTimeout(properties.getSocketTimeout())
                .build();
        httpPost.setEntity(new ByteArrayEntity(line, ContentType.APPLICATION_JSON));
        httpPost.setConfig(config);
        try (CloseableHttpResponse response = closeableHttpClient.execute(httpPost)) {
            int responseCode = response.getStatusLine().getStatusCode();
            if (responseCode / 100 != 2) {
                String string = EntityUtils.toString(response.getEntity());
                throw new IOException(
                        "Server returned HTTP response code: " + responseCode + " for URL: " + url + " with content :'"
                                + string + "'");
            }
        }
        return 0;
    }

}
