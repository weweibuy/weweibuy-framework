package com.weweibuy.framework.common.core.utils;

import org.junit.Test;
import org.springframework.http.HttpMethod;

import java.time.LocalDateTime;
import java.util.Date;

public class JackJsonUtilsTest {


    private static Student student;

    static {
        student = Student.builder()
                .name("tom")
                .age(12)
                .createTime(new Date())
                .birthday(LocalDateTime.now())
                .httpMethod(HttpMethod.GET)
                .build();
    }

    @Test
    public void readSnakeCaseValue() throws Exception {
        String json = JackJsonUtils.writeSnakeCase(student);
        System.err.println(json);
        Student student1 = JackJsonUtils.readSnakeCaseValue(json, Student.class);
        System.err.println(student1);
    }

}