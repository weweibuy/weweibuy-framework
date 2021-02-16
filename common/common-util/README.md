# common-util
  场景:  
  - Excel导入导出
  - CSV导入导出

  
### 1 Excel导入导出：

```
<dependency>
    <groupId>com.weweibuy.framework</groupId>
    <artifactId>common-util-excel</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```
  参考: 
  [EasyExcel](https://github.com/alibaba/easyexcel)  



### 2 CSV导入导出：
   1. 引入依赖:   
 ```
 <dependency>
     <groupId>com.weweibuy.framework</groupId>
     <artifactId>common-util-csv</artifactId>
     <version>1.0-SNAPSHOT</version>
 </dependency>
 ```
   2. CSV写出  
   2.1 定义Java对象  
```java
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

    @CsvProperty(name = "生日", converter = LocalDateCsvTypeConverter.class)
    private LocalDate birthday;

}
```
   2.2 写出Excel  
```java
public class CsvUtilsTest {


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
}
```
  2.3 Demo  
  [CsvUtilsTest](common-util-csv/src/test/java/com/weweibuy/framework/common/util/csv/CsvUtilsTest.java)  
  
   3. CSV读取   
   3.1 读取示例   
```java
public class CsvUtilsTest {


    @Test
    public void read() throws Exception {
        FileInputStream fileOutputStream =
                new FileInputStream("C:/Users/z/Desktop/tmp/test.csv");
        List<Student> read =
                CsvUtils.read(Student.class, fileOutputStream, CommonConstant.CharsetConstant.GBK, true);
        System.err.println(read);

    }
}
```
  3.2 Demo  
  [CsvUtilsTest](common-util-csv/src/test/java/com/weweibuy/framework/common/util/csv/CsvUtilsTest.java)  
  