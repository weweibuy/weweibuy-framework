package com.weweibuy.framework.compensate.interceptor;

import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;
import org.springframework.aop.support.StaticMethodMatcherPointcut;

/**
 * @author durenhao
 * @date 2020/2/13 21:35
 **/
public class CompensateBeanFactoryPointcutAdvisor extends AbstractBeanFactoryPointcutAdvisor {

    private StaticMethodMatcherPointcut pc;

    public void setPc(StaticMethodMatcherPointcut pc) {
        this.pc = pc;
    }

    @Override
    public Pointcut getPointcut() {
        return pc;
    }


}
