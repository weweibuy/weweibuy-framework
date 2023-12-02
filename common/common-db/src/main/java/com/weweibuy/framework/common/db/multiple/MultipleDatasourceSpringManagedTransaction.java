package com.weweibuy.framework.common.db.multiple;

import org.apache.commons.lang3.StringUtils;
import org.mybatis.spring.transaction.SpringManagedTransaction;

import javax.sql.DataSource;

/**
 * @author durenhao
 * @date 2023/12/2 15:29
 **/
public class MultipleDatasourceSpringManagedTransaction extends SpringManagedTransaction {

    public MultipleDatasourceSpringManagedTransaction(DataSource dataSource) {
        super(findDataSource(dataSource));
    }

    private static DataSource findDataSource(DataSource dataSource) {
        if (dataSource instanceof MultipleHolderDataSource) {
            return ((MultipleHolderDataSource) dataSource).findSpecDataSourceOrThrow();
        }
        return dataSource;
    }

}
