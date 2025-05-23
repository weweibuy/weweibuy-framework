package com.weweibuy.framework.common.db.multiple;

import com.weweibuy.framework.common.core.utils.SpringResourcesUtils;
import com.weweibuy.framework.common.db.properties.DataSourceConfigProperties;
import com.weweibuy.framework.common.db.properties.MultipleDatasourceAndMybatisProperties;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

/**
 * @author durenhao
 * @date 2021/7/18 10:53
 **/
@Getter
@Setter
public class DatasourceFactoryBean implements FactoryBean<DataSource> {

    private DataSourceConfigProperties dataSourceProperties;

    private Integer num;

    private Environment environment;

    @Override
    public DataSource getObject() {
        dataSourceProperties.setType(HikariDataSource.class);
        return hikariDataSource();
    }


    @Override
    public Class<?> getObjectType() {
        return DataSource.class;
    }

    protected static <T> T createDataSource(DataSourceProperties properties, Class<? extends DataSource> type) {
        return (T) properties.initializeDataSourceBuilder().type(type)
                .build();
    }


    private DataSource hikariDataSource() {
        HikariDataSource dataSource = createDataSource(dataSourceProperties, HikariDataSource.class);
        HikariConfig hikari = dataSourceProperties.getHikari();
        if (hikari != null) {
            // 配置文件绑定
            SpringResourcesUtils.bindConfig(MultipleDatasourceAndMybatisProperties.PREFIX
                            + ".datasource[" + num + "].hikari",
                    dataSource, environment);
        }
        return dataSource;
    }


}
