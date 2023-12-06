package com.weweibuy.framework.common.db.multiple;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.datasource.AbstractDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

/**
 *
 * @author durenhao
 * @date 2023/12/2 15:32
 **/
public class MultipleHolderDataSource extends AbstractDataSource {

    private Map<String, DataSource> dataSourceMap;

    private DataSource defaultDataSource;

    public MultipleHolderDataSource(Map<String, DataSource> dataSourceMap, DataSource defaultDataSource) {
        this.dataSourceMap = dataSourceMap;
        this.defaultDataSource = defaultDataSource;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return findSpecDataSourceOrThrow().getConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return findSpecDataSourceOrThrow().getConnection(username, password);
    }

    public Optional<DataSource> findDataSource(String datasourceName) {
        return Optional.ofNullable(dataSourceMap.get(datasourceName));
    }


    public DataSource findDataSourceOrDefault(String datasourceName) {
        return findDataSource(datasourceName)
                .orElse(defaultDataSource);
    }


    public DataSource findSpecDataSourceOrDefault() {
        return findDataSourceOrDefault(SpecDataSourceContext.getSpecDatasource());
    }

    public DataSource findSpecDataSourceOrThrow() {
        String specDatasource = SpecDataSourceContext.getSpecDatasource();
        if (StringUtils.isBlank(specDatasource)) {
            return defaultDataSource;
        }
        return findDataSource(specDatasource)
                .orElseThrow(() -> new IllegalArgumentException("指定数据源: " + specDatasource + " 不存在"));
    }

}
