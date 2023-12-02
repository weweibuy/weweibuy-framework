package com.weweibuy.framework.common.db.multiple;

import com.weweibuy.framework.common.db.properties.MybatisAndDatasourceProperties;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeHandler;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.beans.PropertyDescriptor;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author durenhao
 * @date 2021/7/18 16:42
 **/
@Getter
@Setter
public class CustomSqlSessionFactoryBean implements FactoryBean<SqlSessionFactory>, ApplicationContextAware, ResourceLoaderAware {

    private MybatisAndDatasourceProperties properties;

    private ApplicationContext applicationContext;

    private ResourceLoader resourceLoader;

    private ObjectProvider<Interceptor[]> interceptorsProvider;

    private ObjectProvider<TypeHandler[]> typeHandlersProvider;

    private ObjectProvider<LanguageDriver[]> languageDriversProvider;

    private ObjectProvider<DatabaseIdProvider> databaseIdProvider;

    private ObjectProvider<List<ConfigurationCustomizer>> configurationCustomizersProvider;


    @Override
    public SqlSessionFactory getObject() throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        init(factoryBean);
        return factoryBean.getObject();
    }

    @Override
    public Class<?> getObjectType() {
        return SqlSessionFactory.class;
    }

    public void init(SqlSessionFactoryBean factoryBean) {
        factoryBean.setVfs(SpringBootVFS.class);
        if (StringUtils.hasText(this.properties.getConfigLocation())) {
            factoryBean.setConfigLocation(this.resourceLoader.getResource(this.properties.getConfigLocation()));
        }
        applyConfiguration(factoryBean);
        if (this.properties.getConfigurationProperties() != null) {
            factoryBean.setConfigurationProperties(this.properties.getConfigurationProperties());
        }
        if (!ObjectUtils.isEmpty(interceptorsProvider.getIfAvailable())) {
            factoryBean.setPlugins(interceptorsProvider.getIfAvailable());
        }
        if (this.databaseIdProvider != null) {
            factoryBean.setDatabaseIdProvider(this.databaseIdProvider.getIfAvailable());
        }
        if (StringUtils.hasLength(this.properties.getTypeAliasesPackage())) {
            factoryBean.setTypeAliasesPackage(this.properties.getTypeAliasesPackage());
        }
        if (this.properties.getTypeAliasesSuperType() != null) {
            factoryBean.setTypeAliasesSuperType(this.properties.getTypeAliasesSuperType());
        }
        if (StringUtils.hasLength(this.properties.getTypeHandlersPackage())) {
            factoryBean.setTypeHandlersPackage(this.properties.getTypeHandlersPackage());
        }
        if (!ObjectUtils.isEmpty(this.typeHandlersProvider.getIfAvailable())) {
            factoryBean.setTypeHandlers(this.typeHandlersProvider.getIfAvailable());
        }
        if (!ObjectUtils.isEmpty(this.properties.resolveMapperLocations())) {
            factoryBean.setMapperLocations(this.properties.resolveMapperLocations());
        }
        Set<String> factoryPropertyNames = Stream
                .of(new BeanWrapperImpl(SqlSessionFactoryBean.class).getPropertyDescriptors()).map(PropertyDescriptor::getName)
                .collect(Collectors.toSet());
        Class<? extends LanguageDriver> defaultLanguageDriver = this.properties.getDefaultScriptingLanguageDriver();
        if (factoryPropertyNames.contains("scriptingLanguageDrivers")
                && !ObjectUtils.isEmpty(languageDriversProvider.getIfAvailable())) {
            // Need to mybatis-spring 2.0.2+
            factoryBean.setScriptingLanguageDrivers(languageDriversProvider.getIfAvailable());
            if (defaultLanguageDriver == null && languageDriversProvider.getIfAvailable().length == 1) {
                defaultLanguageDriver = languageDriversProvider.getIfAvailable()[0].getClass();
            }
        }
        if (factoryPropertyNames.contains("defaultScriptingLanguageDriver")) {
            // Need to mybatis-spring 2.0.2+
            factoryBean.setDefaultScriptingLanguageDriver(defaultLanguageDriver);
        }
        DataSource dataSource = buildDatasource();
        factoryBean.setDataSource(dataSource);

        if (dataSource instanceof MultipleHolderDataSource) {
            //   指定数据源 + 数据源事务上下文 支持
            factoryBean.setTransactionFactory(new MultipleDatasourceTransactionFactory());
        }
    }

    private DataSource buildDatasource() {
        List<MybatisAndDatasourceProperties.RefDatasource> datasourceList = properties.getDatasource();
        if (CollectionUtils.isEmpty(datasourceList)) {
            throw new IllegalStateException("必须配置Mybatis的数据源");
        }

        Map<String, DataSource> dataSourceMap = applicationContext.getBeansOfType(DataSource.class);
        Map<String, DataSource> refDataSourceMap = datasourceList.stream()
                .collect(Collectors.toMap(MybatisAndDatasourceProperties.RefDatasource::getDatasourceName,
                        p -> Optional.ofNullable(dataSourceMap.get(p.getDatasourceName()))
                                .orElseThrow(() -> new IllegalStateException("数据源: " + p.getDatasourceName() + " 不存在"))));

        if (refDataSourceMap.size() == 1) {
            return refDataSourceMap.values().iterator().next();
        }
        List<DataSource> defaultDatasourceList = datasourceList.stream()
                .filter(p -> Boolean.TRUE.equals(p.getDefaultDatasource()))
                .map(p -> dataSourceMap.get(p.getDatasourceName()))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(defaultDatasourceList) || defaultDatasourceList.size() > 1) {
            throw new IllegalStateException("必须指定一个默认的数据源");
        }
        return new MultipleHolderDataSource(refDataSourceMap, defaultDatasourceList.get(0));
    }

    private void applyConfiguration(SqlSessionFactoryBean factory) {
        Configuration configuration = this.properties.getConfiguration();
        if (configuration == null && !StringUtils.hasText(this.properties.getConfigLocation())) {
            configuration = new Configuration();
        }

        List<ConfigurationCustomizer> configurationCustomizers = configurationCustomizersProvider.getIfAvailable();

        if (configuration != null && !CollectionUtils.isEmpty(configurationCustomizers)) {
            for (ConfigurationCustomizer customizer : configurationCustomizers) {
                customizer.customize(configuration);
            }
        }
        factory.setConfiguration(configuration);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
