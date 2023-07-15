package com.weweibuy.framework.common.core.support;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.weweibuy.framework.common.core.model.constant.CommonConstant;
import com.weweibuy.framework.common.core.utils.DateTimeUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.jackson.JsonComponentModule;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

/**
 * @author durenhao
 * @date 2021/2/16 21:33
 **/
public class JacksonBuilderHelper {

    private static final Map<?, Boolean> FEATURE_DEFAULTS;

    private static final Collection<Module> MODULE_LIST;

    static {
        Map<Object, Boolean> featureDefaults = new HashMap<>();
        featureDefaults.put(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        featureDefaults.put(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS, false);
        FEATURE_DEFAULTS = Collections.unmodifiableMap(featureDefaults);
        MODULE_LIST = new ArrayList<>();
        MODULE_LIST.add(new ParameterNamesModule(JsonCreator.Mode.DEFAULT));
        MODULE_LIST.add(new JsonComponentModule());
    }

    public static Jackson2ObjectMapperBuilder objectMapperBuilder(String dateTimeFormat, String dataFormat) {
        return objectMapperBuilder(dateTimeFormat, dataFormat, CommonConstant.DateConstant.STANDARD_TIME_FORMAT_STR);
    }


    public static Jackson2ObjectMapperBuilder objectMapperBuilder(String dateTimeFormat, String dataFormat, String timeFormat) {
        StandardJackson2ObjectMapperBuilderCustomizer standardJackson2ObjectMapperBuilderCustomizer =
                new StandardJackson2ObjectMapperBuilderCustomizer();
        Jackson2ObjectMapperBuilder objectMapperBuilder = new Jackson2ObjectMapperBuilder();
        List<Jackson2ObjectMapperBuilderCustomizer> customizers = new ArrayList<>();
        Jackson2ObjectMapperBuilderCustomizer localDateCustomizer = localDateCustomizer(dataFormat);
        Jackson2ObjectMapperBuilderCustomizer localDateTimeCustomizer = localDateTimeCustomizer(dateTimeFormat);
        Jackson2ObjectMapperBuilderCustomizer localTimeCustomizer = localTimeCustomizer(timeFormat);
        customizers.add(standardJackson2ObjectMapperBuilderCustomizer);
        customizers.add(localDateCustomizer);
        customizers.add(localDateTimeCustomizer);
        customizers.add(localTimeCustomizer);
        // 加载自定义配置信息
        ServiceLoader<Jackson2ObjectMapperBuilderCustomizer> LOADER = ServiceLoader.load(Jackson2ObjectMapperBuilderCustomizer.class);
        Iterator<Jackson2ObjectMapperBuilderCustomizer> iterator = LOADER.iterator();
        while (iterator.hasNext()) {
            customizers.add(iterator.next());
        }
        for (Jackson2ObjectMapperBuilderCustomizer customizer : customizers) {
            customizer.customize(objectMapperBuilder);
        }
        return objectMapperBuilder;
    }

    public static final class StandardJackson2ObjectMapperBuilderCustomizer
            implements Jackson2ObjectMapperBuilderCustomizer {


        @Override
        public void customize(Jackson2ObjectMapperBuilder builder) {

            builder.timeZone(TimeZone.getDefault());
            configureFeatures(builder, FEATURE_DEFAULTS);
            configureDateFormat(builder);
            configurePropertyNamingStrategy(builder, null);
        }

        private void configureFeatures(Jackson2ObjectMapperBuilder builder, Map<?, Boolean> features) {
            features.forEach((feature, value) -> {
                if (value != null) {
                    if (value) {
                        builder.featuresToEnable(feature);
                    } else {
                        builder.featuresToDisable(feature);
                    }
                }
            });
        }

        private void configureVisibility(Jackson2ObjectMapperBuilder builder,
                                         Map<PropertyAccessor, JsonAutoDetect.Visibility> visibilities) {
            visibilities.forEach(builder::visibility);
        }

        private void configureDateFormat(Jackson2ObjectMapperBuilder builder) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(CommonConstant.DateConstant.STANDARD_DATE_TIME_FORMAT_STR);
            TimeZone timeZone = TimeZone.getDefault();
            simpleDateFormat.setTimeZone(timeZone);
            builder.dateFormat(simpleDateFormat);
        }
    }

    private static void configurePropertyNamingStrategy(Jackson2ObjectMapperBuilder builder, String strategy) {
        // We support a fully qualified class name extending Jackson's
        // PropertyNamingStrategy or a string value corresponding to the constant
        // names in PropertyNamingStrategy which hold default provided
        // implementations
        if (org.apache.commons.lang3.StringUtils.isNotBlank(strategy)) {
            try {
                configurePropertyNamingStrategyClass(builder, ClassUtils.forName(strategy, null));
            } catch (ClassNotFoundException ex) {
                configurePropertyNamingStrategyField(builder, strategy);
            }
        }
    }

    private static void configureModules(Jackson2ObjectMapperBuilder builder) {
        builder.modulesToInstall(MODULE_LIST.toArray(new Module[0]));
    }

    private static void configurePropertyNamingStrategyClass(Jackson2ObjectMapperBuilder builder,
                                                             Class<?> propertyNamingStrategyClass) {
        builder.propertyNamingStrategy(
                (PropertyNamingStrategy) BeanUtils.instantiateClass(propertyNamingStrategyClass));
    }

    private static void configurePropertyNamingStrategyField(Jackson2ObjectMapperBuilder builder, String fieldName) {
        // Find the field (this way we automatically support new constants
        // that may be added by Jackson in the future)
        Field field = ReflectionUtils.findField(PropertyNamingStrategy.class, fieldName,
                PropertyNamingStrategy.class);
        Assert.notNull(field, () -> "Constant named '" + fieldName + "' not found on "
                + PropertyNamingStrategy.class.getName());
        try {
            builder.propertyNamingStrategy((PropertyNamingStrategy) field.get(null));
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }


    public static Jackson2ObjectMapperBuilderCustomizer localDateTimeCustomizer(String format) {
        return builder ->
                builder.serializerByType(LocalDateTime.class, localDateTimeSerializer(format))
                        .deserializerByType(LocalDateTime.class, localDateTimeDeserializer(format));
    }


    public static Jackson2ObjectMapperBuilderCustomizer localDateCustomizer(String format) {
        return builder ->
                builder.serializerByType(LocalDate.class, localDateSerializer(format))
                        .deserializerByType(LocalDate.class, localDateDeserializer(format));
    }

    public static Jackson2ObjectMapperBuilderCustomizer localTimeCustomizer(String format) {
        return builder ->
                builder.serializerByType(LocalTime.class, localTimeSerializer(format))
                        .deserializerByType(LocalTime.class, localTimeDeserializer(format));
    }

    public static LocalDateTimeSerializer localDateTimeSerializer(String format) {
        return new LocalDateTimeSerializer(DateTimeUtils.dateTimeFormatter(format));
    }

    public static LocalDateTimeDeserializer localDateTimeDeserializer(String format) {
        return new LocalDateTimeDeserializer(DateTimeUtils.dateTimeFormatter(format));
    }

    public static LocalDateSerializer localDateSerializer(String format) {
        return new LocalDateSerializer(DateTimeUtils.dateTimeFormatter(format));
    }

    public static LocalDateDeserializer localDateDeserializer(String format) {
        return new LocalDateDeserializer(DateTimeUtils.dateTimeFormatter(format));
    }

    public static LocalTimeSerializer localTimeSerializer(String format) {
        return new LocalTimeSerializer(DateTimeUtils.dateTimeFormatter(format));
    }

    public static LocalTimeDeserializer localTimeDeserializer(String format) {
        return new LocalTimeDeserializer(DateTimeUtils.dateTimeFormatter(format));
    }

}
