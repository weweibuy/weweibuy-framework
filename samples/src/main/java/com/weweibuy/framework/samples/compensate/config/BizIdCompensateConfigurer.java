package com.weweibuy.framework.samples.compensate.config;

import com.weweibuy.framework.compensate.config.CompensateConfigurer;
import com.weweibuy.framework.compensate.support.BizIdCompensateTypeResolver;
import com.weweibuy.framework.compensate.support.CompensateTypeResolverComposite;
import com.weweibuy.framework.samples.compensate.service.BizIdCompensateAssembleImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * @author durenhao
 * @date 2020/2/17 22:23
 **/
@Configuration
public class BizIdCompensateConfigurer implements CompensateConfigurer {

    @Autowired
    private BizIdCompensateAssembleImpl assemble;

    @Override
    public void addCompensateTypeResolver(CompensateTypeResolverComposite composite) {
        composite.addResolver(new BizIdCompensateTypeResolver(assemble));
    }
}
