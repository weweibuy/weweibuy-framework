package com.weweibuy.framework.common.feign.support;

import feign.Response;
import feign.codec.Decoder;
import feign.optionals.OptionalDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * @author durenhao
 * @date 2020/3/2 21:46
 **/
@Slf4j
public class EncoderAndDecoder extends SpringEncoder implements Decoder {

    private Decoder delegate;

    public EncoderAndDecoder(ObjectFactory<HttpMessageConverters> messageConverters) {
        super(messageConverters);
        this.delegate = new OptionalDecoder(new ResponseEntityDecoder(new SpringDecoder(messageConverters)));
    }


    @Override
    public Object decode(Response response, Type type) throws IOException {
        return delegate.decode(response, type);
    }


}
