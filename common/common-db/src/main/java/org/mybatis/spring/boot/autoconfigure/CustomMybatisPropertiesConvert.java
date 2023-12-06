package org.mybatis.spring.boot.autoconfigure;

import org.apache.ibatis.session.Configuration;

/**
 * @author durenhao
 * @date 2023/12/6 15:15
 **/
public class CustomMybatisPropertiesConvert {


    public static void applyTo(MybatisProperties.CoreConfiguration coreConfiguration, Configuration configuration) {
        coreConfiguration.applyTo(configuration);
    }
}
