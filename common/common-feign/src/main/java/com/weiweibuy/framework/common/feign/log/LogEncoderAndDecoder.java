package com.weiweibuy.framework.common.feign.log;

import feign.FeignException;
import feign.Response;
import feign.Util;
import feign.codec.DecodeException;
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
public class LogEncoderAndDecoder extends SpringEncoder implements Decoder {

    private Decoder delegate;

    public LogEncoderAndDecoder(ObjectFactory<HttpMessageConverters> messageConverters) {
        super(messageConverters);
        this.delegate = new ResponseEntityDecoder(new SpringDecoder(messageConverters));
    }


    @Override
    public Object decode(Response response, Type type) throws IOException, DecodeException, FeignException {
        response = logAndRebufferResponse(response);
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

    static Response logAndRebufferResponse(Response response) throws IOException {
        int status = response.status();
        String bodyStr = "";
        if (response.body() != null && !(status == 204 || status == 205)) {
            byte[] bodyData = Util.toByteArray(response.body().asInputStream());
            bodyStr = new String(bodyData);
            response = response.toBuilder().body(bodyData).build();
        }
        log.info("Feign 响应头: {}, 响应数据: {}", response.headers(), bodyStr);
        return response;
    }


}
