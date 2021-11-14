package com.weweibuy.framework.common.core.utils;

import com.weweibuy.framework.common.core.exception.Exceptions;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 类路径下文件工具
 *
 * @author durenhao
 * @date 2021/11/4 22:27
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ClassPathFileUtils {

    private static final String CLASSPATH_STR = "classpath:";


    /**
     * 获取类路径
     *
     * @param path
     * @return
     */
    public static InputStream getClassPathFileStream(String path) throws IOException {
        if (!isClassPath(path)) {
            return null;
        }
        path = path.substring(CLASSPATH_STR.length(), path.length());
        ClassPathResource classPathResource = new ClassPathResource(path);
        return classPathResource.getInputStream();
    }

    /**
     * 获取类路径
     *
     * @return
     */
    public static String getClassPath() {
        File classPathFile = new File(ClassPathFileUtils.class.getResource("/").getPath());
        try {
            return classPathFile.getCanonicalPath();
        } catch (IOException e) {
            throw Exceptions.uncheckedIO(e);
        }
    }


    public static boolean isClassPath(String path) {
        return StringUtils.isNotBlank(path) && path.startsWith(CLASSPATH_STR);
    }

}
