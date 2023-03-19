package com.weweibuy.framework.common.util.zip;

import com.weweibuy.framework.common.core.utils.ClassPathFileUtils;
import org.junit.Test;

public class TarGzUtilsTest {

    @Test
    public void tar() throws Exception {
        String classPath = ClassPathFileUtils.getClassPath();
        String dir = "E:\\Project\\17\\weweibuy-framework\\common\\common-util\\common-util-zip\\src\\test\\resources\\zipdir";
        String destdir = classPath + "/tardir.tar.gz";
        TarGzUtils.tarGz(dir, destdir, false);
    }
    @Test
    public void unTarGz() throws Exception {
        String classPath = ClassPathFileUtils.getClassPath();
        String tarFile = classPath + "/tardir.tar.gz";
        String dest = classPath + "/untar/";
        TarGzUtils.unTarGz(tarFile, dest);
    }



}