package com.weweibuy.framework.common.util.csv;

import com.weweibuy.framework.common.util.csv.annotation.CsvHead;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author durenhao
 * @date 2021/1/5 22:22
 **/
@AllArgsConstructor
public class ReflectCsvContentConverter implements CsvContentConverter<Student> {

    @SneakyThrows
    @Override
    public Collection<String[]> convert(String[] header, List<Student> body) {
        List<String[]> csvContent = new ArrayList<>(body.size() + 1);
        boolean headSet = true;
        String[] head = new String[header.length];
        for (Student student : body) {
            Field[] declaredFields = student.getClass().getDeclaredFields();
            String[] line = new String[header.length];
            for (Field declaredField : declaredFields) {
                String name = declaredField.getAnnotation(CsvHead.class).name();
                int index = declaredField.getAnnotation(CsvHead.class).index();
                if (head.length != declaredFields.length) {
                    head[index] = name;
                } else {
                    line[index] = declaredField.get(student).toString();
                }
            }
            if (headSet) {
                csvContent.add(head);
                headSet = false;
            }
            csvContent.add(line);
        }
        return csvContent;
    }
}
