package com.weweibuy.framework.biztask.config;

import com.weweibuy.framework.biztask.core.BizTaskHandlerMethodHolder;
import com.weweibuy.framework.biztask.db.mapper.BizTaskMapper;
import com.weweibuy.framework.biztask.db.repository.BizTaskRepository;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;

/**
 * @author durenhao
 * @date 2024/1/21 20:54
 **/
@AutoConfiguration
public class BizTaskBaseConfig {

    @Autowired
    private ObjectFactory<BizTaskMapper> bizTaskMapperObjectFactory;


    @Bean
    public BizTaskHandlerMethodHolder bizTaskHandlerMethodHolder() {
        return new BizTaskHandlerMethodHolder();
    }

    @Bean
    public BizTaskRepository bizTaskRepository() {
        return new BizTaskRepository(bizTaskMapperObjectFactory.getObject());
    }


    @AutoConfiguration
    @ConditionalOnBean(MybatisAutoConfiguration.class)
    @MapperScan(basePackageClasses = BizTaskMapper.class)
    public static class SingleDataSourceBizTaskConfig {


    }


}
