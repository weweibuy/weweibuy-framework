package com.weweibuy.framework.common.db.multiple;

import com.weweibuy.framework.common.core.utils.SpringResourcesUtils;
import com.weweibuy.framework.common.db.properties.DataSourceConfigProperties;
import com.weweibuy.framework.common.db.properties.MybatisAndDatasourceProperties;
import com.weweibuy.framework.common.db.properties.MultipleDatasourceAndMybatisProperties;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.*;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

/**
 * 多数据源
 * datasource, sqlSessionFactory, mapperScan注册
 *
 * @author durenhao
 * @date 2021/7/18 10:32
 **/
@Slf4j
public class MultipleDatasourceAndMybatisRegister implements BeanDefinitionRegistryPostProcessor, EnvironmentAware {

    private Environment environment;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {

        // 配置文件绑定
        MultipleDatasourceAndMybatisProperties dataSourceAndMybatisProperties = SpringResourcesUtils.bindConfig(MultipleDatasourceAndMybatisProperties.PREFIX,
                MultipleDatasourceAndMybatisProperties.class, environment);
        if (log.isDebugEnabled()) {
            log.debug("绑定多数据源配置文件: {}", dataSourceAndMybatisProperties);
        }

        List<DataSourceConfigProperties> datasourceList = dataSourceAndMybatisProperties.getMultipleDatasource();
        registryDatasource(datasourceList, registry);

        List<MybatisAndDatasourceProperties> mybatis = dataSourceAndMybatisProperties.getMybatis();
        registryMybatis(mybatis, registry);
    }

    private void registryMybatis(List<MybatisAndDatasourceProperties> mybatis, BeanDefinitionRegistry registry) {
        for (int i = 0; i < mybatis.size(); i++) {
            MybatisAndDatasourceProperties properties = mybatis.get(i);
            registrySqlSessionFactoryBean(properties, registry, i);
            registerMapperScan(properties, registry, i);
        }
    }

    private void registryDatasource(List<DataSourceConfigProperties> datasourceList, BeanDefinitionRegistry registry) {
        datasourceList.stream()
                // 注册数据源
                .peek(e -> registryDatasourceFactoryBean(e.getDatasourceName(), e, registry))
                .filter(e -> Boolean.TRUE.equals(e.getCreateTransactionManager()))
                // 注册事务管理器
                .forEach(e -> registerTransactionManager(e.getDatasourceName(), e, registry));
    }


    /**
     * 注册 Datasource
     *
     * @param beanName
     * @param dataSourceProperties
     * @param registry
     */
    private void registryDatasourceFactoryBean(String beanName, DataSourceConfigProperties dataSourceProperties, BeanDefinitionRegistry registry) {
        BeanDefinitionBuilder definitionBuilder = BeanDefinitionBuilder
                .genericBeanDefinition(DatasourceFactoryBean.class)
                .addPropertyValue("dataSourceProperties", dataSourceProperties)
                .addPropertyValue("name", beanName)
                .addPropertyValue("environment", environment)
                .setPrimary(Optional.ofNullable(dataSourceProperties.getPrimary()).orElse(false));
        AbstractBeanDefinition definition = definitionBuilder.getBeanDefinition();
        BeanDefinitionHolder holder = new BeanDefinitionHolder(definition, beanName);
        BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);
    }


    /**
     * 注册 SqlSessionFactory
     *
     * @param mybatisProperties
     * @param registry
     * @param i
     */
    public void registrySqlSessionFactoryBean(MybatisAndDatasourceProperties mybatisProperties, BeanDefinitionRegistry registry, int i) {

        BeanDefinitionBuilder definitionBuilder = BeanDefinitionBuilder
                .genericBeanDefinition(CustomSqlSessionFactoryBean.class)
                .addPropertyValue("properties", mybatisProperties)
                .setPrimary(Optional.ofNullable(mybatisProperties.getPrimary()).orElse(false))
                .setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
        AbstractBeanDefinition definition = definitionBuilder.getBeanDefinition();
        BeanDefinitionHolder holder = new BeanDefinitionHolder(definition, sqlSessionFactoryBeanName(i));
        BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);
    }


    /**
     * 注册 mapperScan
     *
     * @param properties
     * @param registry
     * @param i
     */
    public void registerMapperScan(MybatisAndDatasourceProperties properties, BeanDefinitionRegistry registry, int num) {

        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(MapperScannerConfigurer.class)
                .addPropertyValue("processPropertyPlaceHolders", true)
                .addPropertyValue("sqlSessionFactoryBeanName", sqlSessionFactoryBeanName(num))
                .addPropertyValue("basePackage", StringUtils.collectionToCommaDelimitedString(properties.getMapperScanPackages()));

        registry.registerBeanDefinition("mapperScannerConfigurer" + num, builder.getBeanDefinition());

    }

    /**
     * 注册事务管理器
     *
     * @param datasourceName
     * @param properties
     * @param registry
     */
    public void registerTransactionManager(String datasourceName, DataSourceConfigProperties properties, BeanDefinitionRegistry registry) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(DataSourceTransactionManagerFactoryBean.class)
                .addPropertyValue("datasourceName", datasourceName)
                .setPrimary(Optional.ofNullable(properties.getPrimary()).orElse(false))
                .setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);

        String transactionManagerName = Optional.ofNullable(properties.getTransactionManagerName())
                .orElseGet(() -> datasourceName + "TransactionManager");
        registry.registerBeanDefinition(transactionManagerName, builder.getBeanDefinition());
    }


    private String sqlSessionFactoryBeanName(int num) {
        return "sqlSessionFactory" + num;
    }


    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }


    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
