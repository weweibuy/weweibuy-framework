package com.weweibuy.framework.common.util.csv;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weweibuy.framework.common.core.model.constant.CommonConstant;
import lombok.Data;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class CsvUtilsTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void export() throws Exception {
        Student student1 = new Student("1班", "清华");
        student1.setName("tom");
        student1.setBirthday(LocalDate.now());


        Student student2 = new Student("2班", "北大");
        student2.setAge(13);
        student2.setName("jack");
        student2.setBirthday(LocalDate.now());

        List<Student> students = Arrays.asList(student1, student2);
        FileOutputStream fileOutputStream = new FileOutputStream("C:/Users/z/Desktop/tmp/test.csv");
        CsvUtils.export( students, Student.class, fileOutputStream, Charset.forName("GBK"));

    }

    @Test
    public void read() throws Exception {
        FileInputStream fileOutputStream =
                new FileInputStream("C:/Users/z/Desktop/tmp/test.csv");
        List<Student> read =
                CsvUtils.read(Student.class, fileOutputStream, CommonConstant.CharsetConstant.GBK, true);
        System.err.println(read);

    }

    @Test
    public void test1() throws Exception {
        User user = new User();
        int age = user.getAge();
        char aChar = '\u0000';
        System.err.println(aChar);
    }


    @Data
    static class User {

        private int age;

        private long id;

        private char aChar;

    }



}