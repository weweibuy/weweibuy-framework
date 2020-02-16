package com.weweibuy.framework.samples.compensate;

import com.weweibuy.framework.compensate.core.*;
import com.weweibuy.framework.samples.compensate.mapper.CompensateMapper;
import com.weweibuy.framework.samples.compensate.model.po.Compensate;
import com.weweibuy.framework.samples.compensate.model.po.CompensateExample;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author durenhao
 * @date 2020/2/16 12:22
 **/
public class JdbcCompensateStore implements CompensateStore {

    @Autowired
    private CompensateMapper compensateMapper;

    @Autowired
    private CompensateConfigStore compensateConfigStore;

    @Override
    public int saveCompensateInfo(CompensateInfo compensateInfo) {
        return compensateMapper.insertSelective(toCompensate(compensateInfo));
    }

    @Override
    public Collection<CompensateInfoExt> queryCompensateInfo() {
        CompensateExample compensateExample = new CompensateExample();
        compensateExample.createCriteria().andIsDeleteEqualTo(false)
                .andNextTriggerTimeLessThan(LocalDateTime.now());
        List<Compensate> compensates = compensateMapper.selectByExampleWithBLOBs(compensateExample);
        return compensates.stream()
                .map(this::toCompensateInfoExt)
                .collect(Collectors.toList());
    }

    @Override
    public int updateCompensateInfo(String id, CompensateInfoExt compensateInfo) {
        Compensate compensate = toCompensate2(compensateInfo);
        compensate.setId(Long.valueOf(id));
        return compensateMapper.updateByPrimaryKeySelective(compensate);
    }

    @Override
    public int deleteCompensateInfo(String id) {
        Compensate compensate = new Compensate();
        compensate.setIsDelete(true);
        compensate.setId(Long.valueOf(id));
        return compensateMapper.updateByPrimaryKeySelective(compensate);
    }

    private CompensateInfoExt toCompensateInfoExt(Compensate compensate) {
        CompensateInfoExt infoExt = new CompensateInfoExt();
        infoExt.setId(compensate.getId() + "");
        infoExt.setAlarmCount(compensate.getAlarmCount());
        infoExt.setRetryCount(compensate.getRetryCount());
        infoExt.setBizId(compensate.getBizId());
        infoExt.setCompensateKey(compensate.getCompensateKey());
        infoExt.setUpdateTime(compensate.getUpdateTime());
        infoExt.setMethodArgs(compensate.getMethodArgs());
        CompensateConfigProperties properties = compensateConfigStore.compensateConfig(compensate.getCompensateKey());
        infoExt.setType(properties.getCompensateType());
        infoExt.setAlarmRule(properties.getAlarmRule());
        infoExt.setRetryRule(properties.getRetryRule());
        infoExt.setNextTriggerTime(compensate.getNextTriggerTime());
        return infoExt;
    }

    private Compensate toCompensate(CompensateInfo compensateInfo) {
        Compensate compensate = new Compensate();
        compensate.setCompensateKey(compensateInfo.getCompensateKey());
        compensate.setBizId(compensateInfo.getBizId());
        compensate.setMethodArgs(compensateInfo.getMethodArgs());
        compensate.setNextTriggerTime(compensateInfo.getNextTriggerTime());
        return compensate;
    }

    private Compensate toCompensate2(CompensateInfoExt compensateInfo) {
        Compensate compensate = toCompensate(compensateInfo);
        compensate.setRetryCount(compensateInfo.getRetryCount());
        compensate.setAlarmCount(compensateInfo.getAlarmCount());
        return compensate;
    }

}
