package com.weweibuy.framework.rocketmq.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * MessageExt 消息属性常量
 *
 * @author durenhao
 * @date 2020/6/24 22:45
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MessageExtPropertyConstant {

    public static final String QUEUE_ID = "queueId";

    public static final String STORE_SIZE = "storeSize";

    public static final String QUEUE_OFFSET = "queueOffset";

    public static final String SYS_FLAG = "sysFlag";

    public static final String BORN_TIME_STAMP = "bornTimestamp";

    public static final String STORE_TIME_STAMP = "storeTimestamp";

    public static final String MSG_ID = "msgId";

    public static final String COMMIT_LOG_OFFSET = "commitLogOffset";

    public static final String BODY_CRC = "bodyCRC";

    public static final String RECONSUME_TIMES = "reconsumeTimes";

    public static final String PREPARED_TRANSACTION_OFFSET = "preparedTransactionOffset";

    public static final Set<String> MESSAGE_EXT_PROPERTIES_SET = new HashSet<>(16);

    static {
        MESSAGE_EXT_PROPERTIES_SET.add(QUEUE_ID);
        MESSAGE_EXT_PROPERTIES_SET.add(QUEUE_OFFSET);
        MESSAGE_EXT_PROPERTIES_SET.add(STORE_SIZE);
        MESSAGE_EXT_PROPERTIES_SET.add(SYS_FLAG);
        MESSAGE_EXT_PROPERTIES_SET.add(BORN_TIME_STAMP);
        MESSAGE_EXT_PROPERTIES_SET.add(STORE_TIME_STAMP);
        MESSAGE_EXT_PROPERTIES_SET.add(MSG_ID);
        MESSAGE_EXT_PROPERTIES_SET.add(COMMIT_LOG_OFFSET);
        MESSAGE_EXT_PROPERTIES_SET.add(BODY_CRC);
        MESSAGE_EXT_PROPERTIES_SET.add(RECONSUME_TIMES);
        MESSAGE_EXT_PROPERTIES_SET.add(PREPARED_TRANSACTION_OFFSET);
    }
}
