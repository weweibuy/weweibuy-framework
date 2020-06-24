package com.weweibuy.framework.rocketmq.core.consumer;

import com.weweibuy.framework.rocketmq.annotation.Property;
import com.weweibuy.framework.rocketmq.constant.MessageExtPropertyConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.core.MethodParameter;

import java.util.HashMap;
import java.util.Map;

/**
 * Property 注解解析器
 *
 * @author durenhao
 * @date 2020/1/9 11:37
 **/
@Slf4j
public class PropertyMethodArgumentResolver implements HandlerMethodArgumentResolver {


    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Property.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, Object messageObject) {
        if (messageObject instanceof MessageExt) {
            MessageExt messageExt = (MessageExt) messageObject;
            Property header = parameter.getParameterAnnotation(Property.class);
            Map<String, String> stringMap = messageExtProperty(messageExt);
            if (StringUtils.isNotBlank(header.value())) {
                return stringMap.get(header.value());
            } else {
                return stringMap;
            }
        }
        log.warn("批量消息不支持 @Property 形式");
        return null;
    }

    /**
     * 获取 MessageExt 的部分属性
     *
     * @param messageExt
     * @return
     */
    private Map<String, String> messageExtProperty(MessageExt messageExt) {
        Map<String, String> hashMap = new HashMap<>(32);
        hashMap.put(MessageExtPropertyConstant.QUEUE_ID, messageExt.getQueueId() + "");
        hashMap.put(MessageExtPropertyConstant.STORE_SIZE, messageExt.getStoreSize() + "");
        hashMap.put(MessageExtPropertyConstant.QUEUE_OFFSET, messageExt.getQueueOffset() + "");
        hashMap.put(MessageExtPropertyConstant.SYS_FLAG, messageExt.getSysFlag() + "");
        hashMap.put(MessageExtPropertyConstant.BORN_TIME_STAMP, messageExt.getBornTimestamp() + "");
        hashMap.put(MessageExtPropertyConstant.STORE_TIME_STAMP, messageExt.getStoreTimestamp() + "");
        hashMap.put(MessageExtPropertyConstant.MSG_ID, messageExt.getMsgId());
        hashMap.put(MessageExtPropertyConstant.COMMIT_LOG_OFFSET, messageExt.getCommitLogOffset() + "");
        hashMap.put(MessageExtPropertyConstant.BODY_CRC, messageExt.getBodyCRC() + "");
        hashMap.put(MessageExtPropertyConstant.RECONSUME_TIMES, messageExt.getReconsumeTimes() + "");
        hashMap.put(MessageExtPropertyConstant.PREPARED_TRANSACTION_OFFSET, messageExt.getPreparedTransactionOffset() + "");
        Map<String, String> properties = messageExt.getProperties();
        hashMap.putAll(properties);
        return hashMap;
    }

}
