package com.weweibuy.framework.compensate.support;


import com.weweibuy.framework.compensate.core.CompensateTypeResolver;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 总和补偿类型
 *
 * @author durenhao
 * @date 2020/2/14 19:32
 **/
public class CompensateTypeResolverComposite {

    private final List<CompensateTypeResolver> resolvers = new LinkedList<>();

    private final Map<String, CompensateTypeResolver> resolverCache =
            new ConcurrentHashMap<>(32);

    public void addResolvers(List<CompensateTypeResolver> compensateTypeResolver) {
        resolvers.addAll(compensateTypeResolver);
    }

    public void addResolver(CompensateTypeResolver compensateTypeResolver) {
        resolvers.add(compensateTypeResolver);
    }

    /**
     * 根据补偿类型获取 匹配的补偿器
     *
     * @param compositeType
     * @return
     */
    public CompensateTypeResolver getArgumentResolver(String compositeType) {
        CompensateTypeResolver result = this.resolverCache.get(compositeType);
        if (result == null) {
            for (CompensateTypeResolver resolver : this.resolvers) {
                if (resolver.match(compositeType)) {
                    result = resolver;
                    this.resolverCache.put(compositeType, result);
                    break;
                }
            }
        }
        return result;
    }

}
