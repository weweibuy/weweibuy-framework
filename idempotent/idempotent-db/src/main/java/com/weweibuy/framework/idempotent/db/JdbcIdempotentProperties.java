package com.weweibuy.framework.idempotent.db;

import com.weweibuy.framework.idempotent.core.annotation.Idempotent;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author durenhao
 * @date 2020/6/20 9:38
 **/
@Data
@ConfigurationProperties(prefix = "idempotent.jdbc")
public class JdbcIdempotentProperties {

    /**
     * 将幂等信息(程序生成的幂等key, 或shardingKey(如果需要)). 插入幂等表
     * 幂等key 与  shardingKey(如果需要 {@link JdbcIdempotentProperties#useSharding}),将作为预编译参数.
     * 其中 幂等key为第一个与编译参数, shardingKey为第二个预编译参数
     * 示例:
     * INSERT INTO idempotent SET idem_key =  ?
     * INSERT INTO idempotent SET idem_key =  ? , sharding_key = ?
     */
    private String insertSql;

    /**
     * 更新方法执行结果到幂等表sql 预编译参数顺序:
     * 执行结果, 幂等key , shardingKey
     * 示例:
     * UPDATE idempotent SET exec_result = ? WHERE idem_key = ?
     * UPDATE idempotent SET exec_result = ? WHERE idem_key = ? and sharding_key = ?
     */
    private String updateSql;

    /**
     * 查询幂等结果 sql 预编译参数顺序:
     * 幂等key , shardingKey
     * 示例:
     * SELECT exec_result FROM idempotent WHERE idem_key = ?
     * SELECT exec_result FROM idempotent WHERE idem_key = ?  and sharding_key = ?
     */
    private String selectSql;

    /**
     * 是否使用分库分表, 使用分库分表是会将 {@link Idempotent#sharding()} 作为预编译参数
     */
    private Boolean useSharding = false;
}
