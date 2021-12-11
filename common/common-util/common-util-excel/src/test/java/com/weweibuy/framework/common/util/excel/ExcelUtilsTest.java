package com.weweibuy.framework.common.util.excel;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class ExcelUtilsTest {


    @Test
    public void readFromFile() throws Exception {
        List<Student> students = ExcelUtils.readFromFile(new File("C:/Users/z/Desktop/tmp/test.xlsx"),
                Student.class, null);
        System.err.println(students);

    }

    @Test
    public void writeFromFile() throws Exception {
        List<Student> students = new ArrayList<>();
        Student student = new Student();
        student.setAge(1);
        students.add(student);
        FileInputStream inputStream = new FileInputStream("C:\\Users\\z\\Desktop\\模板.xlsx");
        ExcelUtils.writeFromTemplate("C:\\Users\\z\\Desktop\\tmp\\导出数据.xlsx",
                inputStream, "数据", Student.class, students);
    }


}