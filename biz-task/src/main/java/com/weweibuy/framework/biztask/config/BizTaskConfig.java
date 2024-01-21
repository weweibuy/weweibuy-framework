package com.weweibuy.framework.biztask.config;

import com.weweibuy.framework.biztask.core.*;
import com.weweibuy.framework.biztask.db.mapper.BizTaskMapper;
import com.weweibuy.framework.biztask.db.repository.BizTaskRepository;
import com.weweibuy.framework.biztask.support.BizTaskHelper;
import com.weweibuy.framework.biztask.support.DefaultBizTaskExecConfigure;
import com.weweibuy.framework.biztask.support.SimpleBizTaskExecutor;
import com.weweibuy.framework.biztask.support.SimpleBizTaskTrigger;
import com.weweibuy.framework.common.core.support.AlarmService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * @author durenhao
 * @date 2024/1/19 21:13
 **/
@AutoConfiguration
@MapperScan(basePackageClasses = BizTaskMapper.class)
public class BizTaskConfig {

    @Autowired
    private ObjectFactory<BizTaskMapper> bizTaskMapperObjectFactory;

    @Autowired
    private ObjectFactory<AlarmService> alarmServiceObjectFactory;

    @Bean
    public BizTaskHandlerMethodHolder bizTaskHandlerMethodHolder() {
        return new BizTaskHandlerMethodHolder();
    }

    @Bean
    public BizTaskBeanPostProcessor bizTaskBeanPostProcessor(BizTaskHandlerMethodHolder bizTaskHandlerMethodHolder) {
        return new BizTaskBeanPostProcessor(bizTaskHandlerMethodHolder);
    }


    @Bean
    public BizTaskRepository bizTaskRepository() {
        return new BizTaskRepository(bizTaskMapperObjectFactory.getObject());
    }

    @Bean
    public BizTaskHelper bizTaskHelper() {
        return new BizTaskHelper(bizTaskRepository(),
                bizTaskExecConfigure(), alarmServiceObjectFactory.getObject());
    }

    @Bean
    @ConditionalOnMissingBean(BizTaskExecutor.class)
    public SimpleBizTaskExecutor simpleBizTaskExecutor(BizTaskHandlerMethodHolder bizTaskHandlerMethodHolder) {
        return new SimpleBizTaskExecutor(bizTaskHandlerMethodHolder);
    }

    @Bean
    @ConditionalOnMissingBean(BizTaskExecConfigure.class)
    public DefaultBizTaskExecConfigure bizTaskExecConfigure() {
        return new DefaultBizTaskExecConfigure();
    }

    @Bean
    @ConditionalOnMissingBean(AbstractBizTaskTrigger.class)
    public SimpleBizTaskTrigger simpleBizTaskTrigger(BizTaskHandlerMethodHolder bizTaskHandlerMethodHolder) {
        return new SimpleBizTaskTrigger(bizTaskRepository(),
                simpleBizTaskExecutor(bizTaskHandlerMethodHolder));
    }


}
