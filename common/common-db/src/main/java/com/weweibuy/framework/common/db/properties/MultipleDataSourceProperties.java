package com.weweibuy.framework.common.db.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * 多数据源配置
 *
 * @author durenhao
 * @date 2021/7/18 9:55
 **/
@Data
@ConfigurationProperties(prefix = "common.db")
public class MultipleDataSourceProperties {

    private Map<String, DataSourceWithMybatisProperties> multipleDatasource;


}