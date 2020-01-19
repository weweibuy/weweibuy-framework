package com.weweibuy.framework.samples.message;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author durenhao
 * @date 2020/1/2 17:04
 **/
@Data
public class SampleUser {

    private String userName;

    private Integer age;

    private LocalDateTime birthday;



}
