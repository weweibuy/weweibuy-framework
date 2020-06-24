package com.weweibuy.framework.compensate.core;


import com.weweibuy.framework.compensate.model.CompensateInfo;
import com.weweibuy.framework.compensate.model.CompensateInfoExt;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author durenhao
 * @date 2020/2/14 22:53
 **/
public class SimpleCompensateStore implements CompensateStore {

    private AtomicInteger atomicInteger = new AtomicInteger();

    private Map<String, CompensateInfoExt> compensateInfoMap = new ConcurrentHashMap<>();

    private CompensateConfigStore compensateConfigStore;

    public SimpleCompensateStore(CompensateConfigStore compensateConfigStore) {
        this.compensateConfigStore = compensateConfigStore;
    }

    @Override
    public int saveCompensateInfo(CompensateInfo compensateInfo) {
        String id = atomicInteger.getAndDecrement() + "";
        compensateInfoMap.put(id, toCompensateInfoExt(id, compensateInfo));
        return 1;
    }

    @Override
    public Collection<CompensateInfoExt> queryCompensateInfo(Integer limit) {
        return compensateInfoMap.values().stream()
                .filter(c -> c.getNextTriggerTime().isBefore(LocalDateTime.now()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<CompensateInfoExt> queryCompensateInfoByIdForce(Set<String> idSet) {
        return compensateInfoMap.values().stream()
                .filter(c -> idSet.contains(c.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public int updateCompensateInfo(String id, CompensateInfoExt ext) {
        ext.setUpdateTime(LocalDateTime.now());
        compensateInfoMap.put(id, ext);
        return 1;
    }

    @Override
    public int deleteCompensateInfo(String id, Boolean success) {
        compensateInfoMap.remove(id);
        return 1;
    }


    private CompensateInfoExt toCompensateInfoExt(String id, CompensateInfo compensateInfo) {
        return new CompensateInfoExt(id, compensateInfo, compensateConfigStore.compensateConfig(compensateInfo.getCompensateKey()));
    }

}
