package com.weweibuy.framework.common.mvc.desensitization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * @author durenhao
 * @date 2019/9/12 22:19
 **/
@Slf4j
public class SensitiveInfoSerialize extends JsonSerializer<String> implements ContextualSerializer {

    private Pattern patten;

    private String replace;

    public SensitiveInfoSerialize() {
    }

    public SensitiveInfoSerialize(Pattern patten, String replace) {
        this.patten = patten;
        this.replace = replace;
    }

    @Override
    public void serialize(String value, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
        jsonGenerator.writeString(replaceStr(value));
    }

    /**
     * 该方法只会初始化一次
     * {@link SerializerProvider#_findExplicitUntypedSerializer(java.lang.Class)}
     *
     * @param serializerProvider
     * @param beanProperty
     * @return
     * @throws JsonMappingException
     */
    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        if (beanProperty != null) {
            if (Objects.equals(beanProperty.getType().getRawClass(), String.class)) { // 非 String 类直接跳过
                SensitiveData sensitiveInfo = beanProperty.getAnnotation(SensitiveData.class);
                if (sensitiveInfo == null) {
                    sensitiveInfo = beanProperty.getContextAnnotation(SensitiveData.class);
                }
                if (sensitiveInfo != null) { // 如果能得到注解，就将注解的 value 传入 SensitiveInfoSerialize
                    return new SensitiveInfoSerialize(Pattern.compile(sensitiveInfo.patten()), sensitiveInfo.replace());
                }
            }
            return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
        }
        return serializerProvider.findNullValueSerializer(beanProperty);
    }

    private String replaceStr(String str) {
        if (StringUtils.isBlank(str)) {
            return str;
        }
        return patten.matcher(str)
                .replaceAll(replace);
    }

}
