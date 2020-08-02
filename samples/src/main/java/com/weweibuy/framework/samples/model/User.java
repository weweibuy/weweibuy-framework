package com.weweibuy.framework.samples.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author durenhao
 * @date 2020/2/15 14:43
 **/
@Data
public class User {

    private String name;

    private Integer age;

    private Dog dog;

    private BigDecimal price;

    private BigDecimal total;

}
