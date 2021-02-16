package com.weweibuy.framework.common.core.utils;

import lombok.*;
import org.springframework.http.HttpMethod;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author : Knight
 * @date : 2021/1/14 11:55 上午
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
public class Student {

    private Integer age;

    private String name;

    private Date createTime;

    private LocalDateTime birthday;

    private HttpMethod httpMethod;


}
