package com.weweibuy.framework.common.log.desensitization;

import com.weweibuy.framework.common.core.model.constant.CommonConstant;
import com.weweibuy.framework.common.core.utils.HttpRequestUtils;
import com.weweibuy.framework.common.core.utils.StringConnectUtils;
import com.weweibuy.framework.common.log.constant.LogMdcConstant;
import com.weweibuy.framework.common.log.constant.LogSensitizationEum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.http.HttpMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 脱敏规则映射操作
 *
 * @author durenhao
 * @date 2020/6/6 16:53
 **/
@Slf4j
public class SensitizationMappingOperator {

    private static final SensitizationMappingConfigurer CONFIGURER = new SensitizationMappingConfigurer();

    private static List<SensitizationMappingConfigurer.HttpSensitizationMapping> httpPatternMappingList;

    private static Map<String, SensitizationMappingConfigurer.HttpSensitizationMapping> httpExactMappingMap;

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
        // 区分匹配型路径 与 精确型路径
        Map<Boolean, List<SensitizationMappingConfigurer.HttpSensitizationMapping>> booleanListMap = Optional.ofNullable((List<SensitizationMappingConfigurer.HttpSensitizationMapping>) (Object) listMap.get(LogSensitizationEum.HTTP))
                .map(m -> m.stream()
                        .filter(i -> shouldPickHttpConfig(i))
                        .distinct()
                        .collect(Collectors.groupingBy(i -> i.getPath().indexOf("*") != -1,
                                Collectors.toList())))
                .orElse(Collections.emptyMap());

        httpPatternMappingList = Optional.ofNullable(booleanListMap.get(true))
                .orElse(Collections.emptyList());

        HttpMethod[] httpMethodArr = {HttpMethod.GET, HttpMethod.POST, HttpMethod.DELETE, HttpMethod.PUT};
        httpExactMappingMap = Optional.ofNullable(booleanListMap.get(false))
                .map(m -> m.stream()
                        .flatMap(i -> completionHttpMethod(i))
                        .distinct()
                        .collect(Collectors.toMap(i ->
                                        StringConnectUtils.connect(CommonConstant.UNDERLINE_STR, i.getPath(), i.getHttpMethod().toString()),
                                Function.identity(),
                                (o, n) -> n)))
                .orElse(Collections.emptyMap());

        // TODO 暂未实现
        rocketMqMappingList = (List<SensitizationMappingConfigurer.RocketMqSensitizationMapping>) (Object) listMap.get(LogSensitizationEum.ROCKET_MQ);
    }

    public static Optional<SensitizationMappingConfigurer.HttpSensitizationMapping> matchRequest(HttpServletRequest request) {

        String requestURI = request.getRequestURI();
        String method = request.getMethod();

        SensitizationMappingConfigurer.HttpSensitizationMapping mapping = null;
        // 精确匹配
        if (!httpExactMappingMap.isEmpty() && (mapping = httpExactMappingMap.get(StringConnectUtils.connect(requestURI, method))) != null) {
            return Optional.ofNullable(mapping);
        }
        // 路径匹配
        return httpPatternMappingList.stream()
                .filter(m -> Objects.isNull(m.getHttpMethod()) || m.getHttpMethod().matches(method))
                .filter(m -> HttpRequestUtils.isMatchPath(m.getPath(), requestURI))
                .findAny();
    }

    public static Optional<SensitizationMappingConfigurer.RocketMqSensitizationMapping> matchTopicTag(String topic, String tag) {

        if (rocketMqMappingList == null) {
            return Optional.empty();
        }

        String mapKey = StringConnectUtils.connect(CommonConstant.UNDERLINE_STR, topic, tag);
        return ROCKET_MQ_MAPPING_MAP.computeIfAbsent(mapKey, key ->
                rocketMqMappingList.stream()
                        .filter(m -> Objects.nonNull(m.getTopic()) && Objects.nonNull(m.getTag()))
                        .filter(m -> mapKey.equals(StringConnectUtils.connect(CommonConstant.UNDERLINE_STR, m.getTopic(), m.getTag())))
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

    private static boolean shouldPickHttpConfig(SensitizationMappingConfigurer.HttpSensitizationMapping mapping) {
        if (StringUtils.isBlank(mapping.getPath())) {
            log.warn("没有配置Http 脱敏路径: {}, 将对其忽略", mapping);
            return false;
        }
        if (CollectionUtils.isEmpty(mapping.getSensitizationField())) {
            log.warn("没有配置Http 脱敏路径: {}, 没有设置脱敏字段: {}, 将对其忽略", mapping.getPath());
            return false;
        }
        return true;
    }

    private static Stream<SensitizationMappingConfigurer.HttpSensitizationMapping> completionHttpMethod(SensitizationMappingConfigurer.HttpSensitizationMapping mapping) {
        HttpMethod[] httpMethodArr = {HttpMethod.GET, HttpMethod.POST, HttpMethod.DELETE, HttpMethod.PUT};
        if (mapping.getHttpMethod() == null) {
            return Arrays.stream(httpMethodArr)
                    .map(h -> {
                        mapping.setHttpMethod(h);
                        return mapping;
                    });
        }
        return Stream.of(mapping);
    }


}
