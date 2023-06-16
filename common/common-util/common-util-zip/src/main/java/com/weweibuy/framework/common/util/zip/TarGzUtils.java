package com.weweibuy.framework.common.util.zip;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author durenhao
 * @date 2023/3/19 21:02
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TarGzUtils {

    public static void tarGz(String source, String dest) throws IOException {
        tarGz(new File(source), dest, true);
    }

    public static void tarGz(String source, String dest, boolean retainDir) throws IOException {
        tarGz(new File(source), dest, retainDir);
    }

    public static void tarGz(File source, String dest) throws IOException {
        tarGz(source, dest, true);
    }

    public static void tarGz(File source, String dest, boolean retainDir) throws IOException {
        if (!source.isDirectory()) {
            tarGz(source, source.getName(), dest);
            return;
        }
        String path;
        if (retainDir) {
            path = FilenameUtils.getFullPath(source.getPath());
        } else {
            path = source.getPath() + File.separator;
        }

        try (FileOutputStream fileOutputStream = FileUtils.openOutputStream(new File(dest), false);
             GzipCompressorOutputStream gzip = new GzipCompressorOutputStream(fileOutputStream);
             TarArchiveOutputStream outputStream = new TarArchiveOutputStream(gzip)) {
            Collection<File> files = FileUtils.listFilesAndDirs(source, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
            Iterator<File> fileIterator = files.iterator();
            while (fileIterator.hasNext()) {
                File file = fileIterator.next();
                String subFilePath = file.getPath();
                if (path.length() > subFilePath.length()) {
                    continue;
                }
                add(file, ArchiveEntryUtils.relativePath(path, subFilePath), outputStream);
            }
        }
    }


    public static void tarGz(File source, String name, String dest) throws IOException {
        tarGz(source, name, new File(dest));
    }

    public static void tarGz(File source, String name, File dest) throws IOException {
        try (FileOutputStream fileOutputStream = FileUtils.openOutputStream(dest, false);
             GzipCompressorOutputStream gzip = new GzipCompressorOutputStream(fileOutputStream);
             TarArchiveOutputStream outputStream = new TarArchiveOutputStream(gzip)) {
            add(source, name, outputStream);
        }
    }

    private static void add(File source, String name, ArchiveOutputStream outputStream) throws IOException {
        if (source.isDirectory()) {
            name = name + "/";
        }
        TarArchiveEntry entry = new TarArchiveEntry(name);
        entry.setSize(source.length());
        ArchiveEntryUtils.addEntry(source, outputStream, entry);
    }

    public static List<File> unTarGz(String source, String dest) throws IOException {
        return unTarGz(new File(source), dest);
    }


    public static List<File> unTarGz(File source, String dest) throws IOException {
        List<File> fileList = new ArrayList<>();
        try (FileInputStream fin = new FileInputStream(source);
             GzipCompressorInputStream gin = new GzipCompressorInputStream(fin);
             TarArchiveInputStream tarArchiveInputStream = new TarArchiveInputStream(gin)) {
            TarArchiveEntry entry;
            while ((entry = tarArchiveInputStream.getNextTarEntry()) != null) {
                String name = dest + File.separator + entry.getName();
                File destFile = new File(name);
                fileList.add(destFile);
                if (entry.isDirectory()) {
                    FileUtils.forceMkdir(destFile);
                    continue;
                }
                try (FileOutputStream fileOutputStream = FileUtils.openOutputStream(destFile, false)) {
                    IOUtils.copy(tarArchiveInputStream, fileOutputStream);
                }
            }

        }
        return fileList;
    }


    public static void gz(String source, String dest) throws IOException {
        gz(new File(source), dest);
    }


    public static void gz(File source, String dest) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(source);
             FileOutputStream fileOutputStream = FileUtils.openOutputStream(new File(dest), false);
             GzipCompressorOutputStream gzip = new GzipCompressorOutputStream(fileOutputStream);) {
            IOUtils.copy(fileInputStream, gzip);
        }
    }


    public static void unGz(String source, String dest) throws IOException {
        unGz(new File(source), dest);
    }

    public static void unGz(File source, String dest) throws IOException {
        unGz(source, new File(dest));
    }

    public static void unGz(File source, File dest) throws IOException {
        try (FileInputStream fin = new FileInputStream(source);
             GzipCompressorInputStream gin = new GzipCompressorInputStream(fin);
             FileOutputStream fileOutputStream = FileUtils.openOutputStream(dest, false)) {
            IOUtils.copy(gin, fileOutputStream);
        }

    }

}
