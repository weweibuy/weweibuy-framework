package com.weweibuy.framework.lb.mq;

import com.weweibuy.framework.lb.support.LoadBalanceOperator;
import com.weweibuy.framework.rocketmq.annotation.RocketListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.stereotype.Component;

/**
 * @author durenhao
 * @date 2020/3/7 20:42
 **/
@Slf4j
@Component
@RocketListener(topic = "${common.lb.server-change-listener.topic}", group = "${common.lb.server-change-listener.group}",
        messageModel = MessageModel.BROADCASTING, threadMax = 1, threadMin = 1)
public class RocketServerChangeListener {

    private final LoadBalanceOperator loadBalanceOperator;

    public RocketServerChangeListener(LoadBalanceOperator loadBalanceOperator) {
        this.loadBalanceOperator = loadBalanceOperator;
    }


//    @RocketConsumerHandler
//    public void update(@Payload ServerChangeMessage changeMessage) {
//        loadBalanceOperator.update(changeMessage.getName());
//    }

}
