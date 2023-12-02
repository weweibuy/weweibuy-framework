package com.weweibuy.framework.common.db.multiple;

/**
 * @author durenhao
 * @date 2023/12/2 15:34
 **/
public class SpecDataSourceContext {

    private static final ThreadLocal<String> datasourceContext = new ThreadLocal<>();

    public static void setSpecDatasource(String datasourceName) {
        datasourceContext.set(datasourceName);
    }

    public static String getSpecDatasource() {
        return datasourceContext.get();
    }

    public static void clear() {
        datasourceContext.remove();
    }
}
