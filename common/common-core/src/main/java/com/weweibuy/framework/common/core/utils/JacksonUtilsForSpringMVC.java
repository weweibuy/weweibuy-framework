package com.weweibuy.framework.common.core.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;

/**
 * @author durenhao
 * @date 2020/2/26 20:38
 **/
@SuppressWarnings("unchecked")
public class JacksonUtilsForSpringMVC {

    private static ObjectMapper MVC_OBJECT_MAPPER;

    @Autowired
    private ObjectMapper objectMapper;

    @PostConstruct
    public void initMvc() {
        MVC_OBJECT_MAPPER = objectMapper;
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

    public static ObjectMapper getMvcObjectMapper() {
        return MVC_OBJECT_MAPPER;
    }

    public static JavaType javaType(ObjectMapper objectMapper, Class<?> clazz, Class<?>... typeClazz) {
        return objectMapper.getTypeFactory().constructParametricType(clazz, typeClazz);
    }

}
