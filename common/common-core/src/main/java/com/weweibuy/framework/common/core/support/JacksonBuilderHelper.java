package com.weweibuy.framework.common.core.support;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.ConfigFeature;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.weweibuy.framework.common.core.utils.DateTimeUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

/**
 * Jackson ObjectMapper 构建助手类
 * 
 * 提供统一的 Jackson ObjectMapper 配置和创建功能，支持：
 * - 序列化和反序列化特性的统一配置
 * - 日期时间格式化配置
 * - 属性命名策略配置
 * - 本地时间类型序列化器配置
 * 
 * @author durenhao
 * @date 2021/2/16 21:33
 **/
public class JacksonBuilderHelper {

    /**
     * Jackson 特性默认配置
     * 使用 ConfigFeature 接口作为 key，支持 SerializationFeature 和 DeserializationFeature
     */
    private static final Map<ConfigFeature, Boolean> FEATURE_DEFAULTS;

    /**
     * Jackson 模块列表
     * 包含默认的模块配置，如参数名称模块等
     */
    private static final Collection<Module> MODULE_LIST;

    /**
     * 静态初始化块
     * 配置默认的 Jackson 特性和模块
     */
    static {
        // 初始化特性默认配置
        Map<ConfigFeature, Boolean> featureDefaults = new HashMap<>();
        // 禁用日期时间序列化为时间戳
        featureDefaults.put(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        // 禁用持续时间序列化为时间戳
        featureDefaults.put(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS, false);
        // 启用枚举数字转换失败时抛出异常
        featureDefaults.put(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, true);
        FEATURE_DEFAULTS = Collections.unmodifiableMap(featureDefaults);
        
        // 初始化模块列表
        MODULE_LIST = new ArrayList<>();
        // 添加参数名称模块，支持构造函数参数名称映射
        MODULE_LIST.add(new ParameterNamesModule(JsonCreator.Mode.DEFAULT));
    }

    /**
     * 创建配置好的 ObjectMapper 实例
     * 
     * @param dateTimeFormat 日期时间格式字符串
     * @param dataFormat 日期格式字符串
     * @param timeFormat 时间格式字符串
     * @param propertyNamingStrategy 属性命名策略，可为 null
     * @return 配置好的 ObjectMapper 实例
     */
    public static ObjectMapper createObjectMapper(String dateTimeFormat, String dataFormat, String timeFormat, PropertyNamingStrategy propertyNamingStrategy) {
        ObjectMapper objectMapper = new ObjectMapper();
        
        // 配置默认特性
        configureFeatures(objectMapper, FEATURE_DEFAULTS);
        
        // 配置日期格式
        configureDateFormat(objectMapper, dateTimeFormat);
        
        // 配置命名策略
        if (propertyNamingStrategy != null) {
            objectMapper.setPropertyNamingStrategy(propertyNamingStrategy);
        }
        
        // 配置模块
        configureModules(objectMapper);
        
        // 配置本地时间序列化器
        configureLocalDateTimeSerializers(objectMapper, dateTimeFormat, dataFormat, timeFormat);
        
        return objectMapper;
    }

    /**
     * 配置 ObjectMapper 的特性
     * 使用 Stream API 过滤 null 值，并根据特性类型进行相应配置
     * 
     * @param objectMapper 要配置的 ObjectMapper
     * @param features 特性配置映射
     */
    private static void configureFeatures(ObjectMapper objectMapper, Map<ConfigFeature, Boolean> features) {
        features.entrySet().stream()
                .filter(entry -> entry.getValue() != null)  // 过滤掉 null 值
                .forEach(entry -> {
                    ConfigFeature feature = entry.getKey();
                    Boolean value = entry.getValue();
                    
                    // 根据特性类型进行相应配置
                    if (feature instanceof SerializationFeature) {
                        configureFeature(objectMapper, (SerializationFeature) feature, value);
                    } else if (feature instanceof DeserializationFeature) {
                        configureFeature(objectMapper, (DeserializationFeature) feature, value);
                    }
                });
    }

    /**
     * 配置序列化特性
     * 
     * @param objectMapper 要配置的 ObjectMapper
     * @param feature 序列化特性
     * @param enable 是否启用
     */
    private static void configureFeature(ObjectMapper objectMapper, SerializationFeature feature, boolean enable) {
        if (enable) {
            objectMapper.enable(feature);
        } else {
            objectMapper.disable(feature);
        }
    }

    /**
     * 配置反序列化特性
     * 
     * @param objectMapper 要配置的 ObjectMapper
     * @param feature 反序列化特性
     * @param enable 是否启用
     */
    private static void configureFeature(ObjectMapper objectMapper, DeserializationFeature feature, boolean enable) {
        if (enable) {
            objectMapper.enable(feature);
        } else {
            objectMapper.disable(feature);
        }
    }

    /**
     * 配置日期格式
     * 
     * @param objectMapper 要配置的 ObjectMapper
     * @param dateTimeFormat 日期时间格式字符串
     */
    private static void configureDateFormat(ObjectMapper objectMapper, String dateTimeFormat) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateTimeFormat);
        TimeZone timeZone = TimeZone.getDefault();
        simpleDateFormat.setTimeZone(timeZone);
        objectMapper.setDateFormat(simpleDateFormat);
    }

    /**
     * 配置 Jackson 模块
     * 
     * @param objectMapper 要配置的 ObjectMapper
     */
    private static void configureModules(ObjectMapper objectMapper) {
        for (Module module : MODULE_LIST) {
            objectMapper.registerModule(module);
        }
    }

    /**
     * 配置本地时间序列化器
     * 
     * @param objectMapper 要配置的 ObjectMapper
     * @param dateTimeFormat 日期时间格式
     * @param dataFormat 日期格式
     * @param timeFormat 时间格式
     */
    private static void configureLocalDateTimeSerializers(ObjectMapper objectMapper, String dateTimeFormat, String dataFormat, String timeFormat) {
        // 注册本地时间模块
        objectMapper.registerModule(createLocalDateTimeModule(dateTimeFormat, dataFormat, timeFormat));
    }

    /**
     * 创建本地时间模块
     * 配置 LocalDateTime、LocalDate、LocalTime 的序列化和反序列化器
     * 
     * @param dateTimeFormat 日期时间格式
     * @param dataFormat 日期格式
     * @param timeFormat 时间格式
     * @return 配置好的 SimpleModule
     */
    private static com.fasterxml.jackson.databind.module.SimpleModule createLocalDateTimeModule(String dateTimeFormat, String dataFormat, String timeFormat) {
        com.fasterxml.jackson.databind.module.SimpleModule module = new com.fasterxml.jackson.databind.module.SimpleModule();
        
        // 配置 LocalDateTime 序列化和反序列化器
        module.addSerializer(LocalDateTime.class, localDateTimeSerializer(dateTimeFormat));
        module.addDeserializer(LocalDateTime.class, localDateTimeDeserializer(dateTimeFormat));
        
        // 配置 LocalDate 序列化和反序列化器
        module.addSerializer(LocalDate.class, localDateSerializer(dataFormat));
        module.addDeserializer(LocalDate.class, localDateDeserializer(dataFormat));
        
        // 配置 LocalTime 序列化和反序列化器
        module.addSerializer(LocalTime.class, localTimeSerializer(timeFormat));
        module.addDeserializer(LocalTime.class, localTimeDeserializer(timeFormat));
        
        return module;
    }

    /**
     * 创建 LocalDateTime 序列化器
     * 
     * @param format 日期时间格式
     * @return LocalDateTime 序列化器
     */
    public static LocalDateTimeSerializer localDateTimeSerializer(String format) {
        return new LocalDateTimeSerializer(DateTimeUtils.dateTimeFormatter(format));
    }

    /**
     * 创建 LocalDateTime 反序列化器
     * 
     * @param format 日期时间格式
     * @return LocalDateTime 反序列化器
     */
    public static LocalDateTimeDeserializer localDateTimeDeserializer(String format) {
        return new LocalDateTimeDeserializer(DateTimeUtils.dateTimeFormatter(format));
    }

    /**
     * 创建 LocalDate 序列化器
     * 
     * @param format 日期格式
     * @return LocalDate 序列化器
     */
    public static LocalDateSerializer localDateSerializer(String format) {
        return new LocalDateSerializer(DateTimeUtils.dateTimeFormatter(format));
    }

    /**
     * 创建 LocalDate 反序列化器
     * 
     * @param format 日期格式
     * @return LocalDate 反序列化器
     */
    public static LocalDateDeserializer localDateDeserializer(String format) {
        return new LocalDateDeserializer(DateTimeUtils.dateTimeFormatter(format));
    }

    /**
     * 创建 LocalTime 序列化器
     * 
     * @param format 时间格式
     * @return LocalTime 序列化器
     */
    public static LocalTimeSerializer localTimeSerializer(String format) {
        return new LocalTimeSerializer(DateTimeUtils.dateTimeFormatter(format));
    }

    /**
     * 创建 LocalTime 反序列化器
     * 
     * @param format 时间格式
     * @return LocalTime 反序列化器
     */
    public static LocalTimeDeserializer localTimeDeserializer(String format) {
        return new LocalTimeDeserializer(DateTimeUtils.dateTimeFormatter(format));
    }

}
