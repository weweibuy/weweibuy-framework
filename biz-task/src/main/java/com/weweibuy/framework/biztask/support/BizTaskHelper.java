package com.weweibuy.framework.biztask.support;

import com.weweibuy.framework.biztask.db.po.BizTask;
import com.weweibuy.framework.biztask.db.repository.BizTaskRepository;
import com.weweibuy.framework.biztask.eum.BizTaskStatusEum;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author durenhao
 * @date 2024/1/20 14:11
 **/
@RequiredArgsConstructor
public class BizTaskHelper {

    private final BizTaskRepository repository;

    private static BizTaskRepository bizTaskRepository;


    public static BizTask createAndSaveTask(Long bizId, BizTaskStatusEum taskStatus, String bizType,
                                            Integer bizStatus, String taskParam,
                                            Long execDelay, TimeUnit delayTimeUnit) {
        BizTask bizTask = new BizTask();
        bizTask.setBizId(bizId);
        bizTask.setBizType(bizType);
        bizTask.setTaskStatus(taskStatus.getCode());
        bizTask.setBizStatus(bizStatus);
        bizTask.setBizParam(taskParam);
        LocalDateTime triggerTime = LocalDateTime.now().plusSeconds(delayTimeUnit.toSeconds(execDelay));
        bizTask.setNextTriggerTime(triggerTime);
        bizTaskRepository.insert(bizTask);
        return bizTask;
    }

    public static int finishTask(BizTask bizTask) {
        BizTask bizTaskUpdate = new BizTask();
        bizTaskUpdate.setTaskStatus(BizTaskStatusEum.SUCCESS.getCode());
        return updateTask(bizTask.getId(), bizTaskUpdate, bizTask.getBizStatus(), bizTask.getBizStatus());

    }

    public static int updateTask(Long id, BizTask update, Integer taskStatus, Integer bizStatus) {
        BizTaskStatusEum bizTaskStatusEum = BizTaskStatusEum.fromCodeOrThrow(taskStatus);
        return bizTaskRepository.update(id, update, bizTaskStatusEum, bizStatus);
    }

    public static int startTask(BizTask bizTask) {
        if (!BizTaskStatusEum.WAIT_EXEC.getCode().equals(bizTask.getTaskStatus())) {
            throw new IllegalStateException("bizTask的状态不是待执行,无法启动, bizTask: " + bizTask);
        }
        BizTask bizTaskUpdate = new BizTask();
        bizTaskUpdate.setTaskStatus(BizTaskStatusEum.EXEC_ING.getCode());
        return updateTask(bizTask.getId(), bizTaskUpdate, bizTask.getTaskStatus(), bizTask.getBizStatus());
    }

    public static int delayTask(BizTask bizTask, Long execDelay, TimeUnit delayTimeUnit, boolean addCount) {
        BizTask bizTaskUpdate = new BizTask();
        bizTaskUpdate.setNextTriggerTime(LocalDateTime.now().plusSeconds(delayTimeUnit.toSeconds(execDelay)));
        if (addCount) {
            bizTaskUpdate.setTriggerCount(bizTask.getTriggerCount() + 1);
        }

        return updateTask(bizTask.getId(), bizTaskUpdate, bizTask.getTaskStatus(), bizTask.getBizStatus());
    }


    public static class BizTaskTransformer {

        private BizTask bizTask;

        private Long newBizId;

        private String newBizType;

        private Integer newBizStatus;

        private String newBizParam;

        private Long execDelay;

        private TimeUnit delayTimeUnit;

        private BizTaskStatusEum taskStatus;


        public BizTaskTransformer bizTask(BizTask bizTask) {
            this.bizTask = bizTask;
            return this;
        }

        public BizTaskTransformer bizId(Long newBizId) {
            this.newBizId = newBizId;
            return this;
        }

        public BizTaskTransformer bizType(String newBizType) {
            this.newBizType = newBizType;
            return this;
        }

        public BizTaskTransformer bizParam(String newBizParam) {
            this.newBizParam = newBizParam;
            return this;
        }

        public BizTaskTransformer delay(Long execDelay, TimeUnit delayTimeUnit) {
            this.execDelay = execDelay;
            this.delayTimeUnit = delayTimeUnit;
            return this;
        }

        public BizTaskTransformer taskStatus(BizTaskStatusEum taskStatus) {
            this.taskStatus = taskStatus;
            return this;
        }

        public BizTaskTransformer bizStatus(Integer newBizStatus) {
            this.newBizStatus = newBizStatus;
            return this;
        }

        public int transformerTask() {
            if (bizTask == null) {
                throw new NullPointerException("bizTask不能为空");
            }

            Integer oriTaskStatus = bizTask.getTaskStatus();
            Integer oriBizStatus = bizTask.getBizStatus();

            BizTask bizTaskUpdate = new BizTask();

            bizTask.setBizId(newBizId);
            bizTaskUpdate.setBizId(newBizId);

            bizTask.setBizType(newBizType);
            bizTaskUpdate.setBizType(newBizType);
            Integer statusCode = Optional.ofNullable(taskStatus)
                    .map(BizTaskStatusEum::getCode)
                    .orElse(BizTaskStatusEum.EXEC_ING.getCode());
            bizTask.setTaskStatus(statusCode);
            bizTaskUpdate.setTaskStatus(statusCode);

            if (newBizStatus != null) {
                bizTaskUpdate.setBizStatus(newBizStatus);
                bizTask.setBizStatus(newBizStatus);

            }

            bizTaskUpdate.setTriggerCount(0);
            bizTask.setTriggerCount(0);

            LocalDateTime nextTriggerTime = null;
            if (execDelay != null && delayTimeUnit != null) {
                nextTriggerTime = LocalDateTime.now().plusSeconds(delayTimeUnit.toSeconds(execDelay));
            } else {
                nextTriggerTime = LocalDateTime.now().plusMinutes(1);
            }
            bizTaskUpdate.setNextTriggerTime(nextTriggerTime);
            bizTask.setNextTriggerTime(nextTriggerTime);

            if (newBizParam != null) {
                bizTaskUpdate.setBizParam(newBizParam);
                bizTask.setBizParam(newBizParam);
            }

            return updateTask(bizTask.getId(), bizTaskUpdate, oriTaskStatus, oriBizStatus);
        }

    }

    public static class BizTaskBuilder {

        private Long bizId;

        private String bizType;

        private String bizParam;

        private BizTaskStatusEum taskStatus;

        private Integer bizStatus;

        private Long execDelay;

        private TimeUnit delayTimeUnit;

        BizTaskBuilder() {
        }

        public BizTaskBuilder bizId(Long bizId) {
            this.bizId = bizId;
            return this;
        }

        public BizTaskBuilder bizType(String bizType) {
            this.bizType = bizType;
            return this;
        }

        public BizTaskBuilder bizParam(String bizParam) {
            this.bizParam = bizParam;
            return this;
        }

        public BizTaskBuilder delay(Long execDelay, TimeUnit delayTimeUnit) {
            this.execDelay = execDelay;
            this.delayTimeUnit = delayTimeUnit;
            return this;
        }


        public BizTaskBuilder taskStatus(BizTaskStatusEum taskStatus) {
            this.taskStatus = taskStatus;
            return this;
        }

        public BizTaskBuilder bizStatus(Integer bizStatus) {
            this.bizStatus = bizStatus;
            return this;
        }

        public BizTask buildAndSave() {
            return createAndSaveTask(bizId, taskStatus, bizType,
                    bizStatus, bizParam, execDelay, delayTimeUnit);
        }

    }


}
