package com.weweibuy.framework.common.db.config;

import com.github.pagehelper.PageInterceptor;
import com.github.pagehelper.autoconfigure.PageHelperProperties;
import com.weweibuy.framework.common.db.multiple.MultipleDatasourceAndMybatisRegister;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.util.List;

/**
 * 分页插件配置
 *
 * @author durenhao
 * @date 2021/7/18 21:14
 **/
@AutoConfiguration(after = MultipleDatasourceConfig.class)
@ConditionalOnBean({MultipleDatasourceAndMybatisRegister.class})
@EnableConfigurationProperties(PageHelperProperties.class)
public class MultipleDatasourcePageHelperConfig implements InitializingBean {

    private final List<SqlSessionFactory> sqlSessionFactoryList;

    private final PageHelperProperties properties;

    public MultipleDatasourcePageHelperConfig(List<SqlSessionFactory> sqlSessionFactoryList, PageHelperProperties properties) {
        this.sqlSessionFactoryList = sqlSessionFactoryList;
        this.properties = properties;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        PageInterceptor interceptor = new PageInterceptor();
        interceptor.setProperties(this.properties);
        for (SqlSessionFactory sqlSessionFactory : sqlSessionFactoryList) {
            org.apache.ibatis.session.Configuration configuration = sqlSessionFactory.getConfiguration();
            if (!containsInterceptor(configuration, interceptor)) {
                configuration.addInterceptor(interceptor);
            }
        }
    }

    /**
     * 是否已经存在相同的拦截器
     *
     * @param configuration
     * @param interceptor
     * @return
     */
    private boolean containsInterceptor(org.apache.ibatis.session.Configuration configuration, Interceptor interceptor) {
        try {
            // getInterceptors since 3.2.2
            return configuration.getInterceptors().contains(interceptor);
        } catch (Exception e) {
            return false;
        }
    }

}
