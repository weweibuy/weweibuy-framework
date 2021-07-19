package com.weweibuy.framework.common.db.properties;

import lombok.Data;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

import java.util.List;

/**
 * 数据源与mybatis 配置
 * @author durenhao
 * @date 2021/7/18 15:30
 **/
@Data
public class DataSourceWithMybatisProperties extends DataSourceProperties {

    /**
     * 是否是主要
     */
    private Boolean primary;

    private List<MapperScanMybatisProperties> mybatis;

}
