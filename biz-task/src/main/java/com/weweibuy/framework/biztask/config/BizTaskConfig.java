package com.weweibuy.framework.biztask.config;

import com.weweibuy.framework.biztask.core.AbstractBizTaskTrigger;
import com.weweibuy.framework.biztask.core.BizTaskBeanPostProcessor;
import com.weweibuy.framework.biztask.core.BizTaskExecutor;
import com.weweibuy.framework.biztask.core.BizTaskHandlerMethodHolder;
import com.weweibuy.framework.biztask.db.mapper.BizTaskMapper;
import com.weweibuy.framework.biztask.db.repository.BizTaskRepository;
import com.weweibuy.framework.biztask.support.BizTaskHelper;
import com.weweibuy.framework.biztask.support.SimpleBizTaskExecutor;
import com.weweibuy.framework.biztask.support.SimpleBizTaskTrigger;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * @author durenhao
 * @date 2024/1/19 21:13
 **/
@AutoConfiguration
public class BizTaskConfig {

    @Bean
    public BizTaskBeanPostProcessor bizTaskBeanPostProcessor() {
        return new BizTaskBeanPostProcessor(bizTaskHandlerMethodHolder());
    }


    @Bean
    public BizTaskHandlerMethodHolder bizTaskHandlerMethodHolder() {
        return new BizTaskHandlerMethodHolder();
    }

    @Bean
    public BizTaskRepository bizTaskRepository(BizTaskMapper bizTaskMapper) {
        return new BizTaskRepository(bizTaskMapper);
    }

    @Bean
    public BizTaskHelper bizTaskHelper(BizTaskMapper bizTaskMapper) {
        return new BizTaskHelper(bizTaskRepository(bizTaskMapper));
    }

    @Bean
    @ConditionalOnMissingBean(BizTaskExecutor.class)
    public SimpleBizTaskExecutor simpleBizTaskExecutor() {
        return new SimpleBizTaskExecutor(bizTaskHandlerMethodHolder());
    }

    @Bean
    @ConditionalOnMissingBean(AbstractBizTaskTrigger.class)
    public SimpleBizTaskTrigger simpleBizTaskTrigger(BizTaskMapper bizTaskMapper) {
        return new SimpleBizTaskTrigger(bizTaskRepository(bizTaskMapper),
                simpleBizTaskExecutor());
    }


}
