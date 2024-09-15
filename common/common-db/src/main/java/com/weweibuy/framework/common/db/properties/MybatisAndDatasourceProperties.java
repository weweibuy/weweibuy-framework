package com.weweibuy.framework.common.db.properties;

import com.weweibuy.framework.common.db.annotation.SpecDatasource;
import lombok.Data;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;

import java.util.List;


/**
 * mybatis 配置, 增加了包扫描配置项
 *
 * @author durenhao
 * @date 2021/7/18 19:57
 **/
@Data
public class MybatisAndDatasourceProperties extends MybatisProperties {

    /**
     * 对应的 sqlSessionFactory 是否是主要
     */
    private Boolean primary;

    /**
     * 数据源名称, 当使用多个数据源时支持通过 {@link SpecDatasource} 指定使用的数据源
     * 指定的数据源在spring事务上下中,受使用相同数据源的事务管理器影响
     */
    private List<RefDatasource> datasource;

    /**
     * 包扫描
     */
    private List<String> mapperScanPackages;

    /**
     * sqlSessionFactory bean名称
     * 默认 sqlSessionFactory + 配置数组序号
     */
    private String sqlSessionFactoryName;


    @Data
    public static class RefDatasource {

        /**
         * 数据源名称
         */
        private String datasourceName;

        /**
         * 当有多个数据源时,是否是默认的数据源
         */
        private Boolean defaultDatasource;

    }

}
