package com.weweibuy.framework.common.util.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.metadata.holder.ReadRowHolder;
import com.alibaba.excel.read.metadata.holder.ReadSheetHolder;
import com.weweibuy.framework.common.core.exception.Exceptions;
import com.weweibuy.framework.common.core.model.eum.CommonErrorCodeEum;
import com.weweibuy.framework.common.util.excel.model.AbstractImportExcelVO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author durenhao
 * @date 2020/1/13 17:16
 **/
public class ValidatedExcelListener<T extends AbstractImportExcelVO> extends AnalysisEventListener<T> {

    private List<T> dataList;

    private Validator validator;

    /**
     * 检验失败立即中断
     */
    private boolean interrupted;

    public ValidatedExcelListener(Validator validator) {
        this(validator, false);
    }

    public ValidatedExcelListener(Validator validator, boolean interrupted) {
        this.validator = validator;
        this.dataList = new ArrayList<>();
        this.interrupted = interrupted;
    }

    public ValidatedExcelListener(Validator validator, Integer initSize) {
        this(validator, initSize, false);
    }

    public ValidatedExcelListener(Validator validator, Integer initSize, boolean interrupted) {
        this.validator = validator;
        this.dataList = new ArrayList<>(initSize);
        this.interrupted = interrupted;
    }

    @Override
    public void invoke(T o, AnalysisContext analysisContext) {
        ReadRowHolder readRowHolder = analysisContext.readRowHolder();
        ReadSheetHolder readSheetHolder = analysisContext.readSheetHolder();
        o.setRowNum(readRowHolder.getRowIndex());
        o.setSheetNum(readSheetHolder.getSheetNo());
        Set<ConstraintViolation<T>> violationSet = null;
        if (validator != null && CollectionUtils.isNotEmpty(violationSet = validator.validate(o))) {
            violationSet.stream()
                    .findFirst()
                    .ifPresent(c -> {
                        o.setErrorMsg(c.getMessage());
                        if (interrupted) {
                            throw Exceptions.business(CommonErrorCodeEum.BAD_REQUEST_PARAM.getCode(),
                                    String.format("Excel 第:%s sheet页, 第: %s 行, 数据错误: %s ", readSheetHolder.getSheetNo(),
                                            readRowHolder.getRowIndex(), c.getMessage()));
                        }
                    });
            if (StringUtils.isBlank(o.getErrorMsg()) && !o.validate() && interrupted) {
                throw Exceptions.business(CommonErrorCodeEum.BAD_REQUEST_PARAM.getCode(), o.getErrorMsg());
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
