package com.weweibuy.framework.common.log.mvc;

import com.weweibuy.framework.common.core.model.constant.CommonConstant;
import com.weweibuy.framework.common.core.utils.HttpRequestUtils;
import com.weweibuy.framework.common.log.config.CommonLogProperties;
import com.weweibuy.framework.common.log.constant.LogMdcConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.http.HttpMethod;

import jakarta.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 脱敏规则映射操作
 *
 * @author durenhao
 * @date 2020/6/6 16:53
 **/
@Slf4j
public class MvcPathMappingOperator {

    private static final String HTTP_PATH_PROPERTIES_ATTR_KEY = "http_path_properties_attr_key";

    /**
     * 精确匹配 key --> path + method
     */
    private Map<String, CommonLogProperties.HttpPathProperties> methodPathExactProperties = new HashMap<>();

    /**
     * 路径匹配 key --> method
     */
    private Map<String, List<CommonLogProperties.HttpPathProperties>> methodPatternProperties = new HashMap<>();


    public MvcPathMappingOperator(List<CommonLogProperties.HttpPathProperties> propertiesConfig, List<CommonLogProperties.HttpPathProperties> codeConfig) {
        init(propertiesConfig, codeConfig);
    }


    /**
     * 获取请求的日志配置信息
     *
     * @param request
     * @return
     */
    public CommonLogProperties.LogProperties findLogProperties(HttpServletRequest request) {
        return findPathPropertiesAndSetReqAttr(request)
                .map(CommonLogProperties.HttpPathProperties::getLog)
                .orElse(null);
    }

    public CommonLogProperties.SensitizationProperties findSensitizationProperties(HttpServletRequest request) {
        return findPathPropertiesAndSetReqAttr(request)
                .map(CommonLogProperties.HttpPathProperties::getSensitization)
                .orElse(null);
    }

    private Optional<CommonLogProperties.HttpPathProperties> findPathPropertiesAndSetReqAttr(HttpServletRequest request) {

        String path = request.getRequestURI();
        if (StringUtils.isBlank(path)) {
            return Optional.empty();
        }
        Optional<CommonLogProperties.HttpPathProperties> propertiesOpt = (Optional<CommonLogProperties.HttpPathProperties>) request.getAttribute(HTTP_PATH_PROPERTIES_ATTR_KEY);
        if (propertiesOpt != null) {
            return propertiesOpt;
        }

        String method = request.getMethod();

        CommonLogProperties.HttpPathProperties properties = findExact(path, method);
        if (properties == null) {
            properties = findPattern(path, method);
        }
        propertiesOpt = Optional.ofNullable(properties);
        request.setAttribute(HTTP_PATH_PROPERTIES_ATTR_KEY, propertiesOpt);
        return propertiesOpt;
    }

    private CommonLogProperties.HttpPathProperties findExact(String path, String method) {
        return methodPathExactProperties.get(exactKey(path, method));
    }

    private static String exactKey(String path, String method) {
        return path.toUpperCase() + "_" + method.toUpperCase();
    }

    private CommonLogProperties.HttpPathProperties findPattern(String path, String method) {
        return Optional.ofNullable(methodPatternProperties.get(method.toUpperCase()))
                .flatMap(s -> s.stream()
                        .filter(p -> HttpRequestUtils.isMatchPath(p.getPath(), path))
                        .findFirst())
                .orElse(null);
    }


    private void init(List<CommonLogProperties.HttpPathProperties> propertiesConfig, List<CommonLogProperties.HttpPathProperties> codeConfig) {

        if (CollectionUtils.isEmpty(propertiesConfig)) {
            propertiesConfig = Collections.emptyList();
        }
        if (CollectionUtils.isEmpty(codeConfig)) {
            codeConfig = Collections.emptyList();
        }

        Map<Boolean, List<CommonLogProperties.HttpPathProperties>> propertiesBooleanListMap =
                propertiesConfig.stream()
                        .collect(Collectors.groupingBy(e -> e.getPath().indexOf('*') != -1));

        Map<Boolean, List<CommonLogProperties.HttpPathProperties>> codeBooleanListMap =
                codeConfig.stream()
                        .collect(Collectors.groupingBy(e -> e.getPath().indexOf('*') != -1));

        putExactPath(codeBooleanListMap);
        putExactPath(propertiesBooleanListMap);

        putMethodPattern(propertiesBooleanListMap);
        putMethodPattern(codeBooleanListMap);

    }


    private void putExactPath(Map<Boolean, List<CommonLogProperties.HttpPathProperties>> booleanListMap) {
        List<CommonLogProperties.HttpPathProperties> exactPathConfig
                = Optional.ofNullable(booleanListMap.get(false))
                .orElse(Collections.emptyList());

        for (CommonLogProperties.HttpPathProperties properties : exactPathConfig) {
            for (HttpMethod method : properties.getMethods()) {
                String key = exactKey(properties.getPath(), method.toString());
                methodPathExactProperties.put(key, properties);
            }
        }
    }

    private void putMethodPattern(Map<Boolean, List<CommonLogProperties.HttpPathProperties>> booleanListMap) {
        List<CommonLogProperties.HttpPathProperties> patternPathLogHttpProperties
                = Optional.ofNullable(booleanListMap.get(true))
                .orElse(Collections.emptyList());

        for (CommonLogProperties.HttpPathProperties properties : patternPathLogHttpProperties) {
            for (HttpMethod method : properties.getMethods()) {
                String methodKey = method.toString();
                List<CommonLogProperties.HttpPathProperties> propertiesList = methodPatternProperties.get(methodKey);
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
