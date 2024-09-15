package com.weweibuy.framework.common.feign.support;

import com.weweibuy.framework.common.core.utils.JackJsonUtils;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

/**
 * @author durenhao
 * @date 2020/3/2 23:23
 **/
public class SnakeCaseEncoderAndDecoder extends EncoderAndDecoder {

    private static ObjectFactory<HttpMessageConverters> httpMessageConvertersObjectFactory;

    public SnakeCaseEncoderAndDecoder() {
        super(messageConverters());
    }

    static synchronized ObjectFactory<HttpMessageConverters> messageConverters() {
        if (httpMessageConvertersObjectFactory == null) {
            MappingJackson2HttpMessageConverter messageConverter =
                    new MappingJackson2HttpMessageConverter(JackJsonUtils.getSnakeCaseMapper());
            HttpMessageConverters converters = new HttpMessageConverters(messageConverter);
            httpMessageConvertersObjectFactory = () -> converters;
        }
        return httpMessageConvertersObjectFactory;
    }

}
