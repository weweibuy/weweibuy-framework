package com.weweibuy.framework.common.feign.support;

import feign.Feign;

/**
 * @author durenhao
 * @date 2020/6/14 8:37
 **/
public class LogContextFeignBuilder extends Feign.Builder{

    @Override
    public Feign build() {
//        SynchronousMethodHandler.Factory synchronousMethodHandlerFactory =
//                new SynchronousMethodHandler.Factory(client, retryer, requestInterceptors, logger,
//                        logLevel, decode404, closeAfterDecode, propagationPolicy);
//        ReflectiveFeign.ParseHandlersByName handlersByName =
//                new ReflectiveFeign.ParseHandlersByName(contract, options, encoder, decoder, queryMapEncoder,
//                        errorDecoder, synchronousMethodHandlerFactory);
//        return new ReflectiveFeign(handlersByName, invocationHandlerFactory, queryMapEncoder);
        return null;
    }
}


