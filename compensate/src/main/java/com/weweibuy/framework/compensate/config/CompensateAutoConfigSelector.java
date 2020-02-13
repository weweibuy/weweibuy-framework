package com.weweibuy.framework.compensate.config;

import com.weweibuy.framework.compensate.annotation.EnableCompensate;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.AdviceModeImportSelector;

/**
 * @author durenhao
 * @date 2020/2/13 21:25
 **/
public class CompensateAutoConfigSelector extends AdviceModeImportSelector<EnableCompensate> {

    private static final String CONFIG_CLASS = "com.weweibuy.framework.compensate.config.CompensateAutoConfig";

    @Override
    protected String[] selectImports(AdviceMode adviceMode) {
        return new String[]{CONFIG_CLASS};
    }
}
