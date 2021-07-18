package com.weweibuy.framework.common.db.properties;

import lombok.Data;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;

import java.util.List;

/**
 * @author durenhao
 * @date 2021/7/18 19:57
 **/
@Data
public class MapperScanMybatisProperties extends MybatisProperties {

    private List<String> mapperScanPackages;

}
