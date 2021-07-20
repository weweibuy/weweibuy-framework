package com.weweibuy.framework.common.db.multiple;

import com.weweibuy.framework.common.core.utils.SpringResourcesUtils;
import com.weweibuy.framework.common.db.properties.DataSourceWithMybatisProperties;
import com.weweibuy.framework.common.db.properties.MapperScanMybatisProperties;
import com.weweibuy.framework.common.db.properties.MultipleDataSourceProperties;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.*;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import java.util.Map;
import java.util.Optional;

/**
 * XA 分布式事务数据源注册
 *
 * @author durenhao
 * @date 2021/7/20 21:44
 **/
public class XaDatasourceRegister implements BeanDefinitionRegistryPostProcessor, EnvironmentAware {

    private Environment environment;


    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        MultipleDataSourceProperties monitorRestBeanConfig = SpringResourcesUtils.bindConfig(MultipleDataSourceProperties.PREFIX,
                MultipleDataSourceProperties.class, environment);
        Map<String, DataSourceWithMybatisProperties> multipleDatasource = monitorRestBeanConfig.getMultipleDatasource();

        multipleDatasource.entrySet().stream()
                .filter(e -> e.getValue().getJoinXa())
                .peek(e -> registryXaDatasourceFactoryBean(e.getKey(), e.getValue(), registry))
                .forEach(e -> e.getValue().getMybatis().stream()
                        .peek(m -> registrySqlSessionFactoryBean(e.getKey(), m, registry))
                        .forEach(m -> registerMapperScan(e.getKey(), m, registry))
                );

    }

    /**
     * 注册 Datasource
     *
     * @param beanName
     * @param dataSourceProperties
     * @param registry
     */
    private void registryXaDatasourceFactoryBean(String beanName, DataSourceWithMybatisProperties dataSourceProperties, BeanDefinitionRegistry registry) {
        beanName = dataSourceName(beanName);
        BeanDefinitionBuilder definitionBuilder = BeanDefinitionBuilder
                .genericBeanDefinition(DatasourceFactoryBean.class)
                .addPropertyValue("dataSourceProperties", dataSourceProperties)
                .addPropertyValue("name", beanName)
                .addPropertyValue("xa", true);
        AbstractBeanDefinition definition = definitionBuilder.getBeanDefinition();

        BeanDefinitionHolder holder = new BeanDefinitionHolder(definition, beanName);
        BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);
    }


    /**
     * 注册 SqlSessionFactory
     *
     * @param datasourceName
     * @param mybatisProperties
     * @param registry
     */
    public void registrySqlSessionFactoryBean(String datasourceName, MapperScanMybatisProperties mybatisProperties, BeanDefinitionRegistry registry) {
        datasourceName = dataSourceName(datasourceName);
        BeanDefinitionBuilder definitionBuilder = BeanDefinitionBuilder
                .genericBeanDefinition(CustomSqlSessionFactoryBean.class)
                .addPropertyValue("datasourceName", datasourceName)
                .addPropertyValue("properties", mybatisProperties)
                .setPrimary(Optional.ofNullable(mybatisProperties.getPrimary()).orElse(false))
                .setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
        AbstractBeanDefinition definition = definitionBuilder.getBeanDefinition();
        BeanDefinitionHolder holder = new BeanDefinitionHolder(definition, sqlSessionFactoryBeanName(datasourceName));
        BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);
    }

    private String dataSourceName(String datasourceName) {
        return "xa" + StringUtils.capitalize(datasourceName);

    }

    private String sqlSessionFactoryBeanName(String datasourceName) {
        return datasourceName + "SqlSessionFactory";
    }

    /**
     * 注册 mapperScan
     *
     * @param datasourceName
     * @param properties
     * @param registry
     */
    public void registerMapperScan(String datasourceName, MapperScanMybatisProperties properties, BeanDefinitionRegistry registry) {
        datasourceName = dataSourceName(datasourceName);
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(MapperScannerConfigurer.class)
                .addPropertyValue("processPropertyPlaceHolders", true)
                .addPropertyValue("sqlSessionFactoryBeanName", sqlSessionFactoryBeanName(datasourceName))
                .addPropertyValue("basePackage", org.springframework.util.StringUtils.collectionToCommaDelimitedString(properties.getMapperScanPackages()));

        registry.registerBeanDefinition(datasourceName + "MapperScannerConfigurer", builder.getBeanDefinition());

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
