package com.weweibuy.framework.common.util.csv;

import org.junit.Test;

import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

public class CsvUtilsTest {




    @Test
    public void export() throws Exception {
        ReflectCsvContentConverter<Student> converter = new ReflectCsvContentConverter<>(Student.class);
        Student student1 = new Student(1, "tom", "A");
        Student student2 = new Student(2, "jack", "b");
        Student student3 = new Student(3, "luck", "c");
        Student student4 = new Student(4, "fuck", "d");
        List<Student> students = Arrays.asList(student1, student2, student3, student4);
        FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\jeres\\Desktop\\新建文件夹\\test.csv");
        CsvUtils.export(null,students, converter, fileOutputStream, Charset.forName("GBK"));

    }

}