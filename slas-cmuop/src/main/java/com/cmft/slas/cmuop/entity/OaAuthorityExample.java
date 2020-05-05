package com.cmft.slas.cmuop.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OaAuthorityExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    private Integer limit;

    private Integer offset;

    public OaAuthorityExample() {
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

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getOffset() {
        return offset;
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

        public Criteria andOaAuthorityIdIsNull() {
            addCriterion("oa_authority_id is null");
            return (Criteria)this;
        }

        public Criteria andOaAuthorityIdIsNotNull() {
            addCriterion("oa_authority_id is not null");
            return (Criteria)this;
        }

        public Criteria andOaAuthorityIdEqualTo(Long value) {
            addCriterion("oa_authority_id =", value, "oaAuthorityId");
            return (Criteria)this;
        }

        public Criteria andOaAuthorityIdNotEqualTo(Long value) {
            addCriterion("oa_authority_id <>", value, "oaAuthorityId");
            return (Criteria)this;
        }

        public Criteria andOaAuthorityIdGreaterThan(Long value) {
            addCriterion("oa_authority_id >", value, "oaAuthorityId");
            return (Criteria)this;
        }

        public Criteria andOaAuthorityIdGreaterThanOrEqualTo(Long value) {
            addCriterion("oa_authority_id >=", value, "oaAuthorityId");
            return (Criteria)this;
        }

        public Criteria andOaAuthorityIdLessThan(Long value) {
            addCriterion("oa_authority_id <", value, "oaAuthorityId");
            return (Criteria)this;
        }

        public Criteria andOaAuthorityIdLessThanOrEqualTo(Long value) {
            addCriterion("oa_authority_id <=", value, "oaAuthorityId");
            return (Criteria)this;
        }

        public Criteria andOaAuthorityIdIn(List<Long> values) {
            addCriterion("oa_authority_id in", values, "oaAuthorityId");
            return (Criteria)this;
        }

        public Criteria andOaAuthorityIdNotIn(List<Long> values) {
            addCriterion("oa_authority_id not in", values, "oaAuthorityId");
            return (Criteria)this;
        }

        public Criteria andOaAuthorityIdBetween(Long value1, Long value2) {
            addCriterion("oa_authority_id between", value1, value2, "oaAuthorityId");
            return (Criteria)this;
        }

        public Criteria andOaAuthorityIdNotBetween(Long value1, Long value2) {
            addCriterion("oa_authority_id not between", value1, value2, "oaAuthorityId");
            return (Criteria)this;
        }

        public Criteria andUidIsNull() {
            addCriterion("uid is null");
            return (Criteria)this;
        }

        public Criteria andUidIsNotNull() {
            addCriterion("uid is not null");
            return (Criteria)this;
        }

        public Criteria andUidEqualTo(String value) {
            addCriterion("uid =", value, "uid");
            return (Criteria)this;
        }

        public Criteria andUidNotEqualTo(String value) {
            addCriterion("uid <>", value, "uid");
            return (Criteria)this;
        }

        public Criteria andUidGreaterThan(String value) {
            addCriterion("uid >", value, "uid");
            return (Criteria)this;
        }

        public Criteria andUidGreaterThanOrEqualTo(String value) {
            addCriterion("uid >=", value, "uid");
            return (Criteria)this;
        }

        public Criteria andUidLessThan(String value) {
            addCriterion("uid <", value, "uid");
            return (Criteria)this;
        }

        public Criteria andUidLessThanOrEqualTo(String value) {
            addCriterion("uid <=", value, "uid");
            return (Criteria)this;
        }

        public Criteria andUidIn(List<String> values) {
            addCriterion("uid in", values, "uid");
            return (Criteria)this;
        }

        public Criteria andUidNotIn(List<String> values) {
            addCriterion("uid not in", values, "uid");
            return (Criteria)this;
        }

        public Criteria andUidBetween(String value1, String value2) {
            addCriterion("uid between", value1, value2, "uid");
            return (Criteria)this;
        }

        public Criteria andUidNotBetween(String value1, String value2) {
            addCriterion("uid not between", value1, value2, "uid");
            return (Criteria)this;
        }

        public Criteria andPsIdIsNull() {
            addCriterion("ps_id is null");
            return (Criteria)this;
        }

        public Criteria andPsIdIsNotNull() {
            addCriterion("ps_id is not null");
            return (Criteria)this;
        }

        public Criteria andPsIdEqualTo(String value) {
            addCriterion("ps_id =", value, "psId");
            return (Criteria)this;
        }

        public Criteria andPsIdNotEqualTo(String value) {
            addCriterion("ps_id <>", value, "psId");
            return (Criteria)this;
        }

        public Criteria andPsIdGreaterThan(String value) {
            addCriterion("ps_id >", value, "psId");
            return (Criteria)this;
        }

        public Criteria andPsIdGreaterThanOrEqualTo(String value) {
            addCriterion("ps_id >=", value, "psId");
            return (Criteria)this;
        }

        public Criteria andPsIdLessThan(String value) {
            addCriterion("ps_id <", value, "psId");
            return (Criteria)this;
        }

        public Criteria andPsIdLessThanOrEqualTo(String value) {
            addCriterion("ps_id <=", value, "psId");
            return (Criteria)this;
        }

        public Criteria andPsIdIn(List<String> values) {
            addCriterion("ps_id in", values, "psId");
            return (Criteria)this;
        }

        public Criteria andPsIdNotIn(List<String> values) {
            addCriterion("ps_id not in", values, "psId");
            return (Criteria)this;
        }

        public Criteria andPsIdBetween(String value1, String value2) {
            addCriterion("ps_id between", value1, value2, "psId");
            return (Criteria)this;
        }

        public Criteria andPsIdNotBetween(String value1, String value2) {
            addCriterion("ps_id not between", value1, value2, "psId");
            return (Criteria)this;
        }

        public Criteria andOaIdIsNull() {
            addCriterion("oa_id is null");
            return (Criteria)this;
        }

        public Criteria andOaIdIsNotNull() {
            addCriterion("oa_id is not null");
            return (Criteria)this;
        }

        public Criteria andOaIdEqualTo(String value) {
            addCriterion("oa_id =", value, "oaId");
            return (Criteria)this;
        }

        public Criteria andOaIdNotEqualTo(String value) {
            addCriterion("oa_id <>", value, "oaId");
            return (Criteria)this;
        }

        public Criteria andOaIdGreaterThan(String value) {
            addCriterion("oa_id >", value, "oaId");
            return (Criteria)this;
        }

        public Criteria andOaIdGreaterThanOrEqualTo(String value) {
            addCriterion("oa_id >=", value, "oaId");
            return (Criteria)this;
        }

        public Criteria andOaIdLessThan(String value) {
            addCriterion("oa_id <", value, "oaId");
            return (Criteria)this;
        }

        public Criteria andOaIdLessThanOrEqualTo(String value) {
            addCriterion("oa_id <=", value, "oaId");
            return (Criteria)this;
        }

        public Criteria andOaIdIn(List<String> values) {
            addCriterion("oa_id in", values, "oaId");
            return (Criteria)this;
        }

        public Criteria andOaIdNotIn(List<String> values) {
            addCriterion("oa_id not in", values, "oaId");
            return (Criteria)this;
        }

        public Criteria andOaIdBetween(String value1, String value2) {
            addCriterion("oa_id between", value1, value2, "oaId");
            return (Criteria)this;
        }

        public Criteria andOaIdNotBetween(String value1, String value2) {
            addCriterion("oa_id not between", value1, value2, "oaId");
            return (Criteria)this;
        }

        public Criteria andAuthAreaIdIsNull() {
            addCriterion("auth_area_id is null");
            return (Criteria)this;
        }

        public Criteria andAuthAreaIdIsNotNull() {
            addCriterion("auth_area_id is not null");
            return (Criteria)this;
        }

        public Criteria andAuthAreaIdEqualTo(String value) {
            addCriterion("auth_area_id =", value, "authAreaId");
            return (Criteria)this;
        }

        public Criteria andAuthAreaIdNotEqualTo(String value) {
            addCriterion("auth_area_id <>", value, "authAreaId");
            return (Criteria)this;
        }

        public Criteria andAuthAreaIdGreaterThan(String value) {
            addCriterion("auth_area_id >", value, "authAreaId");
            return (Criteria)this;
        }

        public Criteria andAuthAreaIdGreaterThanOrEqualTo(String value) {
            addCriterion("auth_area_id >=", value, "authAreaId");
            return (Criteria)this;
        }

        public Criteria andAuthAreaIdLessThan(String value) {
            addCriterion("auth_area_id <", value, "authAreaId");
            return (Criteria)this;
        }

        public Criteria andAuthAreaIdLessThanOrEqualTo(String value) {
            addCriterion("auth_area_id <=", value, "authAreaId");
            return (Criteria)this;
        }

        public Criteria andAuthAreaIdIn(List<String> values) {
            addCriterion("auth_area_id in", values, "authAreaId");
            return (Criteria)this;
        }

        public Criteria andAuthAreaIdNotIn(List<String> values) {
            addCriterion("auth_area_id not in", values, "authAreaId");
            return (Criteria)this;
        }

        public Criteria andAuthAreaIdBetween(String value1, String value2) {
            addCriterion("auth_area_id between", value1, value2, "authAreaId");
            return (Criteria)this;
        }

        public Criteria andAuthAreaIdNotBetween(String value1, String value2) {
            addCriterion("auth_area_id not between", value1, value2, "authAreaId");
            return (Criteria)this;
        }

        public Criteria andAuthReaderIsNull() {
            addCriterion("auth_reader is null");
            return (Criteria)this;
        }

        public Criteria andAuthReaderIsNotNull() {
            addCriterion("auth_reader is not null");
            return (Criteria)this;
        }

        public Criteria andAuthReaderEqualTo(String value) {
            addCriterion("auth_reader =", value, "authReader");
            return (Criteria)this;
        }

        public Criteria andAuthReaderNotEqualTo(String value) {
            addCriterion("auth_reader <>", value, "authReader");
            return (Criteria)this;
        }

        public Criteria andAuthReaderGreaterThan(String value) {
            addCriterion("auth_reader >", value, "authReader");
            return (Criteria)this;
        }

        public Criteria andAuthReaderGreaterThanOrEqualTo(String value) {
            addCriterion("auth_reader >=", value, "authReader");
            return (Criteria)this;
        }

        public Criteria andAuthReaderLessThan(String value) {
            addCriterion("auth_reader <", value, "authReader");
            return (Criteria)this;
        }

        public Criteria andAuthReaderLessThanOrEqualTo(String value) {
            addCriterion("auth_reader <=", value, "authReader");
            return (Criteria)this;
        }

        public Criteria andAuthReaderIn(List<String> values) {
            addCriterion("auth_reader in", values, "authReader");
            return (Criteria)this;
        }

        public Criteria andAuthReaderNotIn(List<String> values) {
            addCriterion("auth_reader not in", values, "authReader");
            return (Criteria)this;
        }

        public Criteria andAuthReaderBetween(String value1, String value2) {
            addCriterion("auth_reader between", value1, value2, "authReader");
            return (Criteria)this;
        }

        public Criteria andAuthReaderNotBetween(String value1, String value2) {
            addCriterion("auth_reader not between", value1, value2, "authReader");
            return (Criteria)this;
        }

        public Criteria andCreateOperatorIsNull() {
            addCriterion("create_operator is null");
            return (Criteria)this;
        }

        public Criteria andCreateOperatorIsNotNull() {
            addCriterion("create_operator is not null");
            return (Criteria)this;
        }

        public Criteria andCreateOperatorEqualTo(String value) {
            addCriterion("create_operator =", value, "createOperator");
            return (Criteria)this;
        }

        public Criteria andCreateOperatorNotEqualTo(String value) {
            addCriterion("create_operator <>", value, "createOperator");
            return (Criteria)this;
        }

        public Criteria andCreateOperatorGreaterThan(String value) {
            addCriterion("create_operator >", value, "createOperator");
            return (Criteria)this;
        }

        public Criteria andCreateOperatorGreaterThanOrEqualTo(String value) {
            addCriterion("create_operator >=", value, "createOperator");
            return (Criteria)this;
        }

        public Criteria andCreateOperatorLessThan(String value) {
            addCriterion("create_operator <", value, "createOperator");
            return (Criteria)this;
        }

        public Criteria andCreateOperatorLessThanOrEqualTo(String value) {
            addCriterion("create_operator <=", value, "createOperator");
            return (Criteria)this;
        }

        public Criteria andCreateOperatorIn(List<String> values) {
            addCriterion("create_operator in", values, "createOperator");
            return (Criteria)this;
        }

        public Criteria andCreateOperatorNotIn(List<String> values) {
            addCriterion("create_operator not in", values, "createOperator");
            return (Criteria)this;
        }

        public Criteria andCreateOperatorBetween(String value1, String value2) {
            addCriterion("create_operator between", value1, value2, "createOperator");
            return (Criteria)this;
        }

        public Criteria andCreateOperatorNotBetween(String value1, String value2) {
            addCriterion("create_operator not between", value1, value2, "createOperator");
            return (Criteria)this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria)this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria)this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria)this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria)this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria)this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria)this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria)this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria)this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria)this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria)this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria)this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
            return (Criteria)this;
        }

        public Criteria andUpdateOperatorIsNull() {
            addCriterion("update_operator is null");
            return (Criteria)this;
        }

        public Criteria andUpdateOperatorIsNotNull() {
            addCriterion("update_operator is not null");
            return (Criteria)this;
        }

        public Criteria andUpdateOperatorEqualTo(String value) {
            addCriterion("update_operator =", value, "updateOperator");
            return (Criteria)this;
        }

        public Criteria andUpdateOperatorNotEqualTo(String value) {
            addCriterion("update_operator <>", value, "updateOperator");
            return (Criteria)this;
        }

        public Criteria andUpdateOperatorGreaterThan(String value) {
            addCriterion("update_operator >", value, "updateOperator");
            return (Criteria)this;
        }

        public Criteria andUpdateOperatorGreaterThanOrEqualTo(String value) {
            addCriterion("update_operator >=", value, "updateOperator");
            return (Criteria)this;
        }

        public Criteria andUpdateOperatorLessThan(String value) {
            addCriterion("update_operator <", value, "updateOperator");
            return (Criteria)this;
        }

        public Criteria andUpdateOperatorLessThanOrEqualTo(String value) {
            addCriterion("update_operator <=", value, "updateOperator");
            return (Criteria)this;
        }

        public Criteria andUpdateOperatorIn(List<String> values) {
            addCriterion("update_operator in", values, "updateOperator");
            return (Criteria)this;
        }

        public Criteria andUpdateOperatorNotIn(List<String> values) {
            addCriterion("update_operator not in", values, "updateOperator");
            return (Criteria)this;
        }

        public Criteria andUpdateOperatorBetween(String value1, String value2) {
            addCriterion("update_operator between", value1, value2, "updateOperator");
            return (Criteria)this;
        }

        public Criteria andUpdateOperatorNotBetween(String value1, String value2) {
            addCriterion("update_operator not between", value1, value2, "updateOperator");
            return (Criteria)this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("update_time is null");
            return (Criteria)this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("update_time is not null");
            return (Criteria)this;
        }

        public Criteria andUpdateTimeEqualTo(Date value) {
            addCriterion("update_time =", value, "updateTime");
            return (Criteria)this;
        }

        public Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterion("update_time <>", value, "updateTime");
            return (Criteria)this;
        }

        public Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterion("update_time >", value, "updateTime");
            return (Criteria)this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("update_time >=", value, "updateTime");
            return (Criteria)this;
        }

        public Criteria andUpdateTimeLessThan(Date value) {
            addCriterion("update_time <", value, "updateTime");
            return (Criteria)this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("update_time <=", value, "updateTime");
            return (Criteria)this;
        }

        public Criteria andUpdateTimeIn(List<Date> values) {
            addCriterion("update_time in", values, "updateTime");
            return (Criteria)this;
        }

        public Criteria andUpdateTimeNotIn(List<Date> values) {
            addCriterion("update_time not in", values, "updateTime");
            return (Criteria)this;
        }

        public Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("update_time between", value1, value2, "updateTime");
            return (Criteria)this;
        }

        public Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("update_time not between", value1, value2, "updateTime");
            return (Criteria)this;
        }

        public Criteria andIsDeleteIsNull() {
            addCriterion("is_delete is null");
            return (Criteria)this;
        }

        public Criteria andIsDeleteIsNotNull() {
            addCriterion("is_delete is not null");
            return (Criteria)this;
        }

        public Criteria andIsDeleteEqualTo(Byte value) {
            addCriterion("is_delete =", value, "isDelete");
            return (Criteria)this;
        }

        public Criteria andIsDeleteNotEqualTo(Byte value) {
            addCriterion("is_delete <>", value, "isDelete");
            return (Criteria)this;
        }

        public Criteria andIsDeleteGreaterThan(Byte value) {
            addCriterion("is_delete >", value, "isDelete");
            return (Criteria)this;
        }

        public Criteria andIsDeleteGreaterThanOrEqualTo(Byte value) {
            addCriterion("is_delete >=", value, "isDelete");
            return (Criteria)this;
        }

        public Criteria andIsDeleteLessThan(Byte value) {
            addCriterion("is_delete <", value, "isDelete");
            return (Criteria)this;
        }

        public Criteria andIsDeleteLessThanOrEqualTo(Byte value) {
            addCriterion("is_delete <=", value, "isDelete");
            return (Criteria)this;
        }

        public Criteria andIsDeleteIn(List<Byte> values) {
            addCriterion("is_delete in", values, "isDelete");
            return (Criteria)this;
        }

        public Criteria andIsDeleteNotIn(List<Byte> values) {
            addCriterion("is_delete not in", values, "isDelete");
            return (Criteria)this;
        }

        public Criteria andIsDeleteBetween(Byte value1, Byte value2) {
            addCriterion("is_delete between", value1, value2, "isDelete");
            return (Criteria)this;
        }

        public Criteria andIsDeleteNotBetween(Byte value1, Byte value2) {
            addCriterion("is_delete not between", value1, value2, "isDelete");
            return (Criteria)this;
        }

    }

    /**
     */
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
