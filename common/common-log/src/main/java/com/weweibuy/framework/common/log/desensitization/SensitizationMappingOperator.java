package com.weweibuy.framework.common.log.desensitization;

import com.weweibuy.framework.common.core.model.constant.CommonConstant;
import com.weweibuy.framework.common.log.constant.LogMdcConstant;
import com.weweibuy.framework.common.log.constant.LogSensitizationEum;
import com.weweibuy.framework.common.log.utils.HttpRequestUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.MDC;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 脱敏规则映射操作
 *
 * @author durenhao
 * @date 2020/6/6 16:53
 **/
public class SensitizationMappingOperator {

    private static final SensitizationMappingConfigurer CONFIGURER = new SensitizationMappingConfigurer();

    private static List<SensitizationMappingConfigurer.HttpSensitizationMapping> httpMappingList;

    private static List<SensitizationMappingConfigurer.RocketMqSensitizationMapping> rocketMqMappingList;

    private static ConcurrentHashMap<String, Optional<SensitizationMappingConfigurer.RocketMqSensitizationMapping>> ROCKET_MQ_MAPPING_MAP = new ConcurrentHashMap<>(8);

    static SensitizationMappingConfigurer getConfigurerInstance() {
        return CONFIGURER;
    }

    /**
     * 初始化配置
     */
    static void initConfigurer() {
        Map<LogSensitizationEum, List<SensitizationMapping>> listMap = CONFIGURER.build();
        httpMappingList = (List<SensitizationMappingConfigurer.HttpSensitizationMapping>) (Object) listMap.get(LogSensitizationEum.HTTP);
        rocketMqMappingList = (List<SensitizationMappingConfigurer.RocketMqSensitizationMapping>) (Object) listMap.get(LogSensitizationEum.ROCKET_MQ);
    }

    public static Optional<SensitizationMappingConfigurer.HttpSensitizationMapping> matchRequest(HttpServletRequest request) {

        if (httpMappingList == null) {
            return Optional.empty();
        }

        String requestURI = request.getRequestURI();
        String method = request.getMethod();

        return httpMappingList.stream()
                .filter(m -> Objects.isNull(m.getHttpMethod()) || m.getHttpMethod().matches(method))
                .filter(m -> Objects.nonNull(m.getPath()) && HttpRequestUtils.isMatchPath(m.getPath(), requestURI))
                .filter(m -> CollectionUtils.isNotEmpty(m.getSensitizationField()))
                .findAny();

    }

    public static Optional<SensitizationMappingConfigurer.RocketMqSensitizationMapping> matchTopicTag(String topic, String tag) {

        if (rocketMqMappingList == null) {
            return Optional.empty();
        }

        String mapKey = topic + "_" + tag;
        return ROCKET_MQ_MAPPING_MAP.computeIfAbsent(mapKey, key ->
                rocketMqMappingList.stream()
                        .filter(m -> Objects.nonNull(m.getTopic()) && Objects.nonNull(m.getTag()))
                        .filter(m -> mapKey.equals(m.getTopic() + "_" + m.getTag()))
                        .filter(m -> CollectionUtils.isNotEmpty(m.getSensitizationField()))
                        .findAny());
    }


    public static void bindSensitizationContext(Set<String> sensitizationField, String logger) {
        MDC.put(LogMdcConstant.SENSITIZATION_FIELDS, sensitizationField.stream().collect(Collectors.joining(CommonConstant.COMMA_STR)));
        MDC.put(LogMdcConstant.SENSITIZATION_LOGGER, logger);
    }

    public static void removeSensitizationContext() {
        MDC.remove(LogMdcConstant.SENSITIZATION_FIELDS);
        MDC.remove(LogMdcConstant.SENSITIZATION_LOGGER);
    }

    public static Optional<Set<String>> getSensitizationField() {
        return Optional.ofNullable(MDC.get(LogMdcConstant.SENSITIZATION_FIELDS))
                .map(s -> Arrays.stream(s.split(CommonConstant.COMMA_STR)).collect(Collectors.toSet()));
    }

    public static Optional<String> getSensitizationLogger() {
        return Optional.ofNullable(MDC.get(LogMdcConstant.SENSITIZATION_LOGGER));
    }


}
