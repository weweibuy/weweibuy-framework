package com.weweibuy.framework.common.util.zip;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author durenhao
 * @date 2023/3/19 21:36
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ArchiveEntryUtils {

    public static void addEntry(File source, ArchiveOutputStream outputStream, ArchiveEntry archiveEntry) throws IOException {
        if (!source.exists()) {
            throw new FileNotFoundException(source.getPath() + " 文件不存在");
        }
        outputStream.putArchiveEntry(archiveEntry);
        if (!source.isDirectory()) {
            try (FileInputStream fileInputStream = new FileInputStream(source)) {
                IOUtils.copy(fileInputStream, outputStream);
            }
        }
        outputStream.closeArchiveEntry();
    }


    static String relativePath(String path1, String path2) {
        return path2.substring(path1.length());
    }

}
