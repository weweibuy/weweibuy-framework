package com.weweibuy.framework.common.core.utils;

import com.weweibuy.framework.common.core.exception.Exceptions;
import com.weweibuy.framework.common.core.model.constant.CommonConstant;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

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
    public static InputStream classPathFileStream(String path) throws IOException {
        if (path.length() < CLASSPATH_STR.length()) {
            throw new IllegalArgumentException(path + " 不是类路径");
        }
        path = path.substring(CLASSPATH_STR.length(), path.length());
        ClassPathResource classPathResource = new ClassPathResource(path);
        return classPathResource.getInputStream();
    }

    /**
     * 类路径或者非类路径文件流
     *
     * @param path
     * @return
     * @throws IOException
     */
    public static InputStream classPathFileOrOther(String path) throws IOException {
        if (isClassPath(path)) {
            return classPathFileStream(path);
        }
        return new FileInputStream(path);
    }

    /**
     * 类路径或者非类路径文件内容
     *
     * @param path
     * @return
     * @throws IOException
     */
    public static String classPathFileContentOrOther(String path, Charset charset) {
        try (InputStream inputStream = classPathFileOrOther(path)) {
            return IOUtils.toString(inputStream, charset);
        } catch (IOException e) {
            throw Exceptions.uncheckedIO(e);
        }
    }


    public static String classPathFileContentOrOther(String path) {
        return classPathFileContentOrOther(path, CommonConstant.CharsetConstant.UT8);
    }

    public static String classPathFileContent(String path, Charset charset) {
        try (InputStream inputStream = classPathFileStream(path)) {
            return IOUtils.toString(inputStream, charset);
        } catch (IOException e) {
            throw Exceptions.uncheckedIO(e);
        }
    }

    public static String classPathFileContent(String path) {
        return classPathFileContent(path, CommonConstant.CharsetConstant.UT8);
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
