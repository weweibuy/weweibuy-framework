package com.weiweibuy.framework.common.mvc.desensitization;

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
import java.util.regex.Matcher;

/**
 * @author durenhao
 * @date 2019/9/12 22:19
 **/
@Slf4j
public class SensitiveInfoSerialize extends JsonSerializer<String> implements ContextualSerializer {

    private SensitiveDataTypeEum sensitiveDataType;

    public SensitiveInfoSerialize() {
    }

    public SensitiveInfoSerialize(SensitiveDataTypeEum sensitiveDataType) {
        this.sensitiveDataType = sensitiveDataType;
    }

    @Override
    public void serialize(String value, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
        jsonGenerator.writeString(replaceStr(value));
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        if (beanProperty != null) {
            if (Objects.equals(beanProperty.getType().getRawClass(), String.class)) { // 非 String 类直接跳过
                SensitiveData sensitiveInfo = beanProperty.getAnnotation(SensitiveData.class);
                if (sensitiveInfo == null) {
                    sensitiveInfo = beanProperty.getContextAnnotation(SensitiveData.class);
                }
                if (sensitiveInfo != null) { // 如果能得到注解，就将注解的 value 传入 SensitiveInfoSerialize
                    return new SensitiveInfoSerialize(sensitiveInfo.type());
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
        Matcher matcher = sensitiveDataType.getPatten().matcher(str);
        if (matcher.find()) {
            return matcher.replaceAll(sensitiveDataType.getReplace());
        }
        return str;
    }

}
