package com.weweibuy.framework.compensate.support;

import com.weweibuy.framework.compensate.annotation.Compensate;
import com.weweibuy.framework.compensate.core.CompensateConfigStore;
import com.weweibuy.framework.compensate.core.CompensateTypeResolver;
import com.weweibuy.framework.compensate.model.CompensateConfigProperties;
import com.weweibuy.framework.compensate.model.CompensateInfo;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 注解数据解析器
 *
 * @author durenhao
 * @date 2020/2/14 16:56
 **/
public class CompensateAnnotationMetaDataParser {

    private Map<String, BinaryExceptionClassifier> shouldCompensateMap = new ConcurrentHashMap<>();

    private final CompensateConfigStore compensateConfigStore;

    private final CompensateTypeResolverComposite resolverComposite;

    public CompensateAnnotationMetaDataParser(CompensateConfigStore compensateConfigStore,
                                              CompensateTypeResolverComposite resolverComposite) {
        this.compensateConfigStore = compensateConfigStore;
        this.resolverComposite = resolverComposite;
    }


    public CompensateInfo toCompensateInfo(Compensate annotation, Object target, Method method, Object[] args) {
        String key = annotation.key();

        CompensateConfigProperties configProperties = compensateConfigStore.compensateConfig(key);
        if (configProperties == null) {
            throw new IllegalStateException("补偿Key: " + key + ",必须有对应的配置");
        }
        String compensateType = configProperties.getCompensateType();

        CompensateTypeResolver argumentResolver = resolverComposite.getArgumentResolver(compensateType);
        Assert.notNull(argumentResolver, "补偿类型: " + compensateType + ",没有对应的解析器");
        CompensateInfo info = argumentResolver.resolver(annotation, target, method, args, configProperties);
        info.setCompensateType(compensateType);
        return info;
    }

    /**
     * 解析补偿信息为 方法参数
     *
     * @param compensateInfo
     * @return
     */
    public Object[] parserCompensateInfo(CompensateInfo compensateInfo) {
        CompensateConfigProperties configProperties = compensateConfigStore.compensateConfig(compensateInfo.getCompensateKey());
        String compensateType = configProperties.getCompensateType();
        CompensateTypeResolver argumentResolver = resolverComposite.getArgumentResolver(compensateType);
        return argumentResolver.deResolver(compensateInfo);
    }

    /**
     * 根据异常判断是否补偿
     *
     * @param annotation
     * @param e
     * @return
     */
    public boolean shouldCompensate(Compensate annotation, Exception e) {

        BinaryExceptionClassifier classifier = shouldCompensateMap.computeIfAbsent(annotation.key(), k -> {
            Map<Class<? extends Throwable>, Boolean> compensateMap = new HashMap<>(8);
            Class<? extends Throwable>[] include = annotation.include();
            Class<? extends Throwable>[] exclude = annotation.exclude();
            for (Class<? extends Throwable> i : include) {
                compensateMap.put(i, true);
            }
            for (Class<? extends Throwable> i : exclude) {
                compensateMap.put(i, false);
            }
            return new BinaryExceptionClassifier(compensateMap, false, true);
        });

        return classifier.classify(e);
    }


}
