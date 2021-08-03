package com.weweibuy.framework.common.util.excel.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * 通用Excel 导入对象
 *
 * @author durenhao
 * @date 2021/1/5 21:32
 **/
@Data
public abstract class AbstractImportExcelVO {

    @ExcelIgnore
    private Integer sheetNum;

    @ExcelIgnore
    private Integer rowNum;

    /**
     * 错误信息
     */
    @ExcelIgnore
    private String errorMsg;

    /**
     * 校验
     *
     * @return
     */
    protected String doValidate() {
        return null;
    }


    public boolean validate() {
        this.errorMsg = doValidate();
        return StringUtils.isNotBlank(errorMsg);
    }

}
