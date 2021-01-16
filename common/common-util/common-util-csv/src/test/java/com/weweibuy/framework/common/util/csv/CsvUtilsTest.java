package com.weweibuy.framework.common.util.csv;

import com.weweibuy.framework.common.core.model.constant.CommonConstant;
import org.junit.Test;
import org.springframework.cglib.beans.BeanMap;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class CsvUtilsTest {


    @Test
    public void export() throws Exception {
        ReflectCsvContentConverter<Student> converter = new ReflectCsvContentConverter<>(Student.class);
        Student student1 = new Student(1, "tom", "A", LocalDateTime.now());
        Student student2 = new Student(2, "jack", null, LocalDateTime.now());
        List<Student> students = Arrays.asList(student1, student2);
        FileOutputStream fileOutputStream = new FileOutputStream("C:/Users/z/Desktop/tmp/test.csv");
        CsvUtils.export(null, students, converter, fileOutputStream, Charset.forName("GBK"));

    }

    @Test
    public void read() throws Exception {
        FileInputStream fileOutputStream =
                new FileInputStream("C:/Users/z/Desktop/tmp/test.csv");
        List<Student> read =
                CsvUtils.read(Student.class, fileOutputStream, CommonConstant.CharsetConstant.GBK, false);
        System.err.println(read);

    }





    @Test
    public void test() {
        Student student1 = new Student(1, "tom", "A", LocalDateTime.now());
        Student student2 = new Student(2, "jack", "b", LocalDateTime.now());
        BeanMap beanMap = BeanMap.create(student1);
        Object name = beanMap.get("name");
        System.err.println(name);
        beanMap.setBean(student2);
        Object name2 = beanMap.get("name");
        System.err.println(name2);
        BeanMap beanMap2 = BeanMap.create(student2);
        System.err.println(beanMap.equals(beanMap2));
    }

}