package com.weweibuy.framework.common.db.multiple;

import com.mysql.cj.jdbc.MysqlXADataSource;
import com.weweibuy.framework.common.db.properties.DataSourceWithMybatisProperties;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Optional;

/**
 * @author durenhao
 * @date 2021/7/18 10:53
 **/
@Getter
@Setter
public class DatasourceFactoryBean implements FactoryBean<DataSource> {

    private DataSourceWithMybatisProperties dataSourceProperties;

    private Boolean xa;

    private String name;

    @Override
    public DataSource getObject() throws Exception {
        if (!xa) {
            return hikariDataSource();
        }
        return atomikosDataSource();
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
        HikariDataSource dataSource = (HikariDataSource) createDataSource(dataSourceProperties, HikariDataSource.class);
        HikariConfig hikari = dataSourceProperties.getHikari();
        if (hikari != null) {
            HikariDataSource hikariDataSource = new HikariDataSource();
            BeanUtils.copyProperties(hikari, hikariDataSource,
                    "driverClassName", "exceptionOverrideClassName", "minimumIdle");
            Optional.ofNullable(hikari.getMinimumIdle())
                    .filter(i -> i >= 0)
                    .ifPresent(hikariDataSource::setMinimumIdle);
        }
        return dataSource;
    }

    private DataSource atomikosDataSource() throws SQLException {
        MysqlXADataSource mysqlXADataSource = new MysqlXADataSource();
        mysqlXADataSource.setURL(dataSourceProperties.getUrl());
        mysqlXADataSource.setPassword(dataSourceProperties.getPassword());
        mysqlXADataSource.setUser(dataSourceProperties.getUsername());
        mysqlXADataSource.setPinGlobalTxToPhysicalConnection(true);
        mysqlXADataSource.setAutoGenerateTestcaseScript(false);

        // 创建atomikos全局事务
        AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
        AtomikosDataSourceBean atomikos = dataSourceProperties.getAtomikos();
        if (atomikos != null) {
            BeanUtils.copyProperties(atomikos, xaDataSource);
        }
        xaDataSource.setXaDataSource(mysqlXADataSource);
        xaDataSource.setUniqueResourceName(name);
        return xaDataSource;
    }

    public static void main(String[] args) {
        AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
        AtomikosDataSourceBean atomikos  = new AtomikosDataSourceBean();
        atomikos.setMaxIdleTime(111);
        if (atomikos != null) {
            BeanUtils.copyProperties(atomikos, xaDataSource);
        }
        int maxIdleTime = xaDataSource.getMaxIdleTime();
        System.err.println(maxIdleTime);
    }
}
