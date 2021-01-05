package com.weweibuy.framework.common.util.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.exception.ExcelAnalysisException;
import com.weweibuy.framework.common.core.exception.BusinessException;
import com.weweibuy.framework.common.util.excel.model.AbstractImportExcelVO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.validation.Validator;
import java.io.File;
import java.io.InputStream;
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
    public static <T extends AbstractImportExcelVO> List<T> readFromFile(File file, Integer sheetAt, Class<T> clazz, Validator validator, boolean interrupted) {
        ValidatedExcelListener validatedExcelListener = new ValidatedExcelListener<T>(validator, interrupted);
        try {
            EasyExcel.read(file, clazz, validatedExcelListener)
                    .sheet(sheetAt)
                    .autoTrim(true)
                    .doRead();
        } catch (ExcelAnalysisException e) {
            unWrapException(e);
        }
        return validatedExcelListener.getDataList();
    }

    public static <T extends AbstractImportExcelVO> List<T> readFromStream(InputStream inputStream, Integer sheetAt, Class<T> clazz, Validator validator, boolean interrupted) {
        ValidatedExcelListener validatedExcelListener = new ValidatedExcelListener<T>(validator, interrupted);
        try {
            EasyExcel.read(inputStream, clazz, validatedExcelListener)
                    .sheet(sheetAt)
                    .autoTrim(true)
                    .doRead();
        } catch (ExcelAnalysisException e) {
            unWrapException(e);
        }
        return validatedExcelListener.getDataList();
    }

    public static <T extends AbstractImportExcelVO> List<T> readFromFile(String path, Integer sheetAt, Class<T> clazz, Validator validator) {
        return readFromFile(new File(path), sheetAt, clazz, validator, false);
    }

    public static <T extends AbstractImportExcelVO> List<T> readFromFile(String path, Class<T> clazz, Validator validator) {
        return readFromFile(new File(path), 0, clazz, validator, false);
    }

    public static <T extends AbstractImportExcelVO> List<T> readFromFile(File file, Class<T> clazz, Validator validator) {
        return readFromFile(file, 0, clazz, validator, false);
    }

    public static <T extends AbstractImportExcelVO> List<T> readFromStream(InputStream inputStream, Integer sheetAt, Class<T> clazz, Validator validator) {
        return readFromStream(inputStream, sheetAt, clazz, validator, false);
    }

    public static <T extends AbstractImportExcelVO> List<T> readFromStream(InputStream inputStream, Class<T> clazz, Validator validator) {
        return readFromStream(inputStream, 0, clazz, validator, false);
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
    public static <T extends AbstractImportExcelVO> void writeFromFile(String fullName, String sheetName, Class<T> clazz, List<T> data) {
        EasyExcel.write(fullName, clazz).sheet(sheetName).doWrite(data);
    }

    private static void unWrapException(ExcelAnalysisException e) {
        Throwable cause = e.getCause();
        if (cause instanceof BusinessException) {
            throw (BusinessException) cause;
        }
        throw e;
    }

}
