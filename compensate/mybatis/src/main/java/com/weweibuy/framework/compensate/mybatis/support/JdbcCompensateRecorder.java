package com.weweibuy.framework.compensate.mybatis.support;

import com.weweibuy.framework.compensate.config.CompensateConfigurer;
import com.weweibuy.framework.compensate.model.CompensateResult;
import com.weweibuy.framework.compensate.model.CompensateStatus;
import com.weweibuy.framework.compensate.model.CompensateTriggerType;
import com.weweibuy.framework.compensate.mybatis.mapper.CompensateLogMapper;
import com.weweibuy.framework.compensate.mybatis.po.CompensateLog;
import com.weweibuy.framework.compensate.support.CompensateRecorder;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;

/**
 * @author durenhao
 * @date 2020/9/6 19:37
 **/
public class JdbcCompensateRecorder implements CompensateRecorder, InitializingBean {

    private static final String INIT_COMPENSATE_STATE = "INIT";

    @Autowired
    private CompensateLogMapper compensateLogMapper;

    @Autowired(required = false)
    private List<CompensateConfigurer> configurerList;

    private ExecutorService executorService;

    @Override
    public void recorderCompensateCreate(String id, Exception e) {
        String message = e.getMessage();
        if (executorService != null) {
            executorService.execute(() -> insert(id, message));
        } else {
            insert(id, message);
        }
    }

    @Override
    public void recorderCompensate(CompensateResult compensateResult, Boolean force, CompensateStatus compensateStatus) {
        if (executorService != null) {
            executorService.execute(() -> insert(compensateResult, force, compensateStatus));
        } else {
            insert(compensateResult, force, compensateStatus);
        }
    }


    public void insert(String id, String exceptionMsg) {
        CompensateLog compensateLog = new CompensateLog();
        compensateLog.setCompensateId(Long.valueOf(id));
        compensateLog.setExceptionInfo(exceptionMsg);
        compensateLog.setTriggerType(CompensateTriggerType.SYSTEM.toString());
        compensateLog.setCompensateState(INIT_COMPENSATE_STATE);
        insertCompensateLog(compensateLog);
    }


    public void insert(CompensateResult compensateResult, Boolean force, CompensateStatus compensateStatus) {
        CompensateLog compensateLog = new CompensateLog();
        compensateLog.setCompensateId(Long.valueOf(compensateResult.getId()));
        compensateLog.setCompensateResult(compensateResult.getResult().toString());
        compensateLog.setExceptionInfo(compensateResult.getExceptionMsg());
        compensateLog.setTriggerType(force ? CompensateTriggerType.FORCE.toString()
                : CompensateTriggerType.SYSTEM.toString());

        compensateLog.setCompensateState(compensateStatus.toString());
        insertCompensateLog(compensateLog);
    }


    private void insertCompensateLog(CompensateLog compensateLog) {
        compensateLogMapper.insertSelective(compensateLog);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        executorService = Optional.ofNullable(configurerList)
                .map(l -> l.get(0))
                .map(CompensateConfigurer::getCompensateExecutorService)
                .orElse(null);
    }
}
