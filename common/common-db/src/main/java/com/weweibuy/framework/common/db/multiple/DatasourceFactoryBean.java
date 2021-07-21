package com.weweibuy.framework.common.db.multiple;

import com.weweibuy.framework.common.db.properties.DataSourceWithMybatisProperties;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

import javax.sql.DataSource;
import java.util.Optional;

/**
 * @author durenhao
 * @date 2021/7/18 10:53
 **/
@Getter
@Setter
public class DatasourceFactoryBean implements FactoryBean<DataSource> {

    private DataSourceWithMybatisProperties dataSourceProperties;

    private String name;

    @Override
    public DataSource getObject() throws Exception {
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


}
