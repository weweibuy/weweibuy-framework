package com.weweibuy.framework.common.feign.support;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;

/**
 * MultipartFile 上传文件帮助类
 *
 * @author durenhao
 * @date 2021/4/17 20:45
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MultipartFileHelper {


    /**
     * 创建 MultipartFile
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static MultipartFile createMultipartFile(File file) throws IOException {
        FileItem fileItem = new DiskFileItem("file",
                Files.probeContentType(file.toPath()),
                false,
                file.getName(),
                (int) file.length(),
                null);
        try (FileInputStream fileInputStream = new FileInputStream(file);
             OutputStream outputStream = fileItem.getOutputStream()) {
            IOUtils.copy(fileInputStream, outputStream);
        }
        //TODO 3.0 删除了 CommonsMultipartFile
        return null;
    }

    /**
     * 通过流创建 MultipartFile
     *
     * @param inputStream
     * @param fileName
     * @return
     * @throws IOException
     */
    public static MultipartFile createMultipartFile(InputStream inputStream, String fileName) throws IOException {
        return createMultipartFile(inputStream, fileName, MediaType.TEXT_PLAIN_VALUE);
    }


    public static MultipartFile createMultipartFile(InputStream inputStream, String fileName, String contentType) throws IOException {
        int available = inputStream.available();
        FileItem fileItem = new DiskFileItem("file",
                contentType,
                false,
                fileName,
                available,
                null);
        try (OutputStream outputStream = fileItem.getOutputStream()) {
            IOUtils.copy(inputStream, outputStream);
        }
//        return new CommonsMultipartFile(fileItem);
        //TODO 3.0 删除了 CommonsMultipartFile
        return null;
    }

}
