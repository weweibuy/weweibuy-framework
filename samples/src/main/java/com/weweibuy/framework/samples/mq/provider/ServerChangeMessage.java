package com.weweibuy.framework.samples.mq.provider;

import lombok.Data;

/**
 * @author durenhao
 * @date 2020/3/7 20:58
 **/
@Data
public class ServerChangeMessage {

    private String name;

    private String instanceId;

}
