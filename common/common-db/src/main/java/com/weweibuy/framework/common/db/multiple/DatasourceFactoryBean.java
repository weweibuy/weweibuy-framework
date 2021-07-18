package com.weweibuy.framework.common.db.multiple;

import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

import javax.sql.DataSource;

/**
 * @author durenhao
 * @date 2021/7/18 10:53
 **/
@Getter
@Setter
public class DatasourceFactoryBean implements FactoryBean<DataSource>  {

    private DataSourceProperties dataSourceProperties;

    private String name;

    @Override
    public DataSource getObject() throws Exception {
        HikariDataSource dataSource = (HikariDataSource)createDataSource(dataSourceProperties, HikariDataSource.class);
        dataSource.setPoolName(name + "-HikariPool");
        return dataSource;
    }

    @Override
    public Class<?> getObjectType() {
        return DataSource.class;
    }

    protected static <T> T createDataSource(DataSourceProperties properties, Class<? extends DataSource> type) {
        return (T) properties.initializeDataSourceBuilder().type(type).build();
    }

}
