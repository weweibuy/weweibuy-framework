package com.weweibuy.framework.common.feign.support;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.IOUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
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

}
