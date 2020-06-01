package com.weweibuy.framework.compensate.core;

import com.weweibuy.framework.compensate.interfaces.CompensateConfigStore;
import com.weweibuy.framework.compensate.interfaces.CompensateStore;
import com.weweibuy.framework.compensate.interfaces.CompensateTrigger;
import com.weweibuy.framework.compensate.interfaces.model.CompensateInfoExt;
import com.weweibuy.framework.compensate.interfaces.model.CompensateResult;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * CompensateTrigger 抽象实现
 *
 * @author durenhao
 * @date 2020/5/31 17:40
 **/
public abstract class AbstractCompensateTrigger implements CompensateTrigger, ApplicationContextAware, InitializingBean {

    private CompensateStore compensateStore;

    private CompensateHandlerService compensateHandlerService;

    private CompensateConfigStore compensateConfigStore;

    private ApplicationContext applicationContext;

    @Override
    public List<CompensateResult> trigger(Object... args) {
        Collection<CompensateInfoExt> compensateInfoExtCollection = queryCompensateInfo();
        return doTrigger(compensateInfoExtCollection);
    }

    /**
     * 触发补偿
     *
     * @param compensateInfoExtCollection
     */
    private List<CompensateResult> doTrigger(Collection<CompensateInfoExt> compensateInfoExtCollection) {
        if (CollectionUtils.isNotEmpty(compensateInfoExtCollection)) {
            return compensateHandlerService.compensate(compensateInfoExtCollection);
        }
        return Collections.emptyList();
    }

    /**
     * 强制触发补偿
     *
     * @param idSet
     */
    @Override
    public List<CompensateResult> forceTrigger(Set<String> idSet) {
        if (CollectionUtils.isNotEmpty(idSet)) {
            Collection<CompensateInfoExt> compensateInfoExtCollection = compensateStore.queryCompensateInfoByIdForce(idSet);
            if (CollectionUtils.isNotEmpty(compensateInfoExtCollection)) {
                return compensateHandlerService.compensateForce(compensateInfoExtCollection);
            }
        }
        return Collections.emptyList();
    }

    /**
     * 查询出补偿信息
     *
     * @param
     * @return
     */
    protected Collection<CompensateInfoExt> queryCompensateInfo() {
        return compensateStore.queryCompensateInfo(compensateConfigStore.getTriggerLimit());
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        this.compensateStore = applicationContext.getBean(CompensateStore.class);
        this.compensateHandlerService = applicationContext.getBean(CompensateHandlerService.class);
        this.compensateConfigStore = applicationContext.getBean(CompensateConfigStore.class);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}
