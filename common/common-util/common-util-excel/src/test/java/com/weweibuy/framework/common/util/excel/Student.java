package com.weweibuy.framework.common.util.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.weweibuy.framework.common.util.excel.model.AbstractImportExcelVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Knight
 * @date : 2021/1/14 11:55 上午
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Student extends AbstractImportExcelVO {

    @ExcelProperty(index = 0)
    private Integer age;


    @Override
    protected String doValidate() {
        return null;
    }
}
