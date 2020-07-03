package com.weweibuy.framework.common.log.desensitization;

import com.weweibuy.framework.common.core.model.constant.CommonConstant;
import com.weweibuy.framework.common.log.constant.LogSensitizationEum;
import lombok.Data;
import org.springframework.http.HttpMethod;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 脱敏配置
 *
 * @author durenhao
 * @date 2020/6/6 10:32
 **/
public class SensitizationMappingConfigurer {

    private List<MappingBuilder> sensitizationMappingBuilders = new ArrayList<>();

    /**
     * Http 脱敏配置
     *
     * @return
     */
    public HttpSensitizationMapping.HttpSensitizationMappingBuilder http() {
        HttpSensitizationMapping.HttpSensitizationMappingBuilder builder = new HttpSensitizationMapping.HttpSensitizationMappingBuilder();
        this.sensitizationMappingBuilders.add(builder);
        return builder;
    }

    // TODO 暂未实现

    /**
     * RocketMq 脱敏配置
     *
     * @return
     */
    public RocketMqSensitizationMapping.RocketMqSensitizationMappingBuilder rocketMq() {
        RocketMqSensitizationMapping.RocketMqSensitizationMappingBuilder builder = new RocketMqSensitizationMapping.RocketMqSensitizationMappingBuilder();
        this.sensitizationMappingBuilders.add(builder);
        return builder;
    }

    public Map<LogSensitizationEum, List<SensitizationMapping>> build() {
        return sensitizationMappingBuilders.stream()
                .map(MappingBuilder::build)
                .collect(Collectors.groupingBy(SensitizationMapping::getType));

    }

    @Data
    public static class BaseSensitizationMapping implements SensitizationMapping {
        /**
         * 敏感字段
         */
        private Set<String> sensitizationField;

        private String logger;

        private LogSensitizationEum type;

    }

    @Data
    public static class HttpSensitizationMapping extends BaseSensitizationMapping {

        /**
         * 路径  可以使用 匹配的形式
         */
        private String path;

        private HttpMethod httpMethod;


        public static class HttpSensitizationMappingBuilder implements MappingBuilder {

            /**
             * 敏感字段
             */
            private Set<String> sensitizationField;

            /**
             * Logger 用于精确匹配
             */
            private String logger;

            /**
             * 路径  可以使用 匹配的形式
             */
            private String path;

            private HttpMethod httpMethod;

            public HttpSensitizationMappingBuilder path(String path) {
                this.path = path;
                return this;
            }

            public HttpSensitizationMappingBuilder method(HttpMethod httpMethod) {
                this.httpMethod = httpMethod;
                return this;
            }

            public HttpSensitizationMappingBuilder method(String httpMethod) {
                this.httpMethod = HttpMethod.resolve(httpMethod.toUpperCase());
                return this;
            }

            public HttpSensitizationMappingBuilder sensitizationField(String... sensitizationFields) {
                if (this.sensitizationField == null) {
                    this.sensitizationField = new HashSet();
                }
                Arrays.stream(sensitizationFields)
                        .filter(f ->
                                f.indexOf(CommonConstant.COMMA_STR) == -1)
                        .forEach(this.sensitizationField::add);
                return this;
            }


            public HttpSensitizationMappingBuilder logger(String logger) {
                this.logger = logger;
                return this;
            }

            public HttpSensitizationMappingBuilder logger(Class logger) {
                this.logger = logger.getName();
                return this;
            }

            @Override
            public SensitizationMapping build() {
                HttpSensitizationMapping sensitizationMapping = new HttpSensitizationMapping();
                sensitizationMapping.setHttpMethod(httpMethod);
                sensitizationMapping.setPath(path);
                sensitizationMapping.setLogger(logger);
                sensitizationMapping.setSensitizationField(sensitizationField);
                sensitizationMapping.setType(LogSensitizationEum.HTTP);
                return sensitizationMapping;
            }
        }


    }


    @Data
    public static final class RocketMqSensitizationMapping extends BaseSensitizationMapping {

        /**
         * 主题
         */
        private String topic;

        private String tag;

        public static class RocketMqSensitizationMappingBuilder implements MappingBuilder {

            private Set<String> sensitizationField;

            private String logger;

            private String topic;

            private String tag;

            public RocketMqSensitizationMappingBuilder topic(String topic) {
                this.topic = topic;
                return this;
            }

            public RocketMqSensitizationMappingBuilder tag(String tag) {
                this.tag = tag;
                return this;
            }


            public RocketMqSensitizationMappingBuilder sensitizationField(String... sensitizationFields) {
                if (this.sensitizationField == null) {
                    this.sensitizationField = new HashSet();
                }
                Arrays.stream(sensitizationFields)
                        .filter(f ->
                                f.indexOf(CommonConstant.COMMA_STR) == -1)
                        .forEach(this.sensitizationField::add);
                return this;
            }


            public RocketMqSensitizationMappingBuilder logger(String logger) {
                this.logger = logger;
                return this;
            }

            public RocketMqSensitizationMappingBuilder logger(Class logger) {
                this.logger = logger.getName();
                return this;
            }

            @Override
            public SensitizationMapping build() {
                RocketMqSensitizationMapping mapping = new RocketMqSensitizationMapping();
                mapping.setTopic(topic);
                mapping.setTag(tag);
                mapping.setSensitizationField(sensitizationField);
                mapping.setLogger(logger);
                mapping.setType(LogSensitizationEum.ROCKET_MQ);
                return mapping;
            }
        }


    }


    public interface MappingBuilder {

        SensitizationMapping build();
    }


}
