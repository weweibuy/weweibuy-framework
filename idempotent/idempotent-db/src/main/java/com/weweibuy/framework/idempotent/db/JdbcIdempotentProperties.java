package com.weweibuy.framework.idempotent.db;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author durenhao
 * @date 2020/6/20 9:38
 **/
@Data
@ConfigurationProperties(prefix = "idempotent.jdbc")
public class JdbcIdempotentProperties {

    private String insertSql;

    private String updateSql;

    private String selectSql;

    private Boolean useSharding = false;
}
