package com.weweibuy.framework.compensate.mybatis.po;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CompensateMethodArgsExtExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public CompensateMethodArgsExtExample() {
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

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
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

        public Criteria andArgsOrderIsNull() {
            addCriterion("args_order is null");
            return (Criteria) this;
        }

        public Criteria andArgsOrderIsNotNull() {
            addCriterion("args_order is not null");
            return (Criteria) this;
        }

        public Criteria andArgsOrderEqualTo(Integer value) {
            addCriterion("args_order =", value, "argsOrder");
            return (Criteria) this;
        }

        public Criteria andArgsOrderNotEqualTo(Integer value) {
            addCriterion("args_order <>", value, "argsOrder");
            return (Criteria) this;
        }

        public Criteria andArgsOrderGreaterThan(Integer value) {
            addCriterion("args_order >", value, "argsOrder");
            return (Criteria) this;
        }

        public Criteria andArgsOrderGreaterThanOrEqualTo(Integer value) {
            addCriterion("args_order >=", value, "argsOrder");
            return (Criteria) this;
        }

        public Criteria andArgsOrderLessThan(Integer value) {
            addCriterion("args_order <", value, "argsOrder");
            return (Criteria) this;
        }

        public Criteria andArgsOrderLessThanOrEqualTo(Integer value) {
            addCriterion("args_order <=", value, "argsOrder");
            return (Criteria) this;
        }

        public Criteria andArgsOrderIn(List<Integer> values) {
            addCriterion("args_order in", values, "argsOrder");
            return (Criteria) this;
        }

        public Criteria andArgsOrderNotIn(List<Integer> values) {
            addCriterion("args_order not in", values, "argsOrder");
            return (Criteria) this;
        }

        public Criteria andArgsOrderBetween(Integer value1, Integer value2) {
            addCriterion("args_order between", value1, value2, "argsOrder");
            return (Criteria) this;
        }

        public Criteria andArgsOrderNotBetween(Integer value1, Integer value2) {
            addCriterion("args_order not between", value1, value2, "argsOrder");
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

        protected Criteria() {
            super();
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
}