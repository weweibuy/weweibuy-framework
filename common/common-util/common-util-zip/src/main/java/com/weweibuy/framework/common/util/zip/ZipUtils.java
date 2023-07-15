package com.weweibuy.framework.common.util.zip;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * zip 压缩, 解压工具
 *
 * @author durenhao
 * @date 2023/3/19 10:47
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ZipUtils {

    /**
     * 解压到指定目录
     *
     * @param file    压缩文件
     * @param destDir
     * @return
     * @throws IOException
     */
    public static List<File> unzip(File file, String destDir) throws IOException {
        ZipFile zipFile = new ZipFile(file);
        return unzip(zipFile, destDir);
    }

    /**
     * 解压到指定目录
     *
     * @param fileFullName 压缩文件目录
     * @param destDir
     * @return
     * @throws IOException
     */
    public static List<File> unzip(String fileFullName, String destDir) throws IOException {
        ZipFile zipFile = new ZipFile(fileFullName);
        return unzip(zipFile, destDir);
    }

    /**
     * 解压到指定目录
     *
     * @param zipFile 压缩文件
     * @param destDir
     * @throws IOException
     */
    public static List<File> unzip(ZipFile zipFile, String destDir) throws IOException {
        Enumeration<ZipArchiveEntry> entries = zipFile.getEntries();
        List<File> fileList = new ArrayList<>();

        while (entries.hasMoreElements()) {
            ZipArchiveEntry zipArchiveEntry = entries.nextElement();
            File file = unzipFileToDestDir(zipFile, zipArchiveEntry, destDir);
            fileList.add(file);
        }
        return fileList;
    }

    /**
     * 解压文件到目录
     *
     * @param zipFile
     * @param entry
     * @param destDir
     * @return
     * @throws IOException
     */
    private static File unzipFileToDestDir(ZipFile zipFile, ZipArchiveEntry entry, String destDir) throws IOException {
        String fileName = destDir + File.separator + entry.getName();
        return unzipFile(zipFile, entry, fileName);
    }

    /**
     * 解压文件
     *
     * @param zipFile
     * @param entry
     * @param destFile
     * @return
     * @throws IOException
     */
    private static File unzipFile(ZipFile zipFile, ZipArchiveEntry entry, String destFile) throws IOException {
        File outputFile = new File(destFile);
        boolean directory = entry.isDirectory();
        if (directory) {
            FileUtils.forceMkdir(outputFile);
            return outputFile;
        }
        try (InputStream inputStream = zipFile.getInputStream(entry);
             FileOutputStream fos = FileUtils.openOutputStream(outputFile, false)) {
            IOUtils.copy(inputStream, fos);
            return outputFile;
        }
    }

    /**
     * 从压缩包中提取文件
     *
     * @param zipFile
     * @param extractFile
     * @param destFileName
     * @return
     * @throws IOException
     */
    public static File extractFile(File zipFile, String extractFile, String destFileName) throws IOException {
        return extractFile(new ZipFile(zipFile), extractFile, destFileName);
    }

    /**
     * 从压缩包中提取文件
     *
     * @param zipFilePath
     * @param extractFile
     * @param destFileName
     * @return
     * @throws IOException
     */
    public static File extractFile(String zipFilePath, String extractFile, String destFileName) throws IOException {
        return extractFile(new ZipFile(zipFilePath), extractFile, destFileName);
    }

    /**
     * 从压缩包中提取文件
     *
     * @param zipFile
     * @param extractFile
     * @param destFileName
     * @return
     * @throws IOException
     */
    public static File extractFile(ZipFile zipFile, String extractFile, String destFileName) throws IOException {
        ZipArchiveEntry zipArchiveEntry = zipFile.getEntry(extractFile);
        if (zipArchiveEntry == null) {
            return null;
        }
        return unzipFile(zipFile, zipArchiveEntry, destFileName);
    }


    /**
     * 从压缩包中提取文件到指定目录
     *
     * @param zipFile
     * @param extractFile
     * @param destDir
     * @return
     * @throws IOException
     */
    public static File extractFileToDir(File zipFile, String extractFile, String destDir) throws IOException {
        return extractFileToDir(new ZipFile(zipFile), extractFile, destDir);
    }

    /**
     * 从压缩包中提取文件
     *
     * @param zipFilePath
     * @param extractFile
     * @param destDir
     * @return
     * @throws IOException
     */
    public static File extractFileToDir(String zipFilePath, String extractFile, String destDir) throws IOException {
        return extractFileToDir(new ZipFile(zipFilePath), extractFile, destDir);
    }

    /**
     * 从压缩包中提取文件
     *
     * @param zipFile
     * @param extractFile
     * @param destDir
     * @return
     * @throws IOException
     */
    public static File extractFileToDir(ZipFile zipFile, String extractFile, String destDir) throws IOException {
        ZipArchiveEntry zipArchiveEntry = zipFile.getEntry(extractFile);
        if (zipArchiveEntry == null) {
            return null;
        }
        String fileName = destDir + File.separator + zipArchiveEntry.getName();
        return unzipFile(zipFile, zipArchiveEntry, fileName);
    }


    /**
     * 压缩文件夹
     *
     * @param sourceFile
     * @param destFile
     * @throws IOException
     */
    public static void zip(String sourceFile, String destFile) throws IOException {
        zip(sourceFile, destFile, true);
    }

    /**
     * 压缩文件夹
     *
     * @param sourceFile
     * @param destFile
     * @param retainDir  压缩文件夹时 压缩后的文件中是否保留文件夹
     * @throws IOException
     */
    public static void zip(String sourceFile, String destFile, boolean retainDir) throws IOException {
        File file = new File(sourceFile);
        zip(file, destFile, retainDir);
    }

    /**
     * 压缩文件夹
     *
     * @param source
     * @param destFile
     * @param retainDir 压缩文件夹时 压缩后的文件中是否保留文件夹
     * @throws IOException
     */
    public static void zip(File source, String destFile, boolean retainDir) throws IOException {
        if (!source.isDirectory()) {
            zipFile(source, destFile);
            return;
        }
        String path;
        if (retainDir) {
            path = FilenameUtils.getFullPath(source.getPath());
        } else {
            path = source.getPath() + File.separator;
        }

        try (FileOutputStream fileOutputStream = FileUtils.openOutputStream(new File(destFile), false);
             ZipArchiveOutputStream outputStream = new ZipArchiveOutputStream(fileOutputStream)) {
            Collection<File> files = FileUtils.listFilesAndDirs(source, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
            Iterator<File> fileIterator = files.iterator();
            while (fileIterator.hasNext()) {
                File file = fileIterator.next();
                String subFilePath = file.getPath();
                if (path.length() > subFilePath.length()) {
                    continue;
                }
                zipFile(file, ArchiveEntryUtils.relativePath(path, subFilePath), outputStream);
            }
        }
    }

    /**
     * 压缩文件
     *
     * @param file
     * @param destFile
     * @throws IOException
     */
    public static void zipFile(String file, String destFile) throws IOException {
        zipFile(new File(file), destFile);
    }

    public static void zipFile(File file, String destFile) throws IOException {
        zipFile(file, file.getName(), destFile);
    }

    /**
     * 压缩文件
     *
     * @param source
     * @param destFile
     * @throws IOException
     */
    public static void zipFile(File source, String name, String destFile) throws IOException {
        try (FileOutputStream fileOutputStream = FileUtils.openOutputStream(new File(destFile), false);
             ZipArchiveOutputStream outputStream = new ZipArchiveOutputStream(fileOutputStream)) {
            zipFile(source, name, outputStream);
        }
    }

    public static void zipFile(File source, String name, ArchiveOutputStream outputStream) throws IOException {
        ZipArchiveEntry entry = new ZipArchiveEntry(source, name);
        ArchiveEntryUtils.addEntry(source, outputStream, entry);
    }


}
