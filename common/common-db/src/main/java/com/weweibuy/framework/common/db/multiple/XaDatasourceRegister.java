package com.weweibuy.framework.common.db.multiple;

import com.weweibuy.framework.common.core.utils.SpringResourcesUtils;
import com.weweibuy.framework.common.db.properties.DataSourceWithMybatisProperties;
import com.weweibuy.framework.common.db.properties.MultipleDataSourceProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.*;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import java.util.Map;

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
                .forEach(e -> registryXaDatasourceFactoryBean(e.getKey(), e.getValue(), registry));

    }

    /**
     * 注册 Datasource
     *
     * @param beanName
     * @param dataSourceProperties
     * @param registry
     */
    private void registryXaDatasourceFactoryBean(String beanName, DataSourceWithMybatisProperties dataSourceProperties, BeanDefinitionRegistry registry) {
        beanName = StringUtils.capitalize(beanName);
        BeanDefinitionBuilder definitionBuilder = BeanDefinitionBuilder
                .genericBeanDefinition(DatasourceFactoryBean.class)
                .addPropertyValue("dataSourceProperties", dataSourceProperties)
                .addPropertyValue("name", beanName)
                .addPropertyValue("xa", true);
        AbstractBeanDefinition definition = definitionBuilder.getBeanDefinition();
        beanName = StringUtils.capitalize(beanName);

        BeanDefinitionHolder holder = new BeanDefinitionHolder(definition, "xa" + beanName);
        BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
