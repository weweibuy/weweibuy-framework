package com.weweibuy.framework.biztask.config;

import com.weweibuy.framework.biztask.core.*;
import com.weweibuy.framework.biztask.db.repository.BizTaskRepository;
import com.weweibuy.framework.biztask.support.BizTaskHelper;
import com.weweibuy.framework.biztask.support.DefaultBizTaskExecConfigure;
import com.weweibuy.framework.biztask.support.SimpleBizTaskExecutor;
import com.weweibuy.framework.biztask.support.SimpleBizTaskTrigger;
import com.weweibuy.framework.common.core.support.AlarmService;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * @author durenhao
 * @date 2024/1/19 21:13
 **/
@AutoConfiguration(after = BizTaskBaseConfig.class)
public class BizTaskConfig {

    @Autowired
    private ObjectFactory<BizTaskRepository> bizTaskRepositoryObjectFactory;

    @Autowired
    private ObjectFactory<AlarmService> alarmServiceObjectFactory;

    @Autowired
    private ObjectFactory<BizTaskHandlerMethodHolder> bizTaskHandlerMethodHolderObjectFactory;

    @Bean
    public BizTaskBeanPostProcessor bizTaskBeanPostProcessor() {
        return new BizTaskBeanPostProcessor(bizTaskHandlerMethodHolderObjectFactory.getObject());
    }


    @Bean
    public BizTaskHelper bizTaskHelper() {
        return new BizTaskHelper(bizTaskRepositoryObjectFactory.getObject(),
                bizTaskExecConfigure(), alarmServiceObjectFactory.getObject());
    }

    @Bean
    @ConditionalOnMissingBean(BizTaskExecutor.class)
    public SimpleBizTaskExecutor simpleBizTaskExecutor() {
        return new SimpleBizTaskExecutor(bizTaskHandlerMethodHolderObjectFactory.getObject());
    }

    @Bean
    @ConditionalOnMissingBean(BizTaskExecConfigure.class)
    public DefaultBizTaskExecConfigure bizTaskExecConfigure() {
        return new DefaultBizTaskExecConfigure();
    }

    @Bean
    @ConditionalOnMissingBean(AbstractBizTaskTrigger.class)
    public SimpleBizTaskTrigger simpleBizTaskTrigger(SimpleBizTaskExecutor simpleBizTaskExecutor) {
        return new SimpleBizTaskTrigger(bizTaskRepositoryObjectFactory.getObject(),
                simpleBizTaskExecutor);
    }


}
