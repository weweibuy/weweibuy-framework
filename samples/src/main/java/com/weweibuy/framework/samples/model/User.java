package com.weweibuy.framework.samples.model;

import lombok.Data;

/**
 * @author durenhao
 * @date 2020/2/15 14:43
 **/
@Data
public class User {

    private String name;

    private Integer age;

    private Dog dog;
}
