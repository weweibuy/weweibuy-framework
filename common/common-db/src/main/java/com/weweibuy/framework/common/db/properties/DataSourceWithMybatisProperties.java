package com.weweibuy.framework.common.db.properties;

import com.zaxxer.hikari.HikariConfig;
import lombok.Data;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;

import java.util.List;

/**
 * 数据源与mybatis 配置
 *
 * @author durenhao
 * @date 2021/7/18 15:30
 **/
@Data
public class DataSourceWithMybatisProperties extends DataSourceProperties {

    /**
     * 是否是主要
     */
    private Boolean primary = false;

    /**
     * 是否创建事务管理器
     */
    private Boolean createTransactionManager = true;

    /**
     * transactionManagerName 默认  数据源名称 + TransactionManager
     */
    private String transactionManagerName;

    /**
     * 是否加入分布式事务控制器, 如果加入帮助创建 XaDatasource
     */
    private Boolean joinXa = false;

    /**
     * 入分布式事务控制器,创建数据源配置
     */
    private AtomikosDataSourceBean atomikos;

    /**
     * hikari 数据连接配置
     */
    private HikariConfig hikari;

    private List<MapperScanMybatisProperties> mybatis;

}
