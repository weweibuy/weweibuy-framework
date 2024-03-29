package com.weweibuy.framework.common.codec;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PasswordGenerateUtils {

    /**
     * 生成随即密码
     *
     * @param pwdLen 生成的密码的总长度
     * @return 密码的字符串
     */
    public static String generate(int pwdLen) {
        //35是因为数组是从0开始的，26个字母+10个数字
        final int maxNum = 75;
        int i;  //生成的随机数
        int count = 0; //生成的密码的长度

        char[] str = {
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                '~', '!', '@', '#', '$', '%', '^', '&'
        };

        StringBuilder pwd = new StringBuilder();
        Random r = new Random();
        while (count < pwdLen) {
            // 生成随机数，取绝对值，防止生成负数，
            i = Math.abs(r.nextInt(maxNum));  //生成的数最大为36-1

            if (i >= 0 && i < str.length) {
                pwd.append(str[i]);
                count++;
            }
        }
        return pwd.toString();
    }

    /**
     * 生成随机密码生成方式二
     * 密码字典 -> 随机打乱集合顺序 -> 随机获取字符
     *
     * @param pwdLen 生成密码长度
     * @return
     */
    public static String generate1(int pwdLen) {
        int i;  //生成的随机数
        int count = 0; //生成的密码的长度

        // 密码字典
        char[] str = {
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                '~', '!', '@', '#', '$', '%', '^', '-', '+'
        };

        StringBuffer stringBuffer = new StringBuffer();
        List<String> list = new ArrayList<>();
        Random r = new Random();
        for (i = 0; i < str.length; i++) {
            list.add(str[i] + "");
        }
        Collections.shuffle(list);
        while (count < pwdLen) {
            //生成 0 ~ 密码字典-1之间的随机数
            i = r.nextInt(list.size());
            stringBuffer.append(list.get(i));
            count++;
        }
        return stringBuffer.toString();
    }


}