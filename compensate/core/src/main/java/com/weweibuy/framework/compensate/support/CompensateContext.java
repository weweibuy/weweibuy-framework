package com.weweibuy.framework.compensate.support;

/**
 * 补偿上下文
 *
 * @author durenhao
 * @date 2020/6/25 17:28
 **/
public final class CompensateContext {

    private static final ThreadLocal<Boolean> BOOLEAN_THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 是否已经存在一个补偿
     *
     * @return
     */
    public static Boolean isExistCompensate() {
        Boolean aBoolean = BOOLEAN_THREAD_LOCAL.get();
        return aBoolean != null ? aBoolean : false;
    }


    public static void setCompensate() {
        BOOLEAN_THREAD_LOCAL.set(true);
    }

    public static Boolean getCompensate() {
        return BOOLEAN_THREAD_LOCAL.get();
    }

    public static void removeCompensate() {
        BOOLEAN_THREAD_LOCAL.remove();
    }

}
