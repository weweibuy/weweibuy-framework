package com.weweibuy.framework.biztask.support;

import com.weweibuy.framework.biztask.core.BizTaskConfigure;
import com.weweibuy.framework.biztask.core.BizTaskExecConfigure;
import com.weweibuy.framework.biztask.db.po.BizTask;
import com.weweibuy.framework.biztask.db.repository.BizTaskRepository;
import com.weweibuy.framework.biztask.eum.BizTaskStatusEum;
import com.weweibuy.framework.common.core.support.AlarmService;
import com.weweibuy.framework.common.core.support.ExecRuleParser;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

/**
 * 业务任务操作, 只能在spring启动后使用
 *
 * @author durenhao
 * @date 2024/1/20 14:11
 **/
public class BizTaskHelper {

    private static BizTaskRepository bizTaskRepository;

    private static BizTaskExecConfigure bizTaskExecConfigure;

    private static AlarmService alarmService;

    private static final String ALARM_BIZ_TYPE = "common_biz_task_type";

    private static final String ALARM_FORMAT = "【业务任务】id: %s, 任务类型: %s, 业务id: %s, 业务状态: %s, 第: %s 次执行失败" +
            "\r\n原因: %s";

    public BizTaskHelper(BizTaskRepository repository, BizTaskExecConfigure configure, AlarmService alarm) {
        bizTaskRepository = repository;
        bizTaskExecConfigure = configure;
        alarmService = alarm;
    }


    public static Optional<BizTask> query(Long id) {
        return bizTaskRepository.select(id);
    }

    public static BizTask createAndSaveTask(Long bizId, BizTaskStatusEum taskStatus, String taskType,
                                            Integer bizStatus, String taskParam,
                                            Long execDelay, ChronoUnit delayTimeUnit) {
        BizTask bizTask = new BizTask();
        bizTask.setBizId(bizId);
        bizTask.setTaskType(taskType);
        bizTask.setTaskStatus(taskStatus.getCode());
        bizTask.setBizStatus(bizStatus);
        bizTask.setTaskParam(taskParam);
        bizTask.setTriggerCount(0);
        LocalDateTime triggerTime = LocalDateTime.now().plus(execDelay, delayTimeUnit);
        bizTask.setNextTriggerTime(triggerTime);
        bizTaskRepository.insert(bizTask);
        return bizTask;
    }


    public static int successTask(BizTask bizTask) {
        BizTask bizTaskUpdate = new BizTask();
        bizTaskUpdate.setTaskStatus(BizTaskStatusEum.SUCCESS.getCode());
        bizTaskUpdate.setTriggerCount(bizTask.getTriggerCount() + 1);
        return updateTask(bizTask.getId(), bizTaskUpdate, bizTask.getTaskStatus(), bizTask.getBizStatus());
    }

    public static int failTask(BizTask bizTask, String remark) {
        BizTask bizTaskUpdate = new BizTask();
        bizTaskUpdate.setTaskStatus(BizTaskStatusEum.FAIL.getCode());
        bizTaskUpdate.setTriggerCount(bizTask.getTriggerCount() + 1);
        bizTaskUpdate.setRemark(remark);
        return updateTask(bizTask.getId(), bizTaskUpdate, bizTask.getTaskStatus(), bizTask.getBizStatus());
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

    public static int delayTask(BizTask bizTask, Long execDelay, ChronoUnit delayTimeUnit) {
        BizTask bizTaskUpdate = new BizTask();
        bizTaskUpdate.setNextTriggerTime(LocalDateTime.now().plus(execDelay, delayTimeUnit));
        bizTaskUpdate.setTriggerCount(bizTask.getTriggerCount() + 1);
        return updateTask(bizTask.getId(), bizTaskUpdate, bizTask.getTaskStatus(), bizTask.getBizStatus());
    }

    public static int delayTask(BizTask bizTask) {
        BizTaskConfigure taskConfigure = bizTaskExecConfigure.getOrDefault(bizTask.getTaskType());
        String rule = Optional.ofNullable(taskConfigure.getRule())
                .orElse(BizTaskExecConfigure.DEFAULT_RULE);
        long time = ExecRuleParser.parser(bizTask.getTriggerCount(), rule);
        return delayTask(bizTask, time, ChronoUnit.MILLIS);
    }


    public static void alarmTaskIfNecessity(BizTask task, Throwable e) {
        BizTaskConfigure taskConfigure = bizTaskExecConfigure.getOrDefault(task.getTaskType());
        Integer alarmAtCount = Optional.ofNullable(taskConfigure.getAlarmAtCount())
                .orElse(BizTaskExecConfigure.DEFAULT_ALARM_AT_COUNT);

        Integer triggerCount = task.getTriggerCount() + 1;
        if (alarmAtCount != null && triggerCount >= alarmAtCount) {
            alarmService.sendAlarmFormatMsg(AlarmService.AlarmLevel.WARN, ALARM_BIZ_TYPE, ALARM_FORMAT,
                    task.getId(), task.getTaskType(), task.getBizId(), task.getBizStatus(), triggerCount,
                    e.getMessage());
        }
    }

    public static void failOrDelayTask(BizTask task) {
        BizTaskConfigure taskConfigure = bizTaskExecConfigure.getOrDefault(task.getTaskType());
        Integer maxCount = Optional.ofNullable(taskConfigure.getMaxExecCount())
                .orElse(BizTaskExecConfigure.DEFAULT_MAX_EXEC_COUNT);

        String rule = Optional.ofNullable(taskConfigure.getRule())
                .orElse(BizTaskExecConfigure.DEFAULT_RULE);

        long time = ExecRuleParser.parser(task.getTriggerCount(), rule);
        if (time < 0 || task.getTriggerCount() + 1 >= maxCount) {
            failTask(task, null);
        } else {
            delayTask(task, time, ChronoUnit.MILLIS);
        }
    }


    public static BizTaskBuilder bizTaskBuilder() {
        return new BizTaskBuilder();
    }

    public static BizTaskTransformer bizTaskTransformer(BizTask bizTask) {
        return new BizTaskTransformer().bizTask(bizTask);
    }

    /**
     * 创建任务
     */
    public static class BizTaskBuilder {

        private Long bizId;

        private String taskType;

        private String taskParam;

        private BizTaskStatusEum taskStatus;

        private Integer bizStatus;

        private Long execDelay;

        private ChronoUnit delayTimeUnit;

        BizTaskBuilder() {
        }

        public BizTaskBuilder bizId(Long bizId) {
            this.bizId = bizId;
            return this;
        }

        public BizTaskBuilder taskType(String taskType) {
            this.taskType = taskType;
            return this;
        }

        public BizTaskBuilder taskParam(String taskParam) {
            this.taskParam = taskParam;
            return this;
        }

        public BizTaskBuilder delay(Long execDelay, ChronoUnit delayTimeUnit) {
            this.execDelay = execDelay;
            this.delayTimeUnit = delayTimeUnit;
            return this;
        }

        public BizTaskBuilder delayByConfigure() {
            if (taskType == null) {
                throw new NullPointerException("taskType为空");
            }
            BizTaskConfigure taskConfigure = bizTaskExecConfigure.getOrDefault(taskType);
            String rule = Optional.ofNullable(taskConfigure.getRule())
                    .orElse(BizTaskExecConfigure.DEFAULT_RULE);

            long delay = ExecRuleParser.parser(0, rule);
            this.execDelay = delay;
            this.delayTimeUnit = ChronoUnit.MILLIS;
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
            BizTaskStatusEum bizTaskStatusEum = Optional.ofNullable(taskStatus)
                    .orElse(BizTaskStatusEum.EXEC_ING);
            if (execDelay == null || delayTimeUnit == null) {
                delayByConfigure();
            }
            return createAndSaveTask(bizId, bizTaskStatusEum, taskType,
                    bizStatus, taskParam, execDelay, delayTimeUnit);
        }

    }


    /**
     * 任务转换, 更改任务的任务类型, 业务id, 业务状态, 执行次数, 下次触发时间
     */
    public static class BizTaskTransformer {

        private BizTask bizTask;

        private Long newBizId;

        private String newTaskType;

        private Integer newBizStatus;

        private String newTaskParam;

        private Long execDelay;

        private ChronoUnit delayTimeUnit;

        private BizTaskStatusEum newTaskStatus;


        private BizTaskTransformer bizTask(BizTask bizTask) {
            this.bizTask = bizTask;
            return this;
        }

        public BizTaskTransformer newBizId(Long newBizId) {
            this.newBizId = newBizId;
            return this;
        }

        public BizTaskTransformer newTaskType(String newTaskType) {
            this.newTaskType = newTaskType;
            return this;
        }

        public BizTaskTransformer newTaskParam(String newTaskParam) {
            this.newTaskParam = newTaskParam;
            return this;
        }

        public BizTaskTransformer delay(Long execDelay, ChronoUnit delayTimeUnit) {
            this.execDelay = execDelay;
            this.delayTimeUnit = delayTimeUnit;
            return this;
        }

        public BizTaskTransformer newTaskStatus(BizTaskStatusEum newTaskStatus) {
            this.newTaskStatus = newTaskStatus;
            return this;
        }

        public BizTaskTransformer newBizStatus(Integer newBizStatus) {
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

            bizTaskUpdate.setBizId(newBizId);
            bizTask.setBizId(newBizId);

            bizTaskUpdate.setTaskType(newTaskType);
            bizTask.setTaskType(newTaskType);

            if (newTaskStatus != null) {
                Integer newTaskStatusCode = newTaskStatus.getCode();
                bizTask.setTaskStatus(newTaskStatusCode);
                bizTaskUpdate.setTaskStatus(newTaskStatusCode);
            }

            if (newBizStatus != null) {
                bizTaskUpdate.setBizStatus(newBizStatus);
                bizTask.setBizStatus(newBizStatus);
            }

            bizTaskUpdate.setTriggerCount(0);
            bizTask.setTriggerCount(0);

            LocalDateTime nextTriggerTime = null;
            if (execDelay != null && delayTimeUnit != null) {
                nextTriggerTime = LocalDateTime.now().plus(execDelay, delayTimeUnit);
            } else {
                BizTaskConfigure taskConfigure = bizTaskExecConfigure.getOrDefault(newTaskType);
                String rule = Optional.ofNullable(taskConfigure.getRule())
                        .orElse(BizTaskExecConfigure.DEFAULT_RULE);
                long delay = ExecRuleParser.parser(0, rule);
                nextTriggerTime = LocalDateTime.now().plus(delay, ChronoUnit.MILLIS);
            }

            bizTaskUpdate.setNextTriggerTime(nextTriggerTime);
            bizTask.setNextTriggerTime(nextTriggerTime);

            if (newTaskParam != null) {
                bizTaskUpdate.setTaskParam(newTaskParam);
                bizTask.setTaskParam(newTaskParam);
            }

            return updateTask(bizTask.getId(), bizTaskUpdate, oriTaskStatus, oriBizStatus);
        }

    }


}
