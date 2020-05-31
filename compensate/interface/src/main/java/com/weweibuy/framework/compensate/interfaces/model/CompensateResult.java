package com.weweibuy.framework.compensate.interfaces.model;

import lombok.Data;
import org.springframework.cglib.beans.BeanCopier;

/**
 * 补偿结果
 *
 * @author durenhao
 * @date 2020/5/31 20:27
 **/
@Data
public class CompensateResult extends CompensateInfoExt {

    private static final BeanCopier COPIER = BeanCopier.create(CompensateInfoExt.class, CompensateResult.class, false);


    /**
     * 补偿结果
     */
    private CompensateResultEum result;

    public static CompensateResult fromCompensateInfoExt(CompensateInfoExt compensateInfoExt, CompensateResultEum result) {
        CompensateResult compensateResult = new CompensateResult();
        COPIER.copy(compensateInfoExt, compensateResult, null);
        compensateResult.setResult(result);
        return compensateResult;
    }

}
