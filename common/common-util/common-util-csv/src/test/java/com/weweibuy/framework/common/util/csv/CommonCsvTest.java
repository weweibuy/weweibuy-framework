package com.weweibuy.framework.common.util.csv;

import com.weweibuy.framework.common.core.utils.ClassPathFileUtils;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class CommonCsvTest {


    @Test
    public void write() throws Exception {
        String classPath = ClassPathFileUtils.getClassPath();
        String file = classPath + "/test.csv";
        Student student1 = new Student("3班", "清华");
        student1.setName("tom");
        student1.setBirthday(LocalDate.now());

        Student student2 = new Student("4班", "北大");
        student2.setBirthday(LocalDate.now());

        List<Student> students = Arrays.asList(student1, student2);

        CommonCsvWriter.<Student>builder()
                .contentConverter(Student.class)
                .body(students)
                .build()
                .write(file);
    }

    @Test
    public void read() throws Exception {
        String classPath = ClassPathFileUtils.getClassPath();
        String file = classPath + "/test.csv";
        List<Student> read = CommonCsvReader.<Student>builder()
                .contentConverter(Student.class)
                .build()
                .read(file);
        System.err.println(read);
    }

}