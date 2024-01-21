package com.weweibuy.framework.biztask.db.po;

import com.weweibuy.framework.common.db.utils.SqlUtils;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BizTaskExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected Integer offset;

    protected Integer rows;

    public BizTaskExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public BizTaskExample orderBy(String orderByClause) {
        this.setOrderByClause(orderByClause);
        return this;
    }

    public BizTaskExample orderBy(String ... orderByClauses) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < orderByClauses.length; i++) {
            sb.append(orderByClauses[i]);
            if (i < orderByClauses.length - 1) {
                sb.append(" , ");
            }
        }
        this.setOrderByClause(sb.toString());
        return this;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria(this);
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
        rows = null;
        offset = null;
    }

    public static Criteria newAndCreateCriteria() {
        BizTaskExample example = new BizTaskExample();
        return example.createCriteria();
    }

    public BizTaskExample when(boolean condition, IExampleWhen then) {
        if (condition) {
            then.example(this);
        }
        return this;
    }

    public BizTaskExample when(boolean condition, IExampleWhen then, IExampleWhen otherwise) {
        if (condition) {
            then.example(this);
        } else {
            otherwise.example(this);
        }
        return this;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getOffset() {
        return this.offset;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public Integer getRows() {
        return this.rows;
    }

    public BizTaskExample limit(Integer rows) {
        this.rows = rows;
        return this;
    }

    public BizTaskExample limit(Integer offset, Integer rows) {
        this.offset = offset;
        this.rows = rows;
        return this;
    }

    public BizTaskExample page(Integer page, Integer pageSize) {
        this.offset = page * pageSize;
        this.rows = pageSize;
        return this;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andBizIdIsNull() {
            addCriterion("biz_id is null");
            return (Criteria) this;
        }

        public Criteria andBizIdIsNotNull() {
            addCriterion("biz_id is not null");
            return (Criteria) this;
        }

        public Criteria andBizIdEqualTo(Long value) {
            addCriterion("biz_id =", value, "bizId");
            return (Criteria) this;
        }

        public Criteria andBizIdNotEqualTo(Long value) {
            addCriterion("biz_id <>", value, "bizId");
            return (Criteria) this;
        }

        public Criteria andBizIdGreaterThan(Long value) {
            addCriterion("biz_id >", value, "bizId");
            return (Criteria) this;
        }

        public Criteria andBizIdGreaterThanOrEqualTo(Long value) {
            addCriterion("biz_id >=", value, "bizId");
            return (Criteria) this;
        }

        public Criteria andBizIdLessThan(Long value) {
            addCriterion("biz_id <", value, "bizId");
            return (Criteria) this;
        }

        public Criteria andBizIdLessThanOrEqualTo(Long value) {
            addCriterion("biz_id <=", value, "bizId");
            return (Criteria) this;
        }

        public Criteria andBizIdIn(List<Long> values) {
            addCriterion("biz_id in", values, "bizId");
            return (Criteria) this;
        }

        public Criteria andBizIdNotIn(List<Long> values) {
            addCriterion("biz_id not in", values, "bizId");
            return (Criteria) this;
        }

        public Criteria andBizIdBetween(Long value1, Long value2) {
            addCriterion("biz_id between", value1, value2, "bizId");
            return (Criteria) this;
        }

        public Criteria andBizIdNotBetween(Long value1, Long value2) {
            addCriterion("biz_id not between", value1, value2, "bizId");
            return (Criteria) this;
        }

        public Criteria andTaskTypeIsNull() {
            addCriterion("task_type is null");
            return (Criteria) this;
        }

        public Criteria andTaskTypeIsNotNull() {
            addCriterion("task_type is not null");
            return (Criteria) this;
        }

        public Criteria andTaskTypeEqualTo(String value) {
            addCriterion("task_type =", value, "taskType");
            return (Criteria) this;
        }

        public Criteria andTaskTypeNotEqualTo(String value) {
            addCriterion("task_type <>", value, "taskType");
            return (Criteria) this;
        }

        public Criteria andTaskTypeGreaterThan(String value) {
            addCriterion("task_type >", value, "taskType");
            return (Criteria) this;
        }

        public Criteria andTaskTypeGreaterThanOrEqualTo(String value) {
            addCriterion("task_type >=", value, "taskType");
            return (Criteria) this;
        }

        public Criteria andTaskTypeLessThan(String value) {
            addCriterion("task_type <", value, "taskType");
            return (Criteria) this;
        }

        public Criteria andTaskTypeLessThanOrEqualTo(String value) {
            addCriterion("task_type <=", value, "taskType");
            return (Criteria) this;
        }

        public Criteria andTaskTypeLike(String value) {
            addCriterion("task_type like", value, "taskType");
            return (Criteria) this;
        }

        public Criteria andTaskTypeNotLike(String value) {
            addCriterion("task_type not like", value, "taskType");
            return (Criteria) this;
        }

        public Criteria andTaskTypeIn(List<String> values) {
            addCriterion("task_type in", values, "taskType");
            return (Criteria) this;
        }

        public Criteria andTaskTypeNotIn(List<String> values) {
            addCriterion("task_type not in", values, "taskType");
            return (Criteria) this;
        }

        public Criteria andTaskTypeBetween(String value1, String value2) {
            addCriterion("task_type between", value1, value2, "taskType");
            return (Criteria) this;
        }

        public Criteria andTaskTypeNotBetween(String value1, String value2) {
            addCriterion("task_type not between", value1, value2, "taskType");
            return (Criteria) this;
        }

        public Criteria andTaskParamIsNull() {
            addCriterion("task_param is null");
            return (Criteria) this;
        }

        public Criteria andTaskParamIsNotNull() {
            addCriterion("task_param is not null");
            return (Criteria) this;
        }

        public Criteria andTaskParamEqualTo(String value) {
            addCriterion("task_param =", value, "taskParam");
            return (Criteria) this;
        }

        public Criteria andTaskParamNotEqualTo(String value) {
            addCriterion("task_param <>", value, "taskParam");
            return (Criteria) this;
        }

        public Criteria andTaskParamGreaterThan(String value) {
            addCriterion("task_param >", value, "taskParam");
            return (Criteria) this;
        }

        public Criteria andTaskParamGreaterThanOrEqualTo(String value) {
            addCriterion("task_param >=", value, "taskParam");
            return (Criteria) this;
        }

        public Criteria andTaskParamLessThan(String value) {
            addCriterion("task_param <", value, "taskParam");
            return (Criteria) this;
        }

        public Criteria andTaskParamLessThanOrEqualTo(String value) {
            addCriterion("task_param <=", value, "taskParam");
            return (Criteria) this;
        }

        public Criteria andTaskParamLike(String value) {
            addCriterion("task_param like", value, "taskParam");
            return (Criteria) this;
        }

        public Criteria andTaskParamNotLike(String value) {
            addCriterion("task_param not like", value, "taskParam");
            return (Criteria) this;
        }

        public Criteria andTaskParamIn(List<String> values) {
            addCriterion("task_param in", values, "taskParam");
            return (Criteria) this;
        }

        public Criteria andTaskParamNotIn(List<String> values) {
            addCriterion("task_param not in", values, "taskParam");
            return (Criteria) this;
        }

        public Criteria andTaskParamBetween(String value1, String value2) {
            addCriterion("task_param between", value1, value2, "taskParam");
            return (Criteria) this;
        }

        public Criteria andTaskParamNotBetween(String value1, String value2) {
            addCriterion("task_param not between", value1, value2, "taskParam");
            return (Criteria) this;
        }

        public Criteria andTriggerCountIsNull() {
            addCriterion("trigger_count is null");
            return (Criteria) this;
        }

        public Criteria andTriggerCountIsNotNull() {
            addCriterion("trigger_count is not null");
            return (Criteria) this;
        }

        public Criteria andTriggerCountEqualTo(Integer value) {
            addCriterion("trigger_count =", value, "triggerCount");
            return (Criteria) this;
        }

        public Criteria andTriggerCountNotEqualTo(Integer value) {
            addCriterion("trigger_count <>", value, "triggerCount");
            return (Criteria) this;
        }

        public Criteria andTriggerCountGreaterThan(Integer value) {
            addCriterion("trigger_count >", value, "triggerCount");
            return (Criteria) this;
        }

        public Criteria andTriggerCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("trigger_count >=", value, "triggerCount");
            return (Criteria) this;
        }

        public Criteria andTriggerCountLessThan(Integer value) {
            addCriterion("trigger_count <", value, "triggerCount");
            return (Criteria) this;
        }

        public Criteria andTriggerCountLessThanOrEqualTo(Integer value) {
            addCriterion("trigger_count <=", value, "triggerCount");
            return (Criteria) this;
        }

        public Criteria andTriggerCountIn(List<Integer> values) {
            addCriterion("trigger_count in", values, "triggerCount");
            return (Criteria) this;
        }

        public Criteria andTriggerCountNotIn(List<Integer> values) {
            addCriterion("trigger_count not in", values, "triggerCount");
            return (Criteria) this;
        }

        public Criteria andTriggerCountBetween(Integer value1, Integer value2) {
            addCriterion("trigger_count between", value1, value2, "triggerCount");
            return (Criteria) this;
        }

        public Criteria andTriggerCountNotBetween(Integer value1, Integer value2) {
            addCriterion("trigger_count not between", value1, value2, "triggerCount");
            return (Criteria) this;
        }

        public Criteria andNextTriggerTimeIsNull() {
            addCriterion("next_trigger_time is null");
            return (Criteria) this;
        }

        public Criteria andNextTriggerTimeIsNotNull() {
            addCriterion("next_trigger_time is not null");
            return (Criteria) this;
        }

        public Criteria andNextTriggerTimeEqualTo(LocalDateTime value) {
            addCriterion("next_trigger_time =", value, "nextTriggerTime");
            return (Criteria) this;
        }

        public Criteria andNextTriggerTimeNotEqualTo(LocalDateTime value) {
            addCriterion("next_trigger_time <>", value, "nextTriggerTime");
            return (Criteria) this;
        }

        public Criteria andNextTriggerTimeGreaterThan(LocalDateTime value) {
            addCriterion("next_trigger_time >", value, "nextTriggerTime");
            return (Criteria) this;
        }

        public Criteria andNextTriggerTimeGreaterThanOrEqualTo(LocalDateTime value) {
            addCriterion("next_trigger_time >=", value, "nextTriggerTime");
            return (Criteria) this;
        }

        public Criteria andNextTriggerTimeLessThan(LocalDateTime value) {
            addCriterion("next_trigger_time <", value, "nextTriggerTime");
            return (Criteria) this;
        }

        public Criteria andNextTriggerTimeLessThanOrEqualTo(LocalDateTime value) {
            addCriterion("next_trigger_time <=", value, "nextTriggerTime");
            return (Criteria) this;
        }

        public Criteria andNextTriggerTimeIn(List<LocalDateTime> values) {
            addCriterion("next_trigger_time in", values, "nextTriggerTime");
            return (Criteria) this;
        }

        public Criteria andNextTriggerTimeNotIn(List<LocalDateTime> values) {
            addCriterion("next_trigger_time not in", values, "nextTriggerTime");
            return (Criteria) this;
        }

        public Criteria andNextTriggerTimeBetween(LocalDateTime value1, LocalDateTime value2) {
            addCriterion("next_trigger_time between", value1, value2, "nextTriggerTime");
            return (Criteria) this;
        }

        public Criteria andNextTriggerTimeNotBetween(LocalDateTime value1, LocalDateTime value2) {
            addCriterion("next_trigger_time not between", value1, value2, "nextTriggerTime");
            return (Criteria) this;
        }

        public Criteria andTaskStatusIsNull() {
            addCriterion("task_status is null");
            return (Criteria) this;
        }

        public Criteria andTaskStatusIsNotNull() {
            addCriterion("task_status is not null");
            return (Criteria) this;
        }

        public Criteria andTaskStatusEqualTo(Integer value) {
            addCriterion("task_status =", value, "taskStatus");
            return (Criteria) this;
        }

        public Criteria andTaskStatusNotEqualTo(Integer value) {
            addCriterion("task_status <>", value, "taskStatus");
            return (Criteria) this;
        }

        public Criteria andTaskStatusGreaterThan(Integer value) {
            addCriterion("task_status >", value, "taskStatus");
            return (Criteria) this;
        }

        public Criteria andTaskStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("task_status >=", value, "taskStatus");
            return (Criteria) this;
        }

        public Criteria andTaskStatusLessThan(Integer value) {
            addCriterion("task_status <", value, "taskStatus");
            return (Criteria) this;
        }

        public Criteria andTaskStatusLessThanOrEqualTo(Integer value) {
            addCriterion("task_status <=", value, "taskStatus");
            return (Criteria) this;
        }

        public Criteria andTaskStatusIn(List<Integer> values) {
            addCriterion("task_status in", values, "taskStatus");
            return (Criteria) this;
        }

        public Criteria andTaskStatusNotIn(List<Integer> values) {
            addCriterion("task_status not in", values, "taskStatus");
            return (Criteria) this;
        }

        public Criteria andTaskStatusBetween(Integer value1, Integer value2) {
            addCriterion("task_status between", value1, value2, "taskStatus");
            return (Criteria) this;
        }

        public Criteria andTaskStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("task_status not between", value1, value2, "taskStatus");
            return (Criteria) this;
        }

        public Criteria andBizStatusIsNull() {
            addCriterion("biz_status is null");
            return (Criteria) this;
        }

        public Criteria andBizStatusIsNotNull() {
            addCriterion("biz_status is not null");
            return (Criteria) this;
        }

        public Criteria andBizStatusEqualTo(Integer value) {
            addCriterion("biz_status =", value, "bizStatus");
            return (Criteria) this;
        }

        public Criteria andBizStatusNotEqualTo(Integer value) {
            addCriterion("biz_status <>", value, "bizStatus");
            return (Criteria) this;
        }

        public Criteria andBizStatusGreaterThan(Integer value) {
            addCriterion("biz_status >", value, "bizStatus");
            return (Criteria) this;
        }

        public Criteria andBizStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("biz_status >=", value, "bizStatus");
            return (Criteria) this;
        }

        public Criteria andBizStatusLessThan(Integer value) {
            addCriterion("biz_status <", value, "bizStatus");
            return (Criteria) this;
        }

        public Criteria andBizStatusLessThanOrEqualTo(Integer value) {
            addCriterion("biz_status <=", value, "bizStatus");
            return (Criteria) this;
        }

        public Criteria andBizStatusIn(List<Integer> values) {
            addCriterion("biz_status in", values, "bizStatus");
            return (Criteria) this;
        }

        public Criteria andBizStatusNotIn(List<Integer> values) {
            addCriterion("biz_status not in", values, "bizStatus");
            return (Criteria) this;
        }

        public Criteria andBizStatusBetween(Integer value1, Integer value2) {
            addCriterion("biz_status between", value1, value2, "bizStatus");
            return (Criteria) this;
        }

        public Criteria andBizStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("biz_status not between", value1, value2, "bizStatus");
            return (Criteria) this;
        }

        public Criteria andDeletedIsNull() {
            addCriterion("is_delete is null");
            return (Criteria) this;
        }

        public Criteria andDeletedIsNotNull() {
            addCriterion("is_delete is not null");
            return (Criteria) this;
        }

        public Criteria andDeletedEqualTo(Boolean value) {
            addCriterion("is_delete =", value, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedNotEqualTo(Boolean value) {
            addCriterion("is_delete <>", value, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedGreaterThan(Boolean value) {
            addCriterion("is_delete >", value, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedGreaterThanOrEqualTo(Boolean value) {
            addCriterion("is_delete >=", value, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedLessThan(Boolean value) {
            addCriterion("is_delete <", value, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedLessThanOrEqualTo(Boolean value) {
            addCriterion("is_delete <=", value, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedIn(List<Boolean> values) {
            addCriterion("is_delete in", values, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedNotIn(List<Boolean> values) {
            addCriterion("is_delete not in", values, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedBetween(Boolean value1, Boolean value2) {
            addCriterion("is_delete between", value1, value2, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedNotBetween(Boolean value1, Boolean value2) {
            addCriterion("is_delete not between", value1, value2, "deleted");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(LocalDateTime value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(LocalDateTime value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(LocalDateTime value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(LocalDateTime value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(LocalDateTime value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(LocalDateTime value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<LocalDateTime> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<LocalDateTime> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(LocalDateTime value1, LocalDateTime value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(LocalDateTime value1, LocalDateTime value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("update_time is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("update_time is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(LocalDateTime value) {
            addCriterion("update_time =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(LocalDateTime value) {
            addCriterion("update_time <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(LocalDateTime value) {
            addCriterion("update_time >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(LocalDateTime value) {
            addCriterion("update_time >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(LocalDateTime value) {
            addCriterion("update_time <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(LocalDateTime value) {
            addCriterion("update_time <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<LocalDateTime> values) {
            addCriterion("update_time in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<LocalDateTime> values) {
            addCriterion("update_time not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(LocalDateTime value1, LocalDateTime value2) {
            addCriterion("update_time between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(LocalDateTime value1, LocalDateTime value2) {
            addCriterion("update_time not between", value1, value2, "updateTime");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {
        private BizTaskExample example;

        protected Criteria(BizTaskExample example) {
            super();
            this.example = example;
        }

        public BizTaskExample example() {
            return this.example;
        }

        @Deprecated
        public Criteria andIf(boolean ifAdd, ICriteriaAdd add) {
            if (ifAdd) {
                add.add(this);
            }
            return this;
        }

        public Criteria when(boolean condition, ICriteriaWhen then) {
            if (condition) {
                then.criteria(this);
            }
            return this;
        }

        public Criteria when(boolean condition, ICriteriaWhen then, ICriteriaWhen otherwise) {
            if (condition) {
                then.criteria(this);
            } else {
                otherwise.criteria(this);
            }
            return this;
        }

        public Criteria addCustomerCriterion(String condition) {
            condition = SqlUtils.containsSqlInjectionForLikeAndThrow(condition);
            this.addCriterion(condition);
            return this;
        }

        @Deprecated
        public interface ICriteriaAdd {
            Criteria add(Criteria add);
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }

    public interface ICriteriaWhen {
        void criteria(Criteria criteria);
    }

    public interface IExampleWhen {
        void example(com.weweibuy.framework.biztask.db.po.BizTaskExample example);
    }
}