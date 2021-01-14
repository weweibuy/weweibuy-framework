package com.weweibuy.framework.common.util.csv;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

/**
 * @author : Knight
 * @date : 2021/1/14 11:55 上午
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Student {
    private String name;

    public static List<Student> build() {
        return Collections.singletonList(new Student("11"));
    }
}
