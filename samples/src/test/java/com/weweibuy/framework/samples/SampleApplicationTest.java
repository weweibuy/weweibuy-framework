package com.weweibuy.framework.samples;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SampleApplicationTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    private String[] arr = {"a", "b", "c"};

    private String json = "{\"objectsArr\":[\"a\",\"b\",\"c\"]}";

    @Test
    public void test01() throws JsonProcessingException {
        TestObject testObject = new TestObject();
        testObject.setObjectsArr(arr);
        System.err.println(objectMapper.writeValueAsString(testObject));

        TestObject readValue = objectMapper.readValue(json, TestObject.class);
        Object[] objectsArr = readValue.getObjectsArr();
    }



    @Data
    public static class TestObject {

        private Object[] objectsArr;

    }

}