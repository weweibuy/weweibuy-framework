/*
 * All rights Reserved, Designed By baowei
 *
 * 注意：本内容仅限于内部传阅，禁止外泄以及用于其他的商业目的
 */
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
