package com.weweibuy.framework.common.util.csv;

import com.weweibuy.framework.common.util.csv.annotation.CsvProperty;
import lombok.Data;

import java.time.LocalDate;

/**
 * @author durenhao
 * @date 2021/1/17 11:33
 **/
@Data
public class Person {

    @CsvProperty(name = "年龄")
    private int age;

    @CsvProperty(name = "姓名")
    private String name;

    @CsvProperty(name = "生日")
    private LocalDate birthday;

}
