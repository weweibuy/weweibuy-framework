package com.weweibuy.framework.common.db.properties;

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
public class MapperScanMybatisProperties extends MybatisProperties {

    /**
     * 对应的 sqlSessionFactory 是否是主要
     */
    private Boolean primary;

    /**
     * 数据源名称
     */
    private List<RefDatasource> datasource;

    /**
     * 包扫描
     */
    private List<String> mapperScanPackages;


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
