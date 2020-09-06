package com.weweibuy.framework.compensate.mybatis.po;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CompensateLogExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public CompensateLogExample() {
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

    public CompensateLogExample orderBy(String orderByClause) {
        this.setOrderByClause(orderByClause);
        return this;
    }

    public CompensateLogExample orderBy(String ... orderByClauses) {
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
        CompensateLogExample example = new CompensateLogExample();
        return example.createCriteria();
    }

    public CompensateLogExample when(boolean condition, IExampleWhen then) {
        if (condition) {
            then.example(this);
        }
        return this;
    }

    public CompensateLogExample when(boolean condition, IExampleWhen then, IExampleWhen otherwise) {
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

        public Criteria andCompensateIdIsNull() {
            addCriterion("compensate_id is null");
            return (Criteria) this;
        }

        public Criteria andCompensateIdIsNotNull() {
            addCriterion("compensate_id is not null");
            return (Criteria) this;
        }

        public Criteria andCompensateIdEqualTo(Long value) {
            addCriterion("compensate_id =", value, "compensateId");
            return (Criteria) this;
        }

        public Criteria andCompensateIdNotEqualTo(Long value) {
            addCriterion("compensate_id <>", value, "compensateId");
            return (Criteria) this;
        }

        public Criteria andCompensateIdGreaterThan(Long value) {
            addCriterion("compensate_id >", value, "compensateId");
            return (Criteria) this;
        }

        public Criteria andCompensateIdGreaterThanOrEqualTo(Long value) {
            addCriterion("compensate_id >=", value, "compensateId");
            return (Criteria) this;
        }

        public Criteria andCompensateIdLessThan(Long value) {
            addCriterion("compensate_id <", value, "compensateId");
            return (Criteria) this;
        }

        public Criteria andCompensateIdLessThanOrEqualTo(Long value) {
            addCriterion("compensate_id <=", value, "compensateId");
            return (Criteria) this;
        }

        public Criteria andCompensateIdIn(List<Long> values) {
            addCriterion("compensate_id in", values, "compensateId");
            return (Criteria) this;
        }

        public Criteria andCompensateIdNotIn(List<Long> values) {
            addCriterion("compensate_id not in", values, "compensateId");
            return (Criteria) this;
        }

        public Criteria andCompensateIdBetween(Long value1, Long value2) {
            addCriterion("compensate_id between", value1, value2, "compensateId");
            return (Criteria) this;
        }

        public Criteria andCompensateIdNotBetween(Long value1, Long value2) {
            addCriterion("compensate_id not between", value1, value2, "compensateId");
            return (Criteria) this;
        }

        public Criteria andTriggerTypeIsNull() {
            addCriterion("trigger_type is null");
            return (Criteria) this;
        }

        public Criteria andTriggerTypeIsNotNull() {
            addCriterion("trigger_type is not null");
            return (Criteria) this;
        }

        public Criteria andTriggerTypeEqualTo(String value) {
            addCriterion("trigger_type =", value, "triggerType");
            return (Criteria) this;
        }

        public Criteria andTriggerTypeNotEqualTo(String value) {
            addCriterion("trigger_type <>", value, "triggerType");
            return (Criteria) this;
        }

        public Criteria andTriggerTypeGreaterThan(String value) {
            addCriterion("trigger_type >", value, "triggerType");
            return (Criteria) this;
        }

        public Criteria andTriggerTypeGreaterThanOrEqualTo(String value) {
            addCriterion("trigger_type >=", value, "triggerType");
            return (Criteria) this;
        }

        public Criteria andTriggerTypeLessThan(String value) {
            addCriterion("trigger_type <", value, "triggerType");
            return (Criteria) this;
        }

        public Criteria andTriggerTypeLessThanOrEqualTo(String value) {
            addCriterion("trigger_type <=", value, "triggerType");
            return (Criteria) this;
        }

        public Criteria andTriggerTypeLike(String value) {
            addCriterion("trigger_type like", value, "triggerType");
            return (Criteria) this;
        }

        public Criteria andTriggerTypeNotLike(String value) {
            addCriterion("trigger_type not like", value, "triggerType");
            return (Criteria) this;
        }

        public Criteria andTriggerTypeIn(List<String> values) {
            addCriterion("trigger_type in", values, "triggerType");
            return (Criteria) this;
        }

        public Criteria andTriggerTypeNotIn(List<String> values) {
            addCriterion("trigger_type not in", values, "triggerType");
            return (Criteria) this;
        }

        public Criteria andTriggerTypeBetween(String value1, String value2) {
            addCriterion("trigger_type between", value1, value2, "triggerType");
            return (Criteria) this;
        }

        public Criteria andTriggerTypeNotBetween(String value1, String value2) {
            addCriterion("trigger_type not between", value1, value2, "triggerType");
            return (Criteria) this;
        }

        public Criteria andCompensateStateIsNull() {
            addCriterion("compensate_state is null");
            return (Criteria) this;
        }

        public Criteria andCompensateStateIsNotNull() {
            addCriterion("compensate_state is not null");
            return (Criteria) this;
        }

        public Criteria andCompensateStateEqualTo(String value) {
            addCriterion("compensate_state =", value, "compensateState");
            return (Criteria) this;
        }

        public Criteria andCompensateStateNotEqualTo(String value) {
            addCriterion("compensate_state <>", value, "compensateState");
            return (Criteria) this;
        }

        public Criteria andCompensateStateGreaterThan(String value) {
            addCriterion("compensate_state >", value, "compensateState");
            return (Criteria) this;
        }

        public Criteria andCompensateStateGreaterThanOrEqualTo(String value) {
            addCriterion("compensate_state >=", value, "compensateState");
            return (Criteria) this;
        }

        public Criteria andCompensateStateLessThan(String value) {
            addCriterion("compensate_state <", value, "compensateState");
            return (Criteria) this;
        }

        public Criteria andCompensateStateLessThanOrEqualTo(String value) {
            addCriterion("compensate_state <=", value, "compensateState");
            return (Criteria) this;
        }

        public Criteria andCompensateStateLike(String value) {
            addCriterion("compensate_state like", value, "compensateState");
            return (Criteria) this;
        }

        public Criteria andCompensateStateNotLike(String value) {
            addCriterion("compensate_state not like", value, "compensateState");
            return (Criteria) this;
        }

        public Criteria andCompensateStateIn(List<String> values) {
            addCriterion("compensate_state in", values, "compensateState");
            return (Criteria) this;
        }

        public Criteria andCompensateStateNotIn(List<String> values) {
            addCriterion("compensate_state not in", values, "compensateState");
            return (Criteria) this;
        }

        public Criteria andCompensateStateBetween(String value1, String value2) {
            addCriterion("compensate_state between", value1, value2, "compensateState");
            return (Criteria) this;
        }

        public Criteria andCompensateStateNotBetween(String value1, String value2) {
            addCriterion("compensate_state not between", value1, value2, "compensateState");
            return (Criteria) this;
        }

        public Criteria andCompensateResultIsNull() {
            addCriterion("compensate_result is null");
            return (Criteria) this;
        }

        public Criteria andCompensateResultIsNotNull() {
            addCriterion("compensate_result is not null");
            return (Criteria) this;
        }

        public Criteria andCompensateResultEqualTo(String value) {
            addCriterion("compensate_result =", value, "compensateResult");
            return (Criteria) this;
        }

        public Criteria andCompensateResultNotEqualTo(String value) {
            addCriterion("compensate_result <>", value, "compensateResult");
            return (Criteria) this;
        }

        public Criteria andCompensateResultGreaterThan(String value) {
            addCriterion("compensate_result >", value, "compensateResult");
            return (Criteria) this;
        }

        public Criteria andCompensateResultGreaterThanOrEqualTo(String value) {
            addCriterion("compensate_result >=", value, "compensateResult");
            return (Criteria) this;
        }

        public Criteria andCompensateResultLessThan(String value) {
            addCriterion("compensate_result <", value, "compensateResult");
            return (Criteria) this;
        }

        public Criteria andCompensateResultLessThanOrEqualTo(String value) {
            addCriterion("compensate_result <=", value, "compensateResult");
            return (Criteria) this;
        }

        public Criteria andCompensateResultLike(String value) {
            addCriterion("compensate_result like", value, "compensateResult");
            return (Criteria) this;
        }

        public Criteria andCompensateResultNotLike(String value) {
            addCriterion("compensate_result not like", value, "compensateResult");
            return (Criteria) this;
        }

        public Criteria andCompensateResultIn(List<String> values) {
            addCriterion("compensate_result in", values, "compensateResult");
            return (Criteria) this;
        }

        public Criteria andCompensateResultNotIn(List<String> values) {
            addCriterion("compensate_result not in", values, "compensateResult");
            return (Criteria) this;
        }

        public Criteria andCompensateResultBetween(String value1, String value2) {
            addCriterion("compensate_result between", value1, value2, "compensateResult");
            return (Criteria) this;
        }

        public Criteria andCompensateResultNotBetween(String value1, String value2) {
            addCriterion("compensate_result not between", value1, value2, "compensateResult");
            return (Criteria) this;
        }

        public Criteria andExceptionInfoIsNull() {
            addCriterion("exception_info is null");
            return (Criteria) this;
        }

        public Criteria andExceptionInfoIsNotNull() {
            addCriterion("exception_info is not null");
            return (Criteria) this;
        }

        public Criteria andExceptionInfoEqualTo(String value) {
            addCriterion("exception_info =", value, "exceptionInfo");
            return (Criteria) this;
        }

        public Criteria andExceptionInfoNotEqualTo(String value) {
            addCriterion("exception_info <>", value, "exceptionInfo");
            return (Criteria) this;
        }

        public Criteria andExceptionInfoGreaterThan(String value) {
            addCriterion("exception_info >", value, "exceptionInfo");
            return (Criteria) this;
        }

        public Criteria andExceptionInfoGreaterThanOrEqualTo(String value) {
            addCriterion("exception_info >=", value, "exceptionInfo");
            return (Criteria) this;
        }

        public Criteria andExceptionInfoLessThan(String value) {
            addCriterion("exception_info <", value, "exceptionInfo");
            return (Criteria) this;
        }

        public Criteria andExceptionInfoLessThanOrEqualTo(String value) {
            addCriterion("exception_info <=", value, "exceptionInfo");
            return (Criteria) this;
        }

        public Criteria andExceptionInfoLike(String value) {
            addCriterion("exception_info like", value, "exceptionInfo");
            return (Criteria) this;
        }

        public Criteria andExceptionInfoNotLike(String value) {
            addCriterion("exception_info not like", value, "exceptionInfo");
            return (Criteria) this;
        }

        public Criteria andExceptionInfoIn(List<String> values) {
            addCriterion("exception_info in", values, "exceptionInfo");
            return (Criteria) this;
        }

        public Criteria andExceptionInfoNotIn(List<String> values) {
            addCriterion("exception_info not in", values, "exceptionInfo");
            return (Criteria) this;
        }

        public Criteria andExceptionInfoBetween(String value1, String value2) {
            addCriterion("exception_info between", value1, value2, "exceptionInfo");
            return (Criteria) this;
        }

        public Criteria andExceptionInfoNotBetween(String value1, String value2) {
            addCriterion("exception_info not between", value1, value2, "exceptionInfo");
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
        private CompensateLogExample example;

        protected Criteria(CompensateLogExample example) {
            super();
            this.example = example;
        }

        public CompensateLogExample example() {
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
        void example(com.weweibuy.framework.compensate.mybatis.po.CompensateLogExample example);
    }
}