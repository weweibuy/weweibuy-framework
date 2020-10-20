package com.weweibuy.framework.common.mvc.date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.weweibuy.framework.common.core.model.eum.DateTypeEum;
import com.weweibuy.framework.common.core.utils.DateTimeUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * @author durenhao
 * @date 2019/9/12 22:19
 **/
@Slf4j
public class LongDateSerialize extends JsonSerializer<Long> implements ContextualSerializer {

    private DateTimeFormatter formatter;

    private DateTypeEum dateTypeEum;

    public LongDateSerialize() {
    }

    public LongDateSerialize(String format , DateTypeEum dateTypeEum) {
        this.formatter = DateTimeFormatter.ofPattern(format);
        this.dateTypeEum = dateTypeEum;
    }

    @Override
    public void serialize(Long value, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
        jsonGenerator.writeString(formatter.format(DateTimeUtils.timestampMilliToLocalDateTime(value)));
    }

    /**
     * 该方法只会初始化一次
     * {@link SerializerProvider#_findExplicitUntypedSerializer(Class)}
     *
     * @param serializerProvider
     * @param beanProperty
     * @return
     * @throws JsonMappingException
     */
    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        if (beanProperty != null && Objects.equals(beanProperty.getType().getRawClass(), long.class)) {
            // 非 Long 类直接跳过
            LongData longData = beanProperty.getAnnotation(LongData.class);
            if (longData == null) {
                longData = beanProperty.getContextAnnotation(LongData.class);
            }
            // 如果能得到注解，就将注解的 value 传入 LongDateSerialize
            if (longData != null) {
                return new LongDateSerialize(longData.format(), longData.dateType());
            }
        }
        return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
    }



}
