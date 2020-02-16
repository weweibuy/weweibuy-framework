package com.weweibuy.framework.samples.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

/**
 * @author durenhao
 * @date 2020/2/16 20:46
 **/
@Builder(access = AccessLevel.PUBLIC)
@Data
public class Test {

    private String name;

    private Integer age;

}
