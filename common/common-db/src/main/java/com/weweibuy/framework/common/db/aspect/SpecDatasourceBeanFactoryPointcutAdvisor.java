package com.weweibuy.framework.common.db.aspect;

import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;
import org.springframework.aop.support.StaticMethodMatcherPointcut;

/**
 * 指定数据源切面配置
 *
 * @author durenhao
 * @date 2023/12/02 14:53
 **/
public class SpecDatasourceBeanFactoryPointcutAdvisor extends AbstractBeanFactoryPointcutAdvisor {

    private StaticMethodMatcherPointcut pc;

    public void setPc(StaticMethodMatcherPointcut pc) {
        this.pc = pc;
    }

    /**
     * @return
     */
    @Override
    public Pointcut getPointcut() {
        return pc;
    }


}
