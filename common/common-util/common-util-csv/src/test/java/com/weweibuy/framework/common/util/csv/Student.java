package com.weweibuy.framework.common.util.csv;

import com.weweibuy.framework.common.util.csv.annotation.CsvProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author : Knight
 * @date : 2021/1/14 11:55 上午
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Student {

    @CsvProperty( index = 2)
    private Integer age;

    @CsvProperty(name = "姓名", order = 10)
    private String name;

    @CsvProperty(index = 0)
    private String school;

    @CsvProperty(name = "生日a", order = 80, converter = LocalDateTimeCsvTypeConverter.class)
    private LocalDateTime birthday;

}
