package com.weweibuy.framework.compensate.core;

import java.util.List;

/**
 * @author durenhao
 * @date 2020/2/14 22:53
 **/
public class SimpleCompensateStore implements CompensateStore {

    @Override
    public int saveCompensateInfo(CompensateInfo compensateInfo) {
        return 0;
    }

    @Override
    public List<CompensateInfo> queryCompensateInfo() {
        return null;
    }

    @Override
    public int updateCompensateInfo(String id, CompensateInfo compensateInfo) {
        return 0;
    }
}
