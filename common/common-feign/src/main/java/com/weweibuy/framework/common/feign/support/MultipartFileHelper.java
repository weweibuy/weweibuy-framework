package com.weweibuy.framework.common.feign.support;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

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
        return new CommonsMultipartFile(fileItem);
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
        int available = inputStream.available();
        FileItem fileItem = new DiskFileItem("file",
                MediaType.TEXT_PLAIN_VALUE,
                false,
                fileName,
                available,
                null);
        try (OutputStream outputStream = fileItem.getOutputStream()) {
            IOUtils.copy(inputStream, outputStream);
        }
        return new CommonsMultipartFile(fileItem);
    }

}
