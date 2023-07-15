package com.weweibuy.framework.common.util.excel;

import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.weweibuy.framework.common.core.utils.ClassPathFileUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class ExcelUtilsTest {


    @Test
    public void readFromFile() throws Exception {
        String classPath = ClassPathFileUtils.getClassPath();
        String path = classPath + "/导出数据.xlsx";
        List<Student> students = ExcelUtils.readFromFile(new File(path),
                Student.class, null);
        System.err.println(students);
    }

    @Test
    public void writeFromFile() throws Exception {
        List<Student> students = new ArrayList<>();
        Student student = new Student();
        student.setAge(1);
        students.add(student);
        String classPath = ClassPathFileUtils.getClassPath();
        FileInputStream inputStream = new FileInputStream(classPath + "/导出数据-模板.xlsx");
        ExcelUtils.writeFromTemplate(classPath + "/导出数据.xlsx",
                inputStream, "数据", Student.class, students);
    }


}