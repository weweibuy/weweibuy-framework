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
     * 是否是主要
     */
    private Boolean primary;

    /**
     * 包扫描
     */
    private List<String> mapperScanPackages;


}
