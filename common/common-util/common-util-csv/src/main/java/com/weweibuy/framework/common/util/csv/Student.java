package com.weweibuy.framework.common.util.csv;

import com.weweibuy.framework.common.util.csv.annotation.CsvHead;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Knight
 * @date : 2021/1/14 11:55 上午
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Student {

    @CsvHead(name = "年龄")
    private Integer age;

    @CsvHead(name = "姓名", index = 10)
    private String name;

    @CsvHead(name = "学校", index = 1)
    private String school;

}
