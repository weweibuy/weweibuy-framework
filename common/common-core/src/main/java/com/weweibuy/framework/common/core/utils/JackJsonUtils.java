package com.weweibuy.framework.common.core.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.weweibuy.framework.common.core.model.constant.CommonConstant;
import com.weweibuy.framework.common.core.support.JacksonBuilderHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;

/**
 * @author durenhao
 * @date 2020/2/26 20:38
 **/
@SuppressWarnings("unchecked")
public class JackJsonUtils {

    private static ObjectMapper CAMEL_CASE_MAPPER;

    private static ObjectMapper SNAKE_CASE_MAPPER;

    private static ObjectMapper MVC_OBJECT_MAPPER;

    private static ObjectMapper UPPER_CAMEL_CASE_MAPPER;

    /***
     * MVC 使用的名称风格
     */
    private static String mvcNamingStrategy;

    @Autowired
    private ObjectMapper objectMapper;

    static {
        Jackson2ObjectMapperBuilder objectMapperBuilder =
                JacksonBuilderHelper.objectMapperBuilder(CommonConstant.DateConstant.STANDARD_DATE_TIME_FORMAT_STR, CommonConstant.DateConstant.STANDARD_DATE_FORMAT_STR);
        init(objectMapperBuilder);
    }

    @PostConstruct
    public void initMvc() {
        MVC_OBJECT_MAPPER = objectMapper;
        PropertyNamingStrategy propertyNamingStrategy = objectMapper.getPropertyNamingStrategy();
        if (propertyNamingStrategy == null ||
                propertyNamingStrategy.getClass().isAssignableFrom(PropertyNamingStrategies.LOWER_CAMEL_CASE.getClass())) {
            mvcNamingStrategy = "LOWER_CAMEL_CASE";
        } else if (propertyNamingStrategy.getClass().isAssignableFrom(PropertyNamingStrategies.SNAKE_CASE.getClass())) {
            mvcNamingStrategy = "SNAKE_CASE";
        }
    }

    public static ObjectMapper createObjectMapper(String dataTimeFormat, String dataFormat, PropertyNamingStrategy propertyNamingStrategy) {
        return createObjectMapper(dataTimeFormat, dataFormat, CommonConstant.DateConstant.STANDARD_TIME_FORMAT_STR, propertyNamingStrategy);
    }

    /**
     * 创建 objectMapper
     *
     * @param dataTimeFormat
     * @param dataFormat
     * @param propertyNamingStrategy
     * @return
     */
    public static ObjectMapper createObjectMapper(String dataTimeFormat, String dataFormat, String timeFormat, PropertyNamingStrategy propertyNamingStrategy) {
        Jackson2ObjectMapperBuilder objectMapperBuilder =
                JacksonBuilderHelper.objectMapperBuilder(dataTimeFormat,
                        dataFormat, timeFormat);
        if (propertyNamingStrategy != null) {
            return objectMapperBuilder.createXmlMapper(false)
                    .propertyNamingStrategy(propertyNamingStrategy)
                    .build();
        }
        return objectMapperBuilder.createXmlMapper(false)
                .build();
    }


    private static void init(Jackson2ObjectMapperBuilder objectMapperBuilder) {

        CAMEL_CASE_MAPPER = objectMapperBuilder.createXmlMapper(false)
                .build();

        SNAKE_CASE_MAPPER = objectMapperBuilder.createXmlMapper(false)
                .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
                .build();

        UPPER_CAMEL_CASE_MAPPER = objectMapperBuilder.createXmlMapper(false)
                .propertyNamingStrategy(PropertyNamingStrategies.UPPER_CAMEL_CASE)
                .build();

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

    public static <T> T readValueWithMvc(File file, Class<? extends T> clazz) {
        try {
            return MVC_OBJECT_MAPPER.readValue(file, clazz);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static <T> T readValueWithMvc(String json, Class<? extends T> clazz) {
        try {
            return MVC_OBJECT_MAPPER.readValue(json, clazz);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static <T> T readValueWithMvc(String json, JavaType javaType) {
        try {
            return MVC_OBJECT_MAPPER.readValue(json, javaType);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static <T> T readValueWithMvc(File file, JavaType javaType) {
        try {
            return MVC_OBJECT_MAPPER.readValue(file, javaType);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static <T> T readValueWithMvc(String json, Class<? extends T> clazz, Class<?>... typeClasses) {
        try {
            return MVC_OBJECT_MAPPER.readValue(json, javaType(MVC_OBJECT_MAPPER, clazz, typeClasses));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static <T> T readValueWithMvc(File file, Class<? extends T> clazz, Class<?>... typeClasses) {
        try {
            return MVC_OBJECT_MAPPER.readValue(file, javaType(MVC_OBJECT_MAPPER, clazz, typeClasses));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static <T> T readValueWithMvc(String json, TypeReference<T> typeReference) {
        try {
            return MVC_OBJECT_MAPPER.readValue(json, typeReference);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static <T> T readValueWithMvc(File file, TypeReference<T> typeReference) {
        try {
            return MVC_OBJECT_MAPPER.readValue(file, typeReference);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static <T> T readValueWithMvc(byte[] json, Class<? extends T> clazz) {
        try {
            return MVC_OBJECT_MAPPER.readValue(json, clazz);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static <T> T readValueWithMvc(byte[] json, JavaType javaType) {
        try {
            return MVC_OBJECT_MAPPER.readValue(json, javaType);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static <T> T readValueWithMvc(byte[] json, TypeReference<T> typeReference) {
        try {
            return MVC_OBJECT_MAPPER.readValue(json, typeReference);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static <T> T readValueWithMvc(byte[] json, Class<? extends T> clazz, Class<?>... typeClasses) {
        try {
            return MVC_OBJECT_MAPPER.readValue(json, javaType(MVC_OBJECT_MAPPER, clazz, typeClasses));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }


    public static String writeWithMvc(Object object) {
        try {
            return MVC_OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static byte[] writeAsByteWithMvc(Object object) {
        try {
            return MVC_OBJECT_MAPPER.writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
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


    public static ObjectMapper getCamelCaseMapper() {
        return CAMEL_CASE_MAPPER;
    }

    public static ObjectMapper getSnakeCaseMapper() {
        return SNAKE_CASE_MAPPER;
    }

    public static ObjectMapper getMvcObjectMapper() {
        return MVC_OBJECT_MAPPER;
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
