package com.weweibuy.framework.common.db.multiple;

import org.apache.ibatis.session.TransactionIsolationLevel;
import org.apache.ibatis.transaction.Transaction;
import org.mybatis.spring.transaction.SpringManagedTransaction;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;

import javax.sql.DataSource;

/**
 * @author durenhao
 * @date 2023/12/2 15:26
 **/
public class MultipleDatasourceTransactionFactory extends SpringManagedTransactionFactory {

    @Override
    public Transaction newTransaction(DataSource dataSource, TransactionIsolationLevel level, boolean autoCommit) {
        return new MultipleDatasourceSpringManagedTransaction(dataSource);
    }

}
