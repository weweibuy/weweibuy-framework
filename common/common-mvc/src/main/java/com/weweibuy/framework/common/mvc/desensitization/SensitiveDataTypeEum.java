package com.weweibuy.framework.common.mvc.desensitization;

import lombok.Getter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 脱敏枚举
 *
 * @author durenhao
 * @date 2020/3/4 21:08
 **/
@Getter
public enum SensitiveDataTypeEum {

    FULL_NAME(Pattern.compile("(?<=[\\u4e00-\\u9fa5]{1})(.*)"), "*"),

    ID_CARD(Pattern.compile("(?<=\\w{6})\\w(?=\\w{1})"), "*"),

    ADDRESS(Pattern.compile("(?<=[\\u4e00-\\u9fa5]{6})(.*)"), "******"),

    BANK_CARD(Pattern.compile("(?<=\\w{6})\\w(?=\\w{4})"), "*"),

    MOBILE_PHONE(Pattern.compile("(?<=\\w{3})\\w(?=\\w{4})"), "*"),

    ;

    private Pattern patten;

    private String replace;

    SensitiveDataTypeEum(Pattern patten, String replace) {
        this.patten = patten;
        this.replace = replace;
    }

    public static void main(String[] args) {
        String str = "123QWErt:?!@#$";
        Pattern pattern = Pattern.compile(".*");
        Matcher matcher = pattern.matcher(str);

        String s = pattern.matcher(str)
                .replaceAll("*");
        System.err.println(s);
    }


}
