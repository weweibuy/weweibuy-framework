package com.weweibuy.framework.common.db.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author durenhao
 * @date 2023/2/26 11:09
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SqlUtils {

    private static final Pattern PATTERN = Pattern.compile(
            "\\b(and|exec|insert|select|drop|grant|alter|delete|update|count|chr|mid|master|truncate|char|declare|or)\\b|(\\*|;|\\+|'|%)");

    /**
     * 是否含有sql注入，返回true表示含有
     *
     * @param str
     * @return
     */
    public static boolean containsSqlInjection(String str) {
        Matcher matcher = PATTERN.matcher(str);
        return matcher.find();
    }

    /**
     * 是否含有sql注入
     *
     * @param str
     * @return
     */
    public static String containsSqlInjectionAndThrow(String str) {
        if (containsSqlInjection(str)) {
            throw new IllegalArgumentException("字符: " + str + " 可能存在SQL风险");
        }
        return str;
    }

}
