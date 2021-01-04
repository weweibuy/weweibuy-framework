package com.weweibuy.framework.common.util.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.weweibuy.framework.common.core.exception.Exceptions;
import org.apache.commons.collections4.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author durenhao
 * @date 2020/1/13 17:16
 **/
public class ValidatedExcelListener<T> extends AnalysisEventListener<T> {

    private List<T> dataList;

    private Validator validator;

    public ValidatedExcelListener(Validator validator) {
        this.validator = validator;
        this.dataList = new ArrayList<>();
    }

    public ValidatedExcelListener(Validator validator, Integer initSize) {
        this.validator = validator;
        this.dataList = new ArrayList<>(initSize);
    }

    @Override
    public void invoke(T o, AnalysisContext analysisContext) {
        if (validator != null) {
            Set<ConstraintViolation<T>> violationSet = validator.validate(o);
            if (CollectionUtils.isNotEmpty(violationSet)) {
                violationSet.stream()
                        .findFirst()
                        .ifPresent(c -> {
                            throw Exceptions.business("-400", String.format("Excel 第:%s sheet页, 第: %s 行, 数据错误: %s ", analysisContext.readSheetHolder().getSheetNo(),
                                    analysisContext.readRowHolder().getRowIndex(), c.getMessage()));
                        });
            }
        }
        dataList.add(o);

    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }

    public List<T> getDataList() {
        return dataList;
    }
}
