package com.weweibuy.framework.common.db.multiple;

import com.mysql.cj.jdbc.MysqlXADataSource;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;

import javax.sql.DataSource;
import java.sql.SQLException;

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

//        return atomikosDataSource(dataSourceProperties);
    }

    @Override
    public Class<?> getObjectType() {
        return DataSource.class;
    }

    protected static <T> T createDataSource(DataSourceProperties properties, Class<? extends DataSource> type) {
        return (T) properties.initializeDataSourceBuilder().type(type).build();
    }

    // todo 数据源 事务
    public DataSource atomikosDataSource(DataSourceProperties properties) throws SQLException {
        MysqlXADataSource mysqlXADataSource = new MysqlXADataSource();
        mysqlXADataSource.setURL(properties.getUrl());
        mysqlXADataSource.setPassword(properties.getPassword());
        mysqlXADataSource.setUser(properties.getUsername());
        mysqlXADataSource.setPinGlobalTxToPhysicalConnection(true);
        // 创建atomikos全局事务
        AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
        xaDataSource.setXaDataSource(mysqlXADataSource);
        xaDataSource.setUniqueResourceName(name);
        return xaDataSource;
    }
}
