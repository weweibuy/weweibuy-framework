package com.weweibuy.framework.common.codec.bcrypt;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author durenhao
 * @date 2021/1/31 18:36
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BCryptUtils {

    public static final BCryptPasswordEncoder ENCODER_INSTANCE =
            new BCryptPasswordEncoder();

    /**
     * 加密
     *
     * @param content
     * @return
     */
    public static String encode(String content) {
        return ENCODER_INSTANCE.encode(content);
    }

    /**
     * 匹配
     *
     * @param ori
     * @param encode
     * @return
     */
    public static boolean match(CharSequence ori, String encode) {
        return ENCODER_INSTANCE.matches(ori, encode);
    }
}
