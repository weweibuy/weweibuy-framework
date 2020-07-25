package com.weweibuy.framework.common.log.config;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author durenhao
 * @date 2020/7/25 23:37
 **/
@Builder
@EqualsAndHashCode
@Getter
@Setter
public class LogDisablePath {

    private String path;

    private Type type;


    public enum Type {

        REQ,

        RESP,

        ALL,
        ;

    }

}
