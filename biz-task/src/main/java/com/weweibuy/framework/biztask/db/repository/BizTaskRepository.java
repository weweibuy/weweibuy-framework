package com.weweibuy.framework.biztask.db.repository;

import com.weweibuy.framework.biztask.db.mapper.BizTaskMapper;
import com.weweibuy.framework.biztask.db.po.BizTask;
import com.weweibuy.framework.biztask.db.po.BizTaskExample;
import com.weweibuy.framework.biztask.eum.BizTaskStatusEum;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author durenhao
 * @date 2024/1/19 21:48
 **/
@RequiredArgsConstructor
public class BizTaskRepository {

    private final BizTaskMapper bizTaskMapper;


    public List<BizTask> selectExecIngTask(Integer taskPartition, Integer totalPartition, Integer limit, Long startId) {
        BizTaskExample example = BizTaskExample.newAndCreateCriteria()
                .andDeletedEqualTo(false)
                .andTaskStatusEqualTo(BizTaskStatusEum.EXEC_ING.getCode())
                .andNextTriggerTimeLessThan(LocalDateTime.now())
                .when(startId != null, c -> c.andIdGreaterThan(startId))
                .when(taskPartition != null && totalPartition != null,
                        c -> c.addCustomerCriterion(String.format(" mod(id, %s) = %s", totalPartition, taskPartition)))
                .example()
                .limit(limit)
                .orderBy("id asc");
        return bizTaskMapper.selectByExample(example);
    }


    public int insert(BizTask bizTask) {
        return bizTaskMapper.insertSelective(bizTask);
    }

    public int update(Long id, BizTask bizTask, BizTaskStatusEum taskStatus, Integer bizStatus) {
        return bizTaskMapper.updateByExampleSelective(bizTask, BizTaskExample.newAndCreateCriteria()
                .andIdEqualTo(id)
                .andDeletedEqualTo(false)
                .when(taskStatus != null,
                        criteria -> criteria.andTaskStatusEqualTo(taskStatus.getCode()))
                .when(bizStatus != null,
                        criteria -> criteria.andBizStatusEqualTo(bizStatus))
                .example());
    }

    public Optional<BizTask> select(Long id) {
        return Optional.ofNullable(bizTaskMapper.selectOneByExample(
                BizTaskExample.newAndCreateCriteria()
                        .andIdEqualTo(id)
                        .andDeletedEqualTo(false)
                        .example()
        ));
    }

    public Optional<BizTask> select(String taskType, Long bizId) {
        return Optional.ofNullable(bizTaskMapper.selectOneByExample(
                BizTaskExample.newAndCreateCriteria()
                        .andTaskTypeEqualTo(taskType)
                        .andBizIdEqualTo(bizId)
                        .andDeletedEqualTo(false)
                        .example()
        ));
    }
}
