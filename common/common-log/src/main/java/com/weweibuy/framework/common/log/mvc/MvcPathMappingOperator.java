package com.weweibuy.framework.common.log.mvc;

import com.weweibuy.framework.common.core.model.constant.CommonConstant;
import com.weweibuy.framework.common.core.utils.HttpRequestUtils;
import com.weweibuy.framework.common.log.config.CommonLogProperties;
import com.weweibuy.framework.common.log.constant.LogMdcConstant;
import com.weweibuy.framework.common.log.support.HttpLogConfigurer;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 脱敏规则映射操作
 *
 * @author durenhao
 * @date 2020/6/6 16:53
 **/
@Slf4j
public class MvcPathMappingOperator {


    /**
     * 精确匹配
     */
    private Map<String, CommonLogProperties.CommonLogHttpProperties> methodPathExactProperties = new HashMap<>();

    /**
     * 路径匹配
     */
    private Map<String, List<CommonLogProperties.CommonLogHttpProperties>> methodPatternProperties = new HashMap<>();


    public MvcPathMappingOperator(CommonLogProperties commonLogProperties, List<HttpLogConfigurer> logDisableConfigurer) {
        mergeProperties(commonLogProperties, logDisableConfigurer);
        init(commonLogProperties);
    }

    private void mergeProperties(CommonLogProperties commonLogProperties, List<HttpLogConfigurer> logDisableConfigurer) {

        if (CollectionUtils.isEmpty(logDisableConfigurer)) {
            return;
        }
        List<CommonLogProperties.CommonLogHttpProperties> logHttpProperties = new ArrayList<>();
        logDisableConfigurer.forEach(l -> l.addHttpLogConfig(logHttpProperties));
        if (CollectionUtils.isEmpty(commonLogProperties.getHttpPath())) {
            commonLogProperties.setHttpPath(logHttpProperties);
            return;
        }

        Map<String, CommonLogProperties.CommonLogHttpProperties> propertiesMap = commonLogProperties.getHttpPath().stream()
                .collect(Collectors.toMap(CommonLogProperties.CommonLogHttpProperties::getPath, Function.identity(), (o, n) -> n));

        for (CommonLogProperties.CommonLogHttpProperties commonLogHttpProperties : logHttpProperties) {
            if (!propertiesMap.containsKey(commonLogHttpProperties.getPath())) {
                commonLogProperties.getHttpPath().add(commonLogHttpProperties);
            }
        }
    }

    /**
     * 获取请求的日志配置信息
     *
     * @param request
     * @return
     */
    public CommonLogProperties.CommonLogHttpProperties findLogProperties(HttpServletRequest request) {
        String path = request.getRequestURI();
        if (StringUtils.isBlank(path)) {
            return null;
        }
        String method = request.getMethod();
        return Optional.ofNullable(findExact(path, method))
                .orElseGet(() -> findPattern(path, method));
    }

    private CommonLogProperties.CommonLogHttpProperties findExact(String path, String method) {
        return methodPathExactProperties.get(exactKey(path, method));
    }

    private static String exactKey(String path, String method) {
        return path.toUpperCase() + "_" + method.toUpperCase();
    }

    private CommonLogProperties.CommonLogHttpProperties findPattern(String path, String method) {
        return Optional.ofNullable(methodPatternProperties.get(method.toUpperCase()))
                .flatMap(s -> s.stream()
                        .filter(p -> HttpRequestUtils.isMatchPath(p.getPath(), path))
                        .findFirst())
                .orElse(null);
    }


    private void init(CommonLogProperties commonLogProperties) {
        Map<Boolean, List<CommonLogProperties.CommonLogHttpProperties>> booleanListMap = Optional.ofNullable(commonLogProperties.getHttpPath())
                .map(l -> l.stream()
                        .collect(Collectors.groupingBy(e -> e.getPath().indexOf('*') != -1)))
                .orElse(Collections.emptyMap());

        List<CommonLogProperties.CommonLogHttpProperties> exactPathLogHttpProperties
                = Optional.ofNullable(booleanListMap.get(false))
                .orElse(Collections.emptyList());

        for (CommonLogProperties.CommonLogHttpProperties properties : exactPathLogHttpProperties) {
            for (String method : properties.getMethod()) {
                methodPathExactProperties.put(exactKey(properties.getPath(), method), properties);
            }
        }

        List<CommonLogProperties.CommonLogHttpProperties> patternPathLogHttpProperties
                = Optional.ofNullable(booleanListMap.get(true))
                .orElse(Collections.emptyList());

        for (CommonLogProperties.CommonLogHttpProperties properties : exactPathLogHttpProperties) {
            for (String method : properties.getMethod()) {
                String methodKey = method.toUpperCase();
                List<CommonLogProperties.CommonLogHttpProperties> propertiesList = methodPatternProperties.get(methodKey);
                if (propertiesList == null) {
                    propertiesList = new ArrayList<>();
                    propertiesList.add(properties);
                } else {
                    boolean match = propertiesList.stream()
                            .anyMatch(p -> p.getPath().equals(properties.getPath()));
                    if (!match) {
                        propertiesList.add(properties);
                    }

                }
                methodPatternProperties.put(methodKey, propertiesList);
            }
        }

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
