package com.weweibuy.framework.compensate.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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

    private CompensateConfigStore compensateConfigStore;

    public SimpleCompensateStore(CompensateConfigStore compensateConfigStore) {
        this.compensateConfigStore = compensateConfigStore;
    }

    @Override
    public int saveCompensateInfo(CompensateInfo compensateInfo) {
        int andDecrement = atomicInteger.getAndDecrement();
        compensateInfoMap.put(andDecrement + "", compensateInfo);
        return 1;
    }

    @Override
    public Collection<CompensateInfoExt> queryCompensateInfo() {
        List<CompensateInfoExt> infoExtArrayList = new ArrayList<>();
        compensateInfoMap.forEach((k, v) -> {
            CompensateConfigProperties configProperties = compensateConfigStore.compensateConfig(v.getCompensateKey());
            infoExtArrayList.add(new CompensateInfoExt(k, v, configProperties));
        });
        return infoExtArrayList;
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
