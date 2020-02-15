package com.weweibuy.framework.compensate.core;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author durenhao
 * @date 2020/2/14 22:53
 **/
public class SimpleCompensateStore implements CompensateStore {

    private AtomicInteger atomicInteger = new AtomicInteger();

    private Map<String, CompensateInfo> compensateInfoMap = new ConcurrentHashMap<>();

    @Override
    public int saveCompensateInfo(CompensateInfo compensateInfo) {
        int andDecrement = atomicInteger.getAndDecrement();
        compensateInfoMap.put(andDecrement + "", compensateInfo);
        return 1;
    }

    @Override
    public Collection<CompensateInfo> queryCompensateInfo() {
        return compensateInfoMap.values();
    }

    @Override
    public int updateCompensateInfo(String id, CompensateInfo compensateInfo) {
        compensateInfoMap.put(id, compensateInfo);
        return 1;
    }

    @Override
    public int deleteCompensateInfo(String id) {
        compensateInfoMap.remove(id);
        return 1;
    }
}
