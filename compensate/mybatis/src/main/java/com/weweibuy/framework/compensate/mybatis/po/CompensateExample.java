package com.weweibuy.framework.compensate.mybatis.po;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CompensateExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public CompensateExample() {
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

    public CompensateExample orderBy(String orderByClause) {
        this.setOrderByClause(orderByClause);
        return this;
    }

    public CompensateExample orderBy(String ... orderByClauses) {
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
    }

    public static Criteria newAndCreateCriteria() {
        CompensateExample example = new CompensateExample();
        return example.createCriteria();
    }

    public CompensateExample when(boolean condition, IExampleWhen then) {
        if (condition) {
            then.example(this);
        }
        return this;
    }

    public CompensateExample when(boolean condition, IExampleWhen then, IExampleWhen otherwise) {
        if (condition) {
            then.example(this);
        } else {
            otherwise.example(this);
        }
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

        public Criteria andCompensateKeyIsNull() {
            addCriterion("compensate_key is null");
            return (Criteria) this;
        }

        public Criteria andCompensateKeyIsNotNull() {
            addCriterion("compensate_key is not null");
            return (Criteria) this;
        }

        public Criteria andCompensateKeyEqualTo(String value) {
            addCriterion("compensate_key =", value, "compensateKey");
            return (Criteria) this;
        }

        public Criteria andCompensateKeyNotEqualTo(String value) {
            addCriterion("compensate_key <>", value, "compensateKey");
            return (Criteria) this;
        }

        public Criteria andCompensateKeyGreaterThan(String value) {
            addCriterion("compensate_key >", value, "compensateKey");
            return (Criteria) this;
        }

        public Criteria andCompensateKeyGreaterThanOrEqualTo(String value) {
            addCriterion("compensate_key >=", value, "compensateKey");
            return (Criteria) this;
        }

        public Criteria andCompensateKeyLessThan(String value) {
            addCriterion("compensate_key <", value, "compensateKey");
            return (Criteria) this;
        }

        public Criteria andCompensateKeyLessThanOrEqualTo(String value) {
            addCriterion("compensate_key <=", value, "compensateKey");
            return (Criteria) this;
        }

        public Criteria andCompensateKeyLike(String value) {
            addCriterion("compensate_key like", value, "compensateKey");
            return (Criteria) this;
        }

        public Criteria andCompensateKeyNotLike(String value) {
            addCriterion("compensate_key not like", value, "compensateKey");
            return (Criteria) this;
        }

        public Criteria andCompensateKeyIn(List<String> values) {
            addCriterion("compensate_key in", values, "compensateKey");
            return (Criteria) this;
        }

        public Criteria andCompensateKeyNotIn(List<String> values) {
            addCriterion("compensate_key not in", values, "compensateKey");
            return (Criteria) this;
        }

        public Criteria andCompensateKeyBetween(String value1, String value2) {
            addCriterion("compensate_key between", value1, value2, "compensateKey");
            return (Criteria) this;
        }

        public Criteria andCompensateKeyNotBetween(String value1, String value2) {
            addCriterion("compensate_key not between", value1, value2, "compensateKey");
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

        public Criteria andBizIdEqualTo(String value) {
            addCriterion("biz_id =", value, "bizId");
            return (Criteria) this;
        }

        public Criteria andBizIdNotEqualTo(String value) {
            addCriterion("biz_id <>", value, "bizId");
            return (Criteria) this;
        }

        public Criteria andBizIdGreaterThan(String value) {
            addCriterion("biz_id >", value, "bizId");
            return (Criteria) this;
        }

        public Criteria andBizIdGreaterThanOrEqualTo(String value) {
            addCriterion("biz_id >=", value, "bizId");
            return (Criteria) this;
        }

        public Criteria andBizIdLessThan(String value) {
            addCriterion("biz_id <", value, "bizId");
            return (Criteria) this;
        }

        public Criteria andBizIdLessThanOrEqualTo(String value) {
            addCriterion("biz_id <=", value, "bizId");
            return (Criteria) this;
        }

        public Criteria andBizIdLike(String value) {
            addCriterion("biz_id like", value, "bizId");
            return (Criteria) this;
        }

        public Criteria andBizIdNotLike(String value) {
            addCriterion("biz_id not like", value, "bizId");
            return (Criteria) this;
        }

        public Criteria andBizIdIn(List<String> values) {
            addCriterion("biz_id in", values, "bizId");
            return (Criteria) this;
        }

        public Criteria andBizIdNotIn(List<String> values) {
            addCriterion("biz_id not in", values, "bizId");
            return (Criteria) this;
        }

        public Criteria andBizIdBetween(String value1, String value2) {
            addCriterion("biz_id between", value1, value2, "bizId");
            return (Criteria) this;
        }

        public Criteria andBizIdNotBetween(String value1, String value2) {
            addCriterion("biz_id not between", value1, value2, "bizId");
            return (Criteria) this;
        }

        public Criteria andCompensateTypeIsNull() {
            addCriterion("compensate_type is null");
            return (Criteria) this;
        }

        public Criteria andCompensateTypeIsNotNull() {
            addCriterion("compensate_type is not null");
            return (Criteria) this;
        }

        public Criteria andCompensateTypeEqualTo(String value) {
            addCriterion("compensate_type =", value, "compensateType");
            return (Criteria) this;
        }

        public Criteria andCompensateTypeNotEqualTo(String value) {
            addCriterion("compensate_type <>", value, "compensateType");
            return (Criteria) this;
        }

        public Criteria andCompensateTypeGreaterThan(String value) {
            addCriterion("compensate_type >", value, "compensateType");
            return (Criteria) this;
        }

        public Criteria andCompensateTypeGreaterThanOrEqualTo(String value) {
            addCriterion("compensate_type >=", value, "compensateType");
            return (Criteria) this;
        }

        public Criteria andCompensateTypeLessThan(String value) {
            addCriterion("compensate_type <", value, "compensateType");
            return (Criteria) this;
        }

        public Criteria andCompensateTypeLessThanOrEqualTo(String value) {
            addCriterion("compensate_type <=", value, "compensateType");
            return (Criteria) this;
        }

        public Criteria andCompensateTypeLike(String value) {
            addCriterion("compensate_type like", value, "compensateType");
            return (Criteria) this;
        }

        public Criteria andCompensateTypeNotLike(String value) {
            addCriterion("compensate_type not like", value, "compensateType");
            return (Criteria) this;
        }

        public Criteria andCompensateTypeIn(List<String> values) {
            addCriterion("compensate_type in", values, "compensateType");
            return (Criteria) this;
        }

        public Criteria andCompensateTypeNotIn(List<String> values) {
            addCriterion("compensate_type not in", values, "compensateType");
            return (Criteria) this;
        }

        public Criteria andCompensateTypeBetween(String value1, String value2) {
            addCriterion("compensate_type between", value1, value2, "compensateType");
            return (Criteria) this;
        }

        public Criteria andCompensateTypeNotBetween(String value1, String value2) {
            addCriterion("compensate_type not between", value1, value2, "compensateType");
            return (Criteria) this;
        }

        public Criteria andMethodArgsIsNull() {
            addCriterion("method_args is null");
            return (Criteria) this;
        }

        public Criteria andMethodArgsIsNotNull() {
            addCriterion("method_args is not null");
            return (Criteria) this;
        }

        public Criteria andMethodArgsEqualTo(String value) {
            addCriterion("method_args =", value, "methodArgs");
            return (Criteria) this;
        }

        public Criteria andMethodArgsNotEqualTo(String value) {
            addCriterion("method_args <>", value, "methodArgs");
            return (Criteria) this;
        }

        public Criteria andMethodArgsGreaterThan(String value) {
            addCriterion("method_args >", value, "methodArgs");
            return (Criteria) this;
        }

        public Criteria andMethodArgsGreaterThanOrEqualTo(String value) {
            addCriterion("method_args >=", value, "methodArgs");
            return (Criteria) this;
        }

        public Criteria andMethodArgsLessThan(String value) {
            addCriterion("method_args <", value, "methodArgs");
            return (Criteria) this;
        }

        public Criteria andMethodArgsLessThanOrEqualTo(String value) {
            addCriterion("method_args <=", value, "methodArgs");
            return (Criteria) this;
        }

        public Criteria andMethodArgsLike(String value) {
            addCriterion("method_args like", value, "methodArgs");
            return (Criteria) this;
        }

        public Criteria andMethodArgsNotLike(String value) {
            addCriterion("method_args not like", value, "methodArgs");
            return (Criteria) this;
        }

        public Criteria andMethodArgsIn(List<String> values) {
            addCriterion("method_args in", values, "methodArgs");
            return (Criteria) this;
        }

        public Criteria andMethodArgsNotIn(List<String> values) {
            addCriterion("method_args not in", values, "methodArgs");
            return (Criteria) this;
        }

        public Criteria andMethodArgsBetween(String value1, String value2) {
            addCriterion("method_args between", value1, value2, "methodArgs");
            return (Criteria) this;
        }

        public Criteria andMethodArgsNotBetween(String value1, String value2) {
            addCriterion("method_args not between", value1, value2, "methodArgs");
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

        public Criteria andRetryCountIsNull() {
            addCriterion("retry_count is null");
            return (Criteria) this;
        }

        public Criteria andRetryCountIsNotNull() {
            addCriterion("retry_count is not null");
            return (Criteria) this;
        }

        public Criteria andRetryCountEqualTo(Integer value) {
            addCriterion("retry_count =", value, "retryCount");
            return (Criteria) this;
        }

        public Criteria andRetryCountNotEqualTo(Integer value) {
            addCriterion("retry_count <>", value, "retryCount");
            return (Criteria) this;
        }

        public Criteria andRetryCountGreaterThan(Integer value) {
            addCriterion("retry_count >", value, "retryCount");
            return (Criteria) this;
        }

        public Criteria andRetryCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("retry_count >=", value, "retryCount");
            return (Criteria) this;
        }

        public Criteria andRetryCountLessThan(Integer value) {
            addCriterion("retry_count <", value, "retryCount");
            return (Criteria) this;
        }

        public Criteria andRetryCountLessThanOrEqualTo(Integer value) {
            addCriterion("retry_count <=", value, "retryCount");
            return (Criteria) this;
        }

        public Criteria andRetryCountIn(List<Integer> values) {
            addCriterion("retry_count in", values, "retryCount");
            return (Criteria) this;
        }

        public Criteria andRetryCountNotIn(List<Integer> values) {
            addCriterion("retry_count not in", values, "retryCount");
            return (Criteria) this;
        }

        public Criteria andRetryCountBetween(Integer value1, Integer value2) {
            addCriterion("retry_count between", value1, value2, "retryCount");
            return (Criteria) this;
        }

        public Criteria andRetryCountNotBetween(Integer value1, Integer value2) {
            addCriterion("retry_count not between", value1, value2, "retryCount");
            return (Criteria) this;
        }

        public Criteria andCompensateStatusIsNull() {
            addCriterion("compensate_status is null");
            return (Criteria) this;
        }

        public Criteria andCompensateStatusIsNotNull() {
            addCriterion("compensate_status is not null");
            return (Criteria) this;
        }

        public Criteria andCompensateStatusEqualTo(Byte value) {
            addCriterion("compensate_status =", value, "compensateStatus");
            return (Criteria) this;
        }

        public Criteria andCompensateStatusNotEqualTo(Byte value) {
            addCriterion("compensate_status <>", value, "compensateStatus");
            return (Criteria) this;
        }

        public Criteria andCompensateStatusGreaterThan(Byte value) {
            addCriterion("compensate_status >", value, "compensateStatus");
            return (Criteria) this;
        }

        public Criteria andCompensateStatusGreaterThanOrEqualTo(Byte value) {
            addCriterion("compensate_status >=", value, "compensateStatus");
            return (Criteria) this;
        }

        public Criteria andCompensateStatusLessThan(Byte value) {
            addCriterion("compensate_status <", value, "compensateStatus");
            return (Criteria) this;
        }

        public Criteria andCompensateStatusLessThanOrEqualTo(Byte value) {
            addCriterion("compensate_status <=", value, "compensateStatus");
            return (Criteria) this;
        }

        public Criteria andCompensateStatusIn(List<Byte> values) {
            addCriterion("compensate_status in", values, "compensateStatus");
            return (Criteria) this;
        }

        public Criteria andCompensateStatusNotIn(List<Byte> values) {
            addCriterion("compensate_status not in", values, "compensateStatus");
            return (Criteria) this;
        }

        public Criteria andCompensateStatusBetween(Byte value1, Byte value2) {
            addCriterion("compensate_status between", value1, value2, "compensateStatus");
            return (Criteria) this;
        }

        public Criteria andCompensateStatusNotBetween(Byte value1, Byte value2) {
            addCriterion("compensate_status not between", value1, value2, "compensateStatus");
            return (Criteria) this;
        }

        public Criteria andAlarmCountIsNull() {
            addCriterion("alarm_count is null");
            return (Criteria) this;
        }

        public Criteria andAlarmCountIsNotNull() {
            addCriterion("alarm_count is not null");
            return (Criteria) this;
        }

        public Criteria andAlarmCountEqualTo(Integer value) {
            addCriterion("alarm_count =", value, "alarmCount");
            return (Criteria) this;
        }

        public Criteria andAlarmCountNotEqualTo(Integer value) {
            addCriterion("alarm_count <>", value, "alarmCount");
            return (Criteria) this;
        }

        public Criteria andAlarmCountGreaterThan(Integer value) {
            addCriterion("alarm_count >", value, "alarmCount");
            return (Criteria) this;
        }

        public Criteria andAlarmCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("alarm_count >=", value, "alarmCount");
            return (Criteria) this;
        }

        public Criteria andAlarmCountLessThan(Integer value) {
            addCriterion("alarm_count <", value, "alarmCount");
            return (Criteria) this;
        }

        public Criteria andAlarmCountLessThanOrEqualTo(Integer value) {
            addCriterion("alarm_count <=", value, "alarmCount");
            return (Criteria) this;
        }

        public Criteria andAlarmCountIn(List<Integer> values) {
            addCriterion("alarm_count in", values, "alarmCount");
            return (Criteria) this;
        }

        public Criteria andAlarmCountNotIn(List<Integer> values) {
            addCriterion("alarm_count not in", values, "alarmCount");
            return (Criteria) this;
        }

        public Criteria andAlarmCountBetween(Integer value1, Integer value2) {
            addCriterion("alarm_count between", value1, value2, "alarmCount");
            return (Criteria) this;
        }

        public Criteria andAlarmCountNotBetween(Integer value1, Integer value2) {
            addCriterion("alarm_count not between", value1, value2, "alarmCount");
            return (Criteria) this;
        }

        public Criteria andHasArgsExtIsNull() {
            addCriterion("has_args_ext is null");
            return (Criteria) this;
        }

        public Criteria andHasArgsExtIsNotNull() {
            addCriterion("has_args_ext is not null");
            return (Criteria) this;
        }

        public Criteria andHasArgsExtEqualTo(Boolean value) {
            addCriterion("has_args_ext =", value, "hasArgsExt");
            return (Criteria) this;
        }

        public Criteria andHasArgsExtNotEqualTo(Boolean value) {
            addCriterion("has_args_ext <>", value, "hasArgsExt");
            return (Criteria) this;
        }

        public Criteria andHasArgsExtGreaterThan(Boolean value) {
            addCriterion("has_args_ext >", value, "hasArgsExt");
            return (Criteria) this;
        }

        public Criteria andHasArgsExtGreaterThanOrEqualTo(Boolean value) {
            addCriterion("has_args_ext >=", value, "hasArgsExt");
            return (Criteria) this;
        }

        public Criteria andHasArgsExtLessThan(Boolean value) {
            addCriterion("has_args_ext <", value, "hasArgsExt");
            return (Criteria) this;
        }

        public Criteria andHasArgsExtLessThanOrEqualTo(Boolean value) {
            addCriterion("has_args_ext <=", value, "hasArgsExt");
            return (Criteria) this;
        }

        public Criteria andHasArgsExtIn(List<Boolean> values) {
            addCriterion("has_args_ext in", values, "hasArgsExt");
            return (Criteria) this;
        }

        public Criteria andHasArgsExtNotIn(List<Boolean> values) {
            addCriterion("has_args_ext not in", values, "hasArgsExt");
            return (Criteria) this;
        }

        public Criteria andHasArgsExtBetween(Boolean value1, Boolean value2) {
            addCriterion("has_args_ext between", value1, value2, "hasArgsExt");
            return (Criteria) this;
        }

        public Criteria andHasArgsExtNotBetween(Boolean value1, Boolean value2) {
            addCriterion("has_args_ext not between", value1, value2, "hasArgsExt");
            return (Criteria) this;
        }

        public Criteria andIsDeleteIsNull() {
            addCriterion("is_delete is null");
            return (Criteria) this;
        }

        public Criteria andIsDeleteIsNotNull() {
            addCriterion("is_delete is not null");
            return (Criteria) this;
        }

        public Criteria andIsDeleteEqualTo(Boolean value) {
            addCriterion("is_delete =", value, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteNotEqualTo(Boolean value) {
            addCriterion("is_delete <>", value, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteGreaterThan(Boolean value) {
            addCriterion("is_delete >", value, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteGreaterThanOrEqualTo(Boolean value) {
            addCriterion("is_delete >=", value, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteLessThan(Boolean value) {
            addCriterion("is_delete <", value, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteLessThanOrEqualTo(Boolean value) {
            addCriterion("is_delete <=", value, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteIn(List<Boolean> values) {
            addCriterion("is_delete in", values, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteNotIn(List<Boolean> values) {
            addCriterion("is_delete not in", values, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteBetween(Boolean value1, Boolean value2) {
            addCriterion("is_delete between", value1, value2, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteNotBetween(Boolean value1, Boolean value2) {
            addCriterion("is_delete not between", value1, value2, "isDelete");
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
        private CompensateExample example;

        protected Criteria(CompensateExample example) {
            super();
            this.example = example;
        }

        public CompensateExample example() {
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
        void example(com.weweibuy.framework.compensate.mybatis.po.CompensateExample example);
    }
}