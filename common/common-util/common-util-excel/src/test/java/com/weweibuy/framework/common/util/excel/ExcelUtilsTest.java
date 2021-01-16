package com.weweibuy.framework.common.util.excel;

import org.junit.Test;

import java.io.File;
import java.util.List;

public class ExcelUtilsTest {


    @Test
    public void readFromFile() throws Exception {
        List<Student> students = ExcelUtils.readFromFile(new File("C:/Users/z/Desktop/tmp/test.xlsx"),
                Student.class, null);
        System.err.println(students);
    }

}