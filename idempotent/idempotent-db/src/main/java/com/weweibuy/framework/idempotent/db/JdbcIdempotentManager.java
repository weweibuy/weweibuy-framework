package com.weweibuy.framework.idempotent.db;

import com.weweibuy.framework.common.core.utils.JackJsonUtils;
import com.weweibuy.framework.idempotent.core.exception.IdempotentException;
import com.weweibuy.framework.idempotent.core.support.IdempotentInfo;
import com.weweibuy.framework.idempotent.core.support.IdempotentManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * @author durenhao
 * @date 2020/6/20 0:03
 **/
@Slf4j
public class JdbcIdempotentManager implements IdempotentManager {

    private JdbcTemplate jdbcTemplate;

    private JdbcIdempotentProperties jdbcIdempotentProperties;

    public JdbcIdempotentManager(JdbcTemplate jdbcTemplate,
                                 JdbcIdempotentProperties jdbcIdempotentProperties) {
        Assert.notNull(jdbcTemplate, "jdbcTemplate 不能为空");
        Assert.notNull(jdbcIdempotentProperties, "jdbcIdempotentProperties 不能为空");
        checkProperties(jdbcIdempotentProperties);
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcIdempotentProperties = jdbcIdempotentProperties;
    }

    @Override
    public boolean tryLock(IdempotentInfo idempotentInfo) {

        if (!TransactionSynchronizationManager.isActualTransactionActive()) {
            throw new IdempotentException("基于JDBC的幂等必须在整个过程中有事务的保护");
        }

        String insertSql = jdbcIdempotentProperties.getInsertSql();
        Object[] prepareArgs = prepareArgs(idempotentInfo);
        try {
            jdbcTemplate.update(insertSql, prepareArgs);
            return true;
        } catch (DuplicateKeyException e) {
            log.info("有没获取到: 幂等key {}, 对应的锁", idempotentInfo.getKey());
            return false;
        }
    }

    @Override
    public Object handlerNoLock(IdempotentInfo idempotentInfo) {
        String selectSql = jdbcIdempotentProperties.getSelectSql();
        Object[] prepareArgs = prepareArgs(idempotentInfo);
        List<String> result = jdbcTemplate.query(selectSql, prepareArgs, new SingleColumnRowMapper(String.class));
        if (CollectionUtils.isEmpty(result)) {
            throw new IdempotentException("无法根据幂等Key: " + idempotentInfo.getKey() + "查询到幂等数据");
        }
        String resultStr = result.get(0);
        if ("null".equals(resultStr)) {
            return null;
        }
        return JackJsonUtils.readCamelCaseValue(resultStr, idempotentInfo.getJavaType());
    }

    @Override
    public void complete(IdempotentInfo idempotentInfo, Object result) {
        String updateSql = jdbcIdempotentProperties.getUpdateSql();
        Object[] prepareArgs = prepareArgs(idempotentInfo, result);
        jdbcTemplate.update(updateSql, prepareArgs);
    }


    private Object[] prepareArgs(IdempotentInfo idempotentInfo, Object result) {
        Object[] objects = prepareArgs(idempotentInfo);
        Object[] prepareArgs = new Object[objects.length + 1];
        System.arraycopy(objects, 0, prepareArgs, 1, objects.length);
        String updateSql = jdbcIdempotentProperties.getUpdateSql();
        String resultJson = JackJsonUtils.writeCamelCase(result);
        prepareArgs[0] = resultJson;
        return prepareArgs;
    }


    private Object[] prepareArgs(IdempotentInfo idempotentInfo) {
        List<Object> prepareArgsList = new ArrayList<>(2);
        prepareArgsList.add(idempotentInfo.getKey());
        if (jdbcIdempotentProperties.getUseSharding()) {
            validateShardingKeyNotBlank(idempotentInfo);
            prepareArgsList.add(idempotentInfo.getSharding());
        }
        return prepareArgsList.toArray();
    }

    private void validateShardingKeyNotBlank(IdempotentInfo idempotentInfo) {
        if (StringUtils.isBlank(idempotentInfo.getSharding())) {
            throw new IdempotentException("shardingKey不能为空");
        }
    }

    private void checkProperties(JdbcIdempotentProperties jdbcIdempotentProperties) {
        Assert.notNull(jdbcIdempotentProperties.getUpdateSql(), "idempotent.jdbc.updateSql  配置不能为空");
        Assert.notNull(jdbcIdempotentProperties.getSelectSql(), "idempotent.jdbc.selectSql 配置不能为空");
        Assert.notNull(jdbcIdempotentProperties.getInsertSql(), "idempotent.jdbc.insertSql 配置不能为空");
        Assert.notNull(jdbcIdempotentProperties.getUseSharding(), "idempotent.jdbc.useSharding 不能为空");
    }
}
