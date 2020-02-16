package com.weweibuy.framework.compensate.support;

import com.weweibuy.framework.compensate.interfaces.RecoverMethodArgsResolver;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author durenhao
 * @date 2020/2/15 22:09
 **/
public class RecoverMethodArgsResolverComposite {

    private final List<RecoverMethodArgsResolver> resolvers = new LinkedList<>();

    private final Map<String, RecoverMethodArgsResolver> resolverCache =
            new ConcurrentHashMap<>(8);

    public void addResolver(RecoverMethodArgsResolver compensateTypeResolver) {
        resolvers.add(compensateTypeResolver);
    }

    public void addResolvers(List<RecoverMethodArgsResolver> compensateTypeResolver) {
        resolvers.addAll(compensateTypeResolver);
    }

    public RecoverMethodArgsResolver getArgumentResolver(String compositeKey) {
        RecoverMethodArgsResolver result = this.resolverCache.get(compositeKey);
        if (result == null) {
            for (RecoverMethodArgsResolver resolver : this.resolvers) {
                if (resolver.match(compositeKey)) {
                    result = resolver;
                    this.resolverCache.put(compositeKey, result);
                    break;
                }
            }
        }
        return result;
    }

}
