package com.weweibuy.framework.idempotent.core.config;

import com.weweibuy.framework.idempotent.core.annotation.EnableIdempotent;
import com.weweibuy.framework.idempotent.core.aspect.IdempotentAspect;
import com.weweibuy.framework.idempotent.core.aspect.IdempotentBeanFactoryPointcutAdvisor;
import com.weweibuy.framework.idempotent.core.aspect.IdempotentPointcut;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.context.annotation.Role;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author durenhao
 * @date 2020/3/31 15:01
 **/
@Configuration
public class IdempotentConfig implements ImportAware {

    protected AnnotationAttributes enableCompensate;

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        this.enableCompensate = AnnotationAttributes.fromMap(
                importMetadata.getAnnotationAttributes(EnableIdempotent.class.getName(), false));
        if (this.enableCompensate == null) {
            throw new IllegalArgumentException(
                    "@EnableIdempotent is not present on importing class " + importMetadata.getClassName());
        }
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public IdempotentBeanFactoryPointcutAdvisor compensateBeanFactoryPointcutAdvisor() {
        IdempotentBeanFactoryPointcutAdvisor advisor = new IdempotentBeanFactoryPointcutAdvisor();
        advisor.setPc(new IdempotentPointcut());
        advisor.setOrder(enableCompensate.<Integer>getNumber("order"));
        advisor.setAdvice(new IdempotentAspect());
        return advisor;
    }


}
