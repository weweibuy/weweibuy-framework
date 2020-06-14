package com.weweibuy.framework.common.feign.support;

import feign.Response;
import feign.Util;
import feign.codec.Decoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;

/**
 * @author durenhao
 * @date 2020/3/2 21:46
 **/
@Slf4j
public class EncoderAndDecoder extends SpringEncoder implements Decoder {

    private Decoder delegate;

    public EncoderAndDecoder(ObjectFactory<HttpMessageConverters> messageConverters) {
        super(messageConverters);
        this.delegate = new ResponseEntityDecoder(new SpringDecoder(messageConverters));
    }


    @Override
    public Object decode(Response response, Type type) throws IOException {
        if (!isOptional(type)) {
            return delegate.decode(response, type);
        }
        if (response.status() == 404 || response.status() == 204) {
            return Optional.empty();
        }
        Type enclosedType = Util.resolveLastTypeParameter(type, Optional.class);
        return Optional.ofNullable(delegate.decode(response, enclosedType));
    }


    static boolean isOptional(Type type) {
        if (!(type instanceof ParameterizedType)) {
            return false;
        }
        ParameterizedType parameterizedType = (ParameterizedType) type;
        return parameterizedType.getRawType().equals(Optional.class);
    }


}
