package com.weweibuy.framework.common.util.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.exception.ExcelAnalysisException;
import com.weweibuy.framework.common.core.exception.BusinessException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.validation.Validator;
import java.io.File;
import java.util.List;

/**
 * Excel 工具
 *
 * @author durenhao
 * @date 2020/1/13 17:20
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExcelUtils {


    /**
     * 读取Excel 文件
     *
     * @param file
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> readFromFile(File file, Integer sheetAt, Class<T> clazz, Validator validator) {
        ValidatedExcelListener validatedExcelListener = new ValidatedExcelListener<T>(validator);
        try {
            EasyExcel.read(file, clazz, validatedExcelListener).sheet(sheetAt).doRead();
        } catch (ExcelAnalysisException e) {
            Throwable cause = e.getCause();
            if (cause instanceof BusinessException) {
                throw (BusinessException) cause;
            }
            throw e;
        }
        return validatedExcelListener.getDataList();
    }

    public static <T> List<T> readFromFile(String path, Integer sheetAt, Class<T> clazz, Validator validator) {
        return readFromFile(new File(path), sheetAt, clazz, validator);
    }

    public static <T> List<T> readFromFile(String path, Class<T> clazz, Validator validator) {
        return readFromFile(new File(path), 0, clazz, validator);
    }

    public static <T> List<T> readFromFile(File file, Class<T> clazz, Validator validator) {
        return readFromFile(file, 0, clazz, validator);
    }

    /**
     * 写出Excel 文件
     *
     * @param fullName
     * @param sheetName
     * @param clazz
     * @param data
     * @param <T>
     */
    public static <T> void writeFromFile(String fullName, String sheetName, Class<T> clazz, List<T> data) {
        EasyExcel.write(fullName, clazz).sheet(sheetName).doWrite(data);
    }


}
