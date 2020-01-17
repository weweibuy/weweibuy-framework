package com.weweibuy.framework.rocketmq.core.consumer;

import org.springframework.core.MethodParameter;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author durenhao
 * @date 2020/1/6 23:24
 **/
public class HandlerMethodArgumentResolverComposite {

    private final List<HandlerMethodArgumentResolver> argumentResolvers = new LinkedList<>();

    private final Map<MethodParameter, HandlerMethodArgumentResolver> argumentResolverCache =
            new ConcurrentHashMap<>(32);


    public HandlerMethodArgumentResolverComposite addResolver(HandlerMethodArgumentResolver argumentResolver) {
        this.argumentResolvers.add(argumentResolver);
        return this;
    }

    public HandlerMethodArgumentResolverComposite addResolvers(HandlerMethodArgumentResolver... resolvers) {
        if (resolvers != null) {
            for (HandlerMethodArgumentResolver resolver : resolvers) {
                this.argumentResolvers.add(resolver);
            }
        }
        return this;
    }

    public HandlerMethodArgumentResolverComposite addResolvers(
            List<? extends HandlerMethodArgumentResolver> argumentResolvers) {

        if (argumentResolvers != null) {
            for (HandlerMethodArgumentResolver resolver : argumentResolvers) {
                this.argumentResolvers.add(resolver);
            }
        }
        return this;
    }


    public HandlerMethodArgumentResolver getArgumentResolver(MethodParameter parameter) {
        HandlerMethodArgumentResolver result = this.argumentResolverCache.get(parameter);
        if (result == null) {
            for (HandlerMethodArgumentResolver resolver : this.argumentResolvers) {
                if (resolver.supportsParameter(parameter)) {
                    result = resolver;
                    this.argumentResolverCache.put(parameter, result);
                    break;
                }
            }
        }
        return result;
    }

    public List<HandlerMethodArgumentResolver> getArgumentResolvers() {
        return argumentResolvers;
    }

}
