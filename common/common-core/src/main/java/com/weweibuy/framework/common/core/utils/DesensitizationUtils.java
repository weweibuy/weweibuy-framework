package com.weweibuy.framework.common.core.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * 脱敏工具
 *
 * @author durenhao
 * @date 2021/12/10 17:53
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DesensitizationUtils {

    private static final String PWD = "**********";

    private static final String X = "*";


    /**
     * 手机号脱敏
     *
     * @param phoneNo
     * @return
     */
    public static String phoneNo(String phoneNo) {
        if (StringUtils.isBlank(phoneNo) || phoneNo.length() < 7) {
            return phoneNo;
        }
        String str1 = phoneNo.substring(0, 3);
        String str2 = phoneNo.substring(7, phoneNo.length());
        return str1 + "****" + str2;
    }

    /**
     * 身份证号脱敏
     *
     * @param idNo
     * @return
     */
    public static String idNo(String idNo) {
        if (StringUtils.isBlank(idNo) || idNo.length() < 14) {
            return idNo;
        }
        String str1 = idNo.substring(0, 6);
        String str2 = idNo.substring(14, idNo.length());
        return str1 + "********" + str2;
    }

    /**
     * 姓名脱敏
     *
     * @param name
     * @return
     */
    public static String name(String name) {
        if (StringUtils.isBlank(name) || name.length() < 2) {
            return name;
        }
        String str1 = name.substring(0, 1);
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 1; i < name.length(); i++) {
            stringBuilder.append(X);
        }
        return str1 + stringBuilder.toString();
    }


    /**
     * 银行卡号脱敏
     *
     * @param bankCardNo
     * @return
     */
    public static String bankCardNo(String bankCardNo) {
        if (StringUtils.isBlank(bankCardNo) || bankCardNo.length() < 10) {
            return bankCardNo;
        }
        String str1 = bankCardNo.substring(0, 6);
        String str2 = bankCardNo.substring(bankCardNo.length() - 4, bankCardNo.length());

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 6; i < bankCardNo.length() - 4; i++) {
            stringBuilder.append(X);
        }
        return str1 + stringBuilder.toString() + str2;
    }

    public static String password(String password) {
        return "**********";
    }

}
