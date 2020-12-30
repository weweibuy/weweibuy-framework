package com.weweibuy.framework.common.core.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.weweibuy.framework.common.core.exception.Exceptions;
import com.weweibuy.framework.common.core.model.eum.CommonErrorCodeEum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * @author durenhao
 * @date 2020/2/26 20:38
 **/
@Component
public class JackJsonUtils {

    private static ObjectMapper CAMEL_CASE_MAPPER;

    private static ObjectMapper SNAKE_CASE_MAPPER;

    private static ObjectMapper MVC_OBJECT_MAPPER;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Jackson2ObjectMapperBuilder objectMapperBuilder;

    @PostConstruct
    public void init() {
        MVC_OBJECT_MAPPER = objectMapper;
        PropertyNamingStrategy propertyNamingStrategy = objectMapper.getPropertyNamingStrategy();

        if (propertyNamingStrategy == null ||
                propertyNamingStrategy.getClass().isAssignableFrom(PropertyNamingStrategy.LOWER_CAMEL_CASE.getClass())) {
            CAMEL_CASE_MAPPER = objectMapper;
            SNAKE_CASE_MAPPER = objectMapperBuilder.createXmlMapper(false)
                    .propertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                    .build();
        } else if (propertyNamingStrategy.getClass().isAssignableFrom(PropertyNamingStrategy.SNAKE_CASE.getClass())) {
            SNAKE_CASE_MAPPER = objectMapper;
            CAMEL_CASE_MAPPER = objectMapperBuilder.createXmlMapper(false)
                    .propertyNamingStrategy(PropertyNamingStrategy.LOWER_CAMEL_CASE)
                    .build();
        } else {
            throw new IllegalStateException("Jackson objectMapper NamingStrategy 为: " + propertyNamingStrategy.getClass().getSimpleName() + "目前不支持");
        }
    }

    public static <T> T readSnakeCaseValue(String json, Class<? extends T> clazz) {
        try {
            return SNAKE_CASE_MAPPER.readValue(json, clazz);
        } catch (IOException e) {
            throw Exceptions.system(CommonErrorCodeEum.JSON_PARSE_EXCEPTION, e);
        }
    }


    public static <T> T readSnakeCaseValue(String json, TypeReference<T> valueTypeRef) {
        try {
            return SNAKE_CASE_MAPPER.readValue(json, valueTypeRef);
        } catch (IOException e) {
            throw Exceptions.system(CommonErrorCodeEum.JSON_PARSE_EXCEPTION, e);
        }
    }

    public static <T> T readSnakeCaseValue(String json, JavaType javaType) {
        try {
            return SNAKE_CASE_MAPPER.readValue(json, javaType);
        } catch (IOException e) {
            throw Exceptions.system(CommonErrorCodeEum.JSON_PARSE_EXCEPTION, e);
        }
    }

    public static <T> T readSnakeCaseValue(String json, Class<? extends T> clazz, Class<?>... typeClasses) {
        try {
            return SNAKE_CASE_MAPPER.readValue(json, javaType(SNAKE_CASE_MAPPER, clazz, typeClasses));
        } catch (IOException e) {
            throw Exceptions.system(CommonErrorCodeEum.JSON_PARSE_EXCEPTION, e);
        }
    }


    public static <T> T readSnakeCaseValue(byte[] json, Class<? extends T> clazz) {
        try {
            return SNAKE_CASE_MAPPER.readValue(json, clazz);
        } catch (IOException e) {
            throw Exceptions.system(CommonErrorCodeEum.JSON_PARSE_EXCEPTION, e);
        }
    }

    public static <T> T readSnakeCaseValue(byte[] json, TypeReference<T> valueTypeRef) {
        try {
            return SNAKE_CASE_MAPPER.readValue(json, valueTypeRef);
        } catch (IOException e) {
            throw Exceptions.system(CommonErrorCodeEum.JSON_PARSE_EXCEPTION, e);
        }
    }

    public static <T> T readSnakeCaseValue(byte[] json, JavaType javaType) {
        try {
            return SNAKE_CASE_MAPPER.readValue(json, javaType);
        } catch (IOException e) {
            throw Exceptions.system(CommonErrorCodeEum.JSON_PARSE_EXCEPTION, e);
        }
    }


    public static <T> T readSnakeCaseValue(byte[] json, Class<? extends T> clazz, Class<?>... typeClasses) {
        try {
            return SNAKE_CASE_MAPPER.readValue(json, javaType(SNAKE_CASE_MAPPER, clazz, typeClasses));
        } catch (IOException e) {
            throw Exceptions.system(CommonErrorCodeEum.JSON_PARSE_EXCEPTION, e);
        }
    }


    public static String writeSnakeCase(Object object) {
        try {
            return SNAKE_CASE_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw Exceptions.system(CommonErrorCodeEum.JSON_PARSE_EXCEPTION, e);
        }
    }

    public static byte[] writeSnakeCaseAsByte(Object object) {
        try {
            return SNAKE_CASE_MAPPER.writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
            throw Exceptions.system(CommonErrorCodeEum.JSON_PARSE_EXCEPTION, e);
        }
    }

    public static <T> T readValue(String json, Class<? extends T> clazz) {
        try {
            return MVC_OBJECT_MAPPER.readValue(json, clazz);
        } catch (IOException e) {
            throw Exceptions.system(CommonErrorCodeEum.JSON_PARSE_EXCEPTION, e);
        }
    }

    public static <T> T readValue(String json, JavaType javaType) {
        try {
            return MVC_OBJECT_MAPPER.readValue(json, javaType);
        } catch (IOException e) {
            throw Exceptions.system(CommonErrorCodeEum.JSON_PARSE_EXCEPTION, e);
        }
    }

    public static <T> T readValue(String json, Class<? extends T> clazz, Class<?>... typeClasses) {
        try {
            return MVC_OBJECT_MAPPER.readValue(json, javaType(MVC_OBJECT_MAPPER, clazz, typeClasses));
        } catch (IOException e) {
            throw Exceptions.system(CommonErrorCodeEum.JSON_PARSE_EXCEPTION, e);
        }
    }

    public static <T> T readValue(String json, TypeReference<T> typeReference) {
        try {
            return MVC_OBJECT_MAPPER.readValue(json, typeReference);
        } catch (IOException e) {
            throw Exceptions.system(CommonErrorCodeEum.JSON_PARSE_EXCEPTION, e);
        }
    }

    public static <T> T readValue(byte[] json, Class<? extends T> clazz) {
        try {
            return MVC_OBJECT_MAPPER.readValue(json, clazz);
        } catch (IOException e) {
            throw Exceptions.system(CommonErrorCodeEum.JSON_PARSE_EXCEPTION, e);
        }
    }

    public static <T> T readValue(byte[] json, JavaType javaType) {
        try {
            return MVC_OBJECT_MAPPER.readValue(json, javaType);
        } catch (IOException e) {
            throw Exceptions.system(CommonErrorCodeEum.JSON_PARSE_EXCEPTION, e);
        }
    }

    public static <T> T readValue(byte[] json, TypeReference<T> typeReference) {
        try {
            return MVC_OBJECT_MAPPER.readValue(json, typeReference);
        } catch (IOException e) {
            throw Exceptions.system(CommonErrorCodeEum.JSON_PARSE_EXCEPTION, e);
        }
    }

    public static <T> T readValue(byte[] json, Class<? extends T> clazz, Class<?>... typeClasses) {
        try {
            return MVC_OBJECT_MAPPER.readValue(json, javaType(MVC_OBJECT_MAPPER, clazz, typeClasses));
        } catch (IOException e) {
            throw Exceptions.system(CommonErrorCodeEum.JSON_PARSE_EXCEPTION, e);
        }
    }


    public static String write(Object object) {
        try {
            return MVC_OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw Exceptions.system(CommonErrorCodeEum.JSON_WRITE_EXCEPTION, e);
        }
    }

    public static byte[] writeAsByte(Object object) {
        try {
            return MVC_OBJECT_MAPPER.writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
            throw Exceptions.system(CommonErrorCodeEum.JSON_WRITE_EXCEPTION, e);
        }
    }


    public static <T> T readCamelCaseValue(String json, Class<? extends T> clazz) {
        try {
            return CAMEL_CASE_MAPPER.readValue(json, clazz);
        } catch (IOException e) {
            throw Exceptions.system(CommonErrorCodeEum.JSON_WRITE_EXCEPTION, e);
        }
    }

    public static <T> T readCamelCaseValue(String json, JavaType javaType) {
        try {
            return CAMEL_CASE_MAPPER.readValue(json, javaType);
        } catch (IOException e) {
            throw Exceptions.system(CommonErrorCodeEum.JSON_WRITE_EXCEPTION, e);
        }
    }

    public static <T> T readCamelCaseValue(String json, TypeReference<T> typeReference) {
        try {
            return CAMEL_CASE_MAPPER.readValue(json, typeReference);
        } catch (IOException e) {
            throw Exceptions.system(CommonErrorCodeEum.JSON_WRITE_EXCEPTION, e);
        }
    }

    public static <T> T readCamelCaseValue(String json, Class<? extends T> clazz, Class<?>... typeClasses) {
        try {
            return CAMEL_CASE_MAPPER.readValue(json, javaType(CAMEL_CASE_MAPPER, clazz, typeClasses));
        } catch (IOException e) {
            throw Exceptions.system(CommonErrorCodeEum.JSON_WRITE_EXCEPTION, e);
        }
    }

    public static <T> T readCamelCaseValue(byte[] json, Class<? extends T> clazz) {
        try {
            return CAMEL_CASE_MAPPER.readValue(json, clazz);
        } catch (IOException e) {
            throw Exceptions.system(CommonErrorCodeEum.JSON_WRITE_EXCEPTION, e);
        }
    }

    public static <T> T readCamelCaseValue(byte[] json, JavaType javaType) {
        try {
            return CAMEL_CASE_MAPPER.readValue(json, javaType);
        } catch (IOException e) {
            throw Exceptions.system(CommonErrorCodeEum.JSON_WRITE_EXCEPTION, e);
        }
    }

    public static <T> T readCamelCaseValue(byte[] json, TypeReference<T> typeReference) {
        try {
            return CAMEL_CASE_MAPPER.readValue(json, typeReference);
        } catch (IOException e) {
            throw Exceptions.system(CommonErrorCodeEum.JSON_WRITE_EXCEPTION, e);
        }
    }


    public static <T> T readCamelCaseValue(byte[] json, Class<? extends T> clazz, Class<?>... typeClasses) {
        try {
            return CAMEL_CASE_MAPPER.readValue(json, javaType(CAMEL_CASE_MAPPER, clazz, typeClasses));
        } catch (IOException e) {
            throw Exceptions.system(CommonErrorCodeEum.JSON_WRITE_EXCEPTION, e);
        }
    }

    public static String writeCamelCase(Object object) {
        try {
            return CAMEL_CASE_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw Exceptions.system(CommonErrorCodeEum.JSON_WRITE_EXCEPTION, e);
        }
    }

    public static byte[] writeCamelCaseAsByte(Object object) {
        try {
            return CAMEL_CASE_MAPPER.writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
            throw Exceptions.system(CommonErrorCodeEum.JSON_WRITE_EXCEPTION, e);
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

    public static JavaType javaType(ObjectMapper objectMapper, Class<?> clazz, Class<?>... typeClazz) {
        return objectMapper.getTypeFactory().constructParametricType(clazz, typeClazz);
    }

}
