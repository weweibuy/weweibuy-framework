package com.weweibuy.framework.compensate.core;

import lombok.Data;

/**
 * @author durenhao
 * @date 2020/2/13 20:09
 **/
@Data
public class CompensateInfo {

    private String compensateKey;

    private String bizId;

    private Object[] args;


}
