package com.weweibuy.framework.compensate.support;

import com.weweibuy.framework.compensate.utils.DateUtils;

import java.util.concurrent.TimeUnit;

/**
 * 规则解析器
 *
 * @author durenhao
 * @date 2020/2/15 18:49
 **/
public class RuleParser {

    private static final String UN_LIMIT = "...";

    public static long parser(Integer count, String rule) {
        String[] spiltRule = spiltRule(rule);
        if (spiltRule.length < count + 1) {
            if (UN_LIMIT.equals(spiltRule[spiltRule.length - 1])) {
                if (spiltRule.length > 1) {
                    String s = spiltRule[spiltRule.length - 2];
                    return parserTime(s);
                } else {
                    return 0;
                }
            }
            return -1;
        }
        String current = spiltRule[count];
        if (UN_LIMIT.equals(current) && spiltRule.length > 1) {
            return parserTime(spiltRule[spiltRule.length - 2]);
        }
        return parserTime(current);
    }


    private static String[] spiltRule(String rule) {
        return rule.split(" ");
    }

    private static long parserTime(String rule) {
        if (rule.length() < 2) {
            return 0;
        }
        String unit = rule.substring(rule.length() - 1);
        String num = rule.substring(0, rule.length() - 1);
        if (!isNumeric(num)) {
            return 0;
        }
        switch (unit) {
            case "s":
                return DateUtils.toMils(Long.valueOf(num), TimeUnit.SECONDS);
            case "H":
                return DateUtils.toMils(Long.valueOf(num), TimeUnit.HOURS);
            case "m":
                return DateUtils.toMils(Long.valueOf(num), TimeUnit.MINUTES);
            case "d":
                return DateUtils.toMils(Long.valueOf(num), TimeUnit.DAYS);
            case "M":
                return DateUtils.toMils(Long.valueOf(num), TimeUnit.DAYS) * 30;
            case "y":
                return DateUtils.toMils(Long.valueOf(num), TimeUnit.DAYS) * 365;
            default:
                return 0;
        }
    }


    private static boolean isNumeric(String str) {
        for (int i = str.length(); --i >= 0; ) {
            int chr = str.charAt(i);
            if (chr < 48 || chr > 57)
                return false;
        }
        return true;
    }

}
