package com.weweibuy.framework.common.db.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * 多数据源配置
 *
 * @author durenhao
 * @date 2021/7/18 9:55
 **/
@Data
@ConfigurationProperties(prefix = MultipleDatasourceAndMybatisProperties.PREFIX)
public class MultipleDatasourceAndMybatisProperties {

    public static final String PREFIX = "common.db.multiple";

    /**
     * 是否开启指定数据源功能
     */
    private Boolean enableSpecDatasource = true;

    /**
     * 开启指定数据源切面的执行顺序
     */
    private Integer specDatasourceAspectOrder = Integer.MAX_VALUE;

    /**
     * 数据源配置
     */
    private List<DataSourceConfigProperties> datasource;

    /**
     * mybatis 配置
     */
    private List<MybatisAndDatasourceProperties> mybatis;

}
