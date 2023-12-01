package com.weweibuy.framework.common.db.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

/**
 * 多数据源配置
 * 注意配置的是时候必须使用 中划线风格!  eg:  common.db.multiple-datasource.xxx
 *
 * @author durenhao
 * @date 2021/7/18 9:55
 **/
@Data
@ConfigurationProperties(prefix = MultipleDataSourceProperties.PREFIX)
public class MultipleDataSourceProperties {

    public static final String PREFIX = "common.db";

    private Map<String, DataSourceWithMybatisProperties> multipleDatasource;

    /**
     * mybatis 配置
     */
    private List<MapperScanMybatisProperties> mybatis;

}
