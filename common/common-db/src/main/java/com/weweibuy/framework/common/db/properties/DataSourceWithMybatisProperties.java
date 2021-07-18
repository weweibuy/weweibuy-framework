package com.weweibuy.framework.common.db.properties;

import lombok.Data;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

import java.util.List;

/**
 * @author durenhao
 * @date 2021/7/18 15:30
 **/
@Data
public class DataSourceWithMybatisProperties extends DataSourceProperties {

    private List<MapperScanMybatisProperties> mybatis;

}
