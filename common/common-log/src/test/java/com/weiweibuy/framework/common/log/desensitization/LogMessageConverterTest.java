package com.weiweibuy.framework.common.log.desensitization;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogMessageConverterTest {


    @Test
    public void test01(){
        String msg = "请求路径: /hello, Method: POST,  数据: {\"address\":\"上海闵行虹桥\", \"fullName\":\"张三\", \"msg\":[\"tomq11214,1212ww\"],\"app\":[\"\",\"\"],\"qqq\":[\"121\"],\"phone\":\"13800000000\", " +
                "\"idNo\":\"612305871515151x\"}";
        String patten = "(mobile|phone|phoneNo|tel)(\":\")(\\d{3})\\d{4}(\\d{4})(\")";

        String msg2 = "请求路径: /hello, Method: POST,  数据: {\"msg\":[\"tomq11214,1212ww\"],\"app\":[\"\",\"\"],\"qqq\":[\"121\"],\"phone\":\"13800000000\"}";

        String pn = "(idNo|certId|certID)(\":\")(\\w{6})\\w*(\\w{1})(\")";
        String pw = "(password|pwd|appSecret)(\":\").*?(?=(\"))";
        String name = "(fullName)(\":\")([\\u4e00-\\u9fa5]{1})([\\u4e00-\\u9fa5]*)(\")";

        String address = "(address)(\":\")([\\u4e00-\\u9fa5]{4})([\\u4e00-\\u9fa5]*)(\")";


        Pattern pattern = Pattern.compile(patten);
        Pattern pattern2 = Pattern.compile(pn);
        Pattern pattern3 = Pattern.compile(pw);
        Pattern pattern4 = Pattern.compile(name);
        Pattern pattern5 = Pattern.compile(address);

        Matcher matcher = pattern.matcher(msg);
        Matcher matcher1 = pattern2.matcher(msg);
        Matcher matcher2 = pattern3.matcher(msg);
        Matcher matcher4 = pattern4.matcher(msg);
        Matcher matcher5 = pattern5.matcher(msg);


        boolean b = matcher.find();
        String xxx = matcher.replaceAll("$1$2$3****$4$5");
        String s = matcher1.replaceAll("$1$2$3**************$4$5");
        System.err.println(msg);
        System.err.println(s);
        System.err.println(xxx);
        String s1 = matcher2.replaceAll("$1$2$3******");
        String s2 = matcher4.replaceAll("$1$2$3**$5");
        String s3 = matcher5.replaceAll("$1$2$3********$5");
        System.err.println(s1);
        System.err.println(s2);
        System.err.println(s3);
    }


}