package com.weweibuy.framework.common.core.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.weweibuy.framework.common.core.model.constant.CommonConstant;
import com.weweibuy.framework.common.core.support.JacksonBuilderHelper;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;

/**
 * @author durenhao
 * @date 2020/2/26 20:38
 **/
@SuppressWarnings("unchecked")
public class JacksonUtils {

    private static ObjectMapper CAMEL_CASE_MAPPER;

    private static ObjectMapper SNAKE_CASE_MAPPER;

    private static ObjectMapper UPPER_CAMEL_CASE_MAPPER;

    static {
        init();
    }

    private static void init() {
        CAMEL_CASE_MAPPER = JacksonBuilderHelper.createObjectMapper(
                CommonConstant.DateConstant.STANDARD_DATE_TIME_FORMAT_STR, 
                CommonConstant.DateConstant.STANDARD_DATE_FORMAT_STR,
                CommonConstant.DateConstant.STANDARD_TIME_FORMAT_STR,
                null
        );

        SNAKE_CASE_MAPPER = JacksonBuilderHelper.createObjectMapper(
                CommonConstant.DateConstant.STANDARD_DATE_TIME_FORMAT_STR,
                CommonConstant.DateConstant.STANDARD_DATE_FORMAT_STR,
                CommonConstant.DateConstant.STANDARD_TIME_FORMAT_STR,
                PropertyNamingStrategies.SNAKE_CASE
        );

        UPPER_CAMEL_CASE_MAPPER = JacksonBuilderHelper.createObjectMapper(
                CommonConstant.DateConstant.STANDARD_DATE_TIME_FORMAT_STR,
                CommonConstant.DateConstant.STANDARD_DATE_FORMAT_STR,
                CommonConstant.DateConstant.STANDARD_TIME_FORMAT_STR,
                PropertyNamingStrategies.UPPER_CAMEL_CASE
        );
    }

    public static ObjectMapper createObjectMapper(String dataTimeFormat, String dataFormat, String timeFormat, PropertyNamingStrategy propertyNamingStrategy) {
        return JacksonBuilderHelper.createObjectMapper(dataTimeFormat, dataFormat, timeFormat, propertyNamingStrategy);
    }

    public static <T> T readSnakeCaseValue(String json, Class<? extends T> clazz) {
        try {
            return SNAKE_CASE_MAPPER.readValue(json, clazz);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static <T> T readSnakeCaseValue(File file, Class<? extends T> clazz) {
        try {
            return SNAKE_CASE_MAPPER.readValue(file, clazz);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static <T> T readSnakeCaseValue(String json, TypeReference<T> valueTypeRef) {
        try {
            return SNAKE_CASE_MAPPER.readValue(json, valueTypeRef);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static <T> T readSnakeCaseValue(File file, TypeReference<T> valueTypeRef) {
        try {
            return SNAKE_CASE_MAPPER.readValue(file, valueTypeRef);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static <T> T readSnakeCaseValue(String json, JavaType javaType) {
        try {
            return SNAKE_CASE_MAPPER.readValue(json, javaType);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static <T> T readSnakeCaseValue(File file, JavaType javaType) {
        try {
            return SNAKE_CASE_MAPPER.readValue(file, javaType);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static <T> T readSnakeCaseValue(String json, Class<? extends T> clazz, Class<?>... typeClasses) {
        try {
            return SNAKE_CASE_MAPPER.readValue(json, javaType(SNAKE_CASE_MAPPER, clazz, typeClasses));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static <T> T readSnakeCaseValue(File file, Class<? extends T> clazz, Class<?>... typeClasses) {
        try {
            return SNAKE_CASE_MAPPER.readValue(file, javaType(SNAKE_CASE_MAPPER, clazz, typeClasses));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }


    public static <T> T readSnakeCaseValue(byte[] json, Class<? extends T> clazz) {
        try {
            return SNAKE_CASE_MAPPER.readValue(json, clazz);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static <T> T readSnakeCaseValue(byte[] json, TypeReference<T> valueTypeRef) {
        try {
            return SNAKE_CASE_MAPPER.readValue(json, valueTypeRef);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static <T> T readSnakeCaseValue(byte[] json, JavaType javaType) {
        try {
            return SNAKE_CASE_MAPPER.readValue(json, javaType);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }


    public static <T> T readSnakeCaseValue(byte[] json, Class<? extends T> clazz, Class<?>... typeClasses) {
        try {
            return SNAKE_CASE_MAPPER.readValue(json, javaType(SNAKE_CASE_MAPPER, clazz, typeClasses));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }


    public static String writeSnakeCase(Object object) {
        try {
            return SNAKE_CASE_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static byte[] writeSnakeCaseAsByte(Object object) {
        try {
            return SNAKE_CASE_MAPPER.writeValueAsBytes(object);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static <T> T readCamelCaseValue(String json, Class<? extends T> clazz) {
        try {
            return CAMEL_CASE_MAPPER.readValue(json, clazz);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static <T> T readCamelCaseValue(File file, Class<? extends T> clazz) {
        try {
            return CAMEL_CASE_MAPPER.readValue(file, clazz);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static <T> T readCamelCaseValue(String json, JavaType javaType) {
        try {
            return CAMEL_CASE_MAPPER.readValue(json, javaType);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static <T> T readCamelCaseValue(File file, JavaType javaType) {
        try {
            return CAMEL_CASE_MAPPER.readValue(file, javaType);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static <T> T readCamelCaseValue(String json, TypeReference<T> typeReference) {
        try {
            return CAMEL_CASE_MAPPER.readValue(json, typeReference);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static <T> T readCamelCaseValue(File file, TypeReference<T> typeReference) {
        try {
            return CAMEL_CASE_MAPPER.readValue(file, typeReference);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static <T> T readCamelCaseValue(String json, Class<? extends T> clazz, Class<?>... typeClasses) {
        try {
            return CAMEL_CASE_MAPPER.readValue(json, javaType(CAMEL_CASE_MAPPER, clazz, typeClasses));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static <T> T readCamelCaseValue(File file, Class<? extends T> clazz, Class<?>... typeClasses) {
        try {
            return CAMEL_CASE_MAPPER.readValue(file, javaType(CAMEL_CASE_MAPPER, clazz, typeClasses));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static <T> T readCamelCaseValue(byte[] json, Class<? extends T> clazz) {
        try {
            return CAMEL_CASE_MAPPER.readValue(json, clazz);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static <T> T readCamelCaseValue(byte[] json, JavaType javaType) {
        try {
            return CAMEL_CASE_MAPPER.readValue(json, javaType);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static <T> T readCamelCaseValue(byte[] json, TypeReference<T> typeReference) {
        try {
            return CAMEL_CASE_MAPPER.readValue(json, typeReference);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }


    public static <T> T readCamelCaseValue(byte[] json, Class<? extends T> clazz, Class<?>... typeClasses) {
        try {
            return CAMEL_CASE_MAPPER.readValue(json, javaType(CAMEL_CASE_MAPPER, clazz, typeClasses));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static String writeCamelCase(Object object) {
        try {
            return CAMEL_CASE_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static byte[] writeCamelCaseAsByte(Object object) {
        try {
            return CAMEL_CASE_MAPPER.writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
            throw new UncheckedIOException(e);
        }
    }


    public static <T> T readValue(String json, Class<? extends T> clazz) {
        try {
            return CAMEL_CASE_MAPPER.readValue(json, clazz);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static <T> T readValue(File file, Class<? extends T> clazz) {
        try {
            return CAMEL_CASE_MAPPER.readValue(file, clazz);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static <T> T readValue(String json, JavaType javaType) {
        try {
            return CAMEL_CASE_MAPPER.readValue(json, javaType);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static <T> T readValue(File file, JavaType javaType) {
        try {
            return CAMEL_CASE_MAPPER.readValue(file, javaType);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static <T> T readValue(String json, TypeReference<T> typeReference) {
        try {
            return CAMEL_CASE_MAPPER.readValue(json, typeReference);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static <T> T readValue(File file, TypeReference<T> typeReference) {
        try {
            return CAMEL_CASE_MAPPER.readValue(file, typeReference);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static <T> T readValue(String json, Class<? extends T> clazz, Class<?>... typeClasses) {
        try {
            return CAMEL_CASE_MAPPER.readValue(json, javaType(CAMEL_CASE_MAPPER, clazz, typeClasses));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static <T> T readValue(File file, Class<? extends T> clazz, Class<?>... typeClasses) {
        try {
            return CAMEL_CASE_MAPPER.readValue(file, javaType(CAMEL_CASE_MAPPER, clazz, typeClasses));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static <T> T readValue(byte[] json, Class<? extends T> clazz) {
        try {
            return CAMEL_CASE_MAPPER.readValue(json, clazz);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static <T> T readValue(byte[] json, JavaType javaType) {
        try {
            return CAMEL_CASE_MAPPER.readValue(json, javaType);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static <T> T readValue(byte[] json, TypeReference<T> typeReference) {
        try {
            return CAMEL_CASE_MAPPER.readValue(json, typeReference);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }


    public static <T> T readValue(byte[] json, Class<? extends T> clazz, Class<?>... typeClasses) {
        try {
            return CAMEL_CASE_MAPPER.readValue(json, javaType(CAMEL_CASE_MAPPER, clazz, typeClasses));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static String writeValue(Object object) {
        try {
            return CAMEL_CASE_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static byte[] writeAsByte(Object object) {
        try {
            return CAMEL_CASE_MAPPER.writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
            throw new UncheckedIOException(e);
        }
    }

    // ==================== Convert Value Methods ====================

    /**
     * 使用驼峰命名策略的 ObjectMapper 进行对象转换
     * 
     * @param fromValue 源对象
     * @param toValueType 目标类型
     * @param <T> 目标类型泛型
     * @return 转换后的对象
     */
    public static <T> T convertValue(Object fromValue, Class<T> toValueType) {
        try {
            return CAMEL_CASE_MAPPER.convertValue(fromValue, toValueType);
        } catch (IllegalArgumentException e) {
            throw new UncheckedIOException(new IOException("Convert value failed", e));
        }
    }

    /**
     * 使用驼峰命名策略的 ObjectMapper 进行对象转换
     * 
     * @param fromValue 源对象
     * @param toValueType 目标类型引用
     * @param <T> 目标类型泛型
     * @return 转换后的对象
     */
    public static <T> T convertValue(Object fromValue, TypeReference<T> toValueType) {
        try {
            return CAMEL_CASE_MAPPER.convertValue(fromValue, toValueType);
        } catch (IllegalArgumentException e) {
            throw new UncheckedIOException(new IOException("Convert value failed", e));
        }
    }

    /**
     * 使用驼峰命名策略的 ObjectMapper 进行对象转换
     * 
     * @param fromValue 源对象
     * @param toValueType 目标 Java 类型
     * @param <T> 目标类型泛型
     * @return 转换后的对象
     */
    public static <T> T convertValue(Object fromValue, JavaType toValueType) {
        try {
            return CAMEL_CASE_MAPPER.convertValue(fromValue, toValueType);
        } catch (IllegalArgumentException e) {
            throw new UncheckedIOException(new IOException("Convert value failed", e));
        }
    }

    /**
     * 使用下划线命名策略的 ObjectMapper 进行对象转换
     * 
     * @param fromValue 源对象
     * @param toValueType 目标类型
     * @param <T> 目标类型泛型
     * @return 转换后的对象
     */
    public static <T> T convertSnakeCaseValue(Object fromValue, Class<T> toValueType) {
        try {
            return SNAKE_CASE_MAPPER.convertValue(fromValue, toValueType);
        } catch (IllegalArgumentException e) {
            throw new UncheckedIOException(new IOException("Convert snake case value failed", e));
        }
    }

    /**
     * 使用下划线命名策略的 ObjectMapper 进行对象转换
     * 
     * @param fromValue 源对象
     * @param toValueType 目标类型引用
     * @param <T> 目标类型泛型
     * @return 转换后的对象
     */
    public static <T> T convertSnakeCaseValue(Object fromValue, TypeReference<T> toValueType) {
        try {
            return SNAKE_CASE_MAPPER.convertValue(fromValue, toValueType);
        } catch (IllegalArgumentException e) {
            throw new UncheckedIOException(new IOException("Convert snake case value failed", e));
        }
    }

    /**
     * 使用下划线命名策略的 ObjectMapper 进行对象转换
     * 
     * @param fromValue 源对象
     * @param toValueType 目标 Java 类型
     * @param <T> 目标类型泛型
     * @return 转换后的对象
     */
    public static <T> T convertSnakeCaseValue(Object fromValue, JavaType toValueType) {
        try {
            return SNAKE_CASE_MAPPER.convertValue(fromValue, toValueType);
        } catch (IllegalArgumentException e) {
            throw new UncheckedIOException(new IOException("Convert snake case value failed", e));
        }
    }

    /**
     * 使用首字母大写驼峰命名策略的 ObjectMapper 进行对象转换
     * 
     * @param fromValue 源对象
     * @param toValueType 目标类型
     * @param <T> 目标类型泛型
     * @return 转换后的对象
     */
    public static <T> T convertUpperCamelCaseValue(Object fromValue, Class<T> toValueType) {
        try {
            return UPPER_CAMEL_CASE_MAPPER.convertValue(fromValue, toValueType);
        } catch (IllegalArgumentException e) {
            throw new UncheckedIOException(new IOException("Convert upper camel case value failed", e));
        }
    }

    /**
     * 使用首字母大写驼峰命名策略的 ObjectMapper 进行对象转换
     * 
     * @param fromValue 源对象
     * @param toValueType 目标类型引用
     * @param <T> 目标类型泛型
     * @return 转换后的对象
     */
    public static <T> T convertUpperCamelCaseValue(Object fromValue, TypeReference<T> toValueType) {
        try {
            return UPPER_CAMEL_CASE_MAPPER.convertValue(fromValue, toValueType);
        } catch (IllegalArgumentException e) {
            throw new UncheckedIOException(new IOException("Convert upper camel case value failed", e));
        }
    }

    /**
     * 使用首字母大写驼峰命名策略的 ObjectMapper 进行对象转换
     * 
     * @param fromValue 源对象
     * @param toValueType 目标 Java 类型
     * @param <T> 目标类型泛型
     * @return 转换后的对象
     */
    public static <T> T convertUpperCamelCaseValue(Object fromValue, JavaType toValueType) {
        try {
            return UPPER_CAMEL_CASE_MAPPER.convertValue(fromValue, toValueType);
        } catch (IllegalArgumentException e) {
            throw new UncheckedIOException(new IOException("Convert upper camel case value failed", e));
        }
    }


    public static ObjectMapper getCamelCaseMapper() {
        return CAMEL_CASE_MAPPER;
    }

    public static ObjectMapper getSnakeCaseMapper() {
        return SNAKE_CASE_MAPPER;
    }

    public static ObjectMapper getUpperCamelCaseMapper() {
        return UPPER_CAMEL_CASE_MAPPER;
    }

    public static JavaType javaType(ObjectMapper objectMapper, Class<?> clazz, Class<?>... typeClazz) {
        return objectMapper.getTypeFactory().constructParametricType(clazz, typeClazz);
    }

    public static JavaType javaType(Class<?> clazz, Class<?>... typeClazz) {
        return CAMEL_CASE_MAPPER.getTypeFactory().constructParametricType(clazz, typeClazz);
    }

    public static JavaType javaType(TypeReference<?> typeReference) {
        return CAMEL_CASE_MAPPER.getTypeFactory().constructType(typeReference);
    }

}
