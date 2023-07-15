package com.weweibuy.framework.common.util.zip;

import com.weweibuy.framework.common.core.utils.ClassPathFileUtils;
import org.junit.Test;

import java.io.File;

public class ZipUtilsTest {


    @Test
    public void unzipFile() throws Exception {
        String classPath = ClassPathFileUtils.getClassPath();
        String zipFile = classPath + "/zipdir.zip";
        ZipUtils.unzip(zipFile, classPath + "/unzip");
    }


    @Test
    public void extractFile() throws Exception {
        String classPath = ClassPathFileUtils.getClassPath();
        String zipFile = classPath + "/zipdir.zip";
        String destFileName = classPath + "/extractFile/文件夹1";
        ZipUtils.extractFile(zipFile, "zipdir\\文件夹1/", destFileName);
    }

    @Test
    public void zipDir() throws Exception {
        String classPath = ClassPathFileUtils.getClassPath();
        String dir = "E:\\Project\\17\\weweibuy-framework\\common\\common-util\\common-util-zip\\src\\test\\resources\\zipdir";
        String destdir = classPath + "/zipdir.zip";
        File file = new File(dir);
        ZipUtils.zip(dir, destdir, true);
    }

    @Test
    public void extractFileToDir() throws Exception {
        String classPath = ClassPathFileUtils.getClassPath();
        String zipFile = classPath + "/zipdir.zip";
        String destdir = classPath + "/extractFile";
        ZipUtils.extractFileToDir(zipFile, "ES.txt", destdir);
    }



}