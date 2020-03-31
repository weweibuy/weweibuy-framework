package com.weweibuy.framework.idempotent.core.aspect;

import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;
import org.springframework.aop.support.StaticMethodMatcherPointcut;

/**
 * 幂等切面配置
 *
 * @author durenhao
 * @date 2020/3/31 14:52
 **/
public class IdempotentBeanFactoryPointcutAdvisor extends AbstractBeanFactoryPointcutAdvisor {

    private StaticMethodMatcherPointcut pc;

    public void setPc(StaticMethodMatcherPointcut pc) {
        this.pc = pc;
    }

    @Override
    public Pointcut getPointcut() {
        return pc;
    }


}
