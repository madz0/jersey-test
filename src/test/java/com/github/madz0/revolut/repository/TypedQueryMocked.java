package com.github.madz0.revolut.repository;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;

@Getter
@Setter
public class TypedQueryMocked<X> implements TypedQuery<X> {
    List<X> resultSet;
    int executedUpdateResult;
    int size = 0;
    int start = 0;

    @Override
    public List<X> getResultList() {
        if (size > 0) {
            int limit = start + size;
            if (limit > resultSet.size()) {
                limit = resultSet.size();
            }
            return resultSet.subList(start, limit);
        } else {
            return resultSet;
        }
    }

    @Override
    public X getSingleResult() {
        return resultSet.get(0);
    }

    @Override
    public int executeUpdate() {
        return executedUpdateResult;
    }

    @Override
    public TypedQuery<X> setMaxResults(int i) {
        size = i;
        return this;
    }

    @Override
    public int getMaxResults() {
        return size;
    }

    @Override
    public TypedQuery<X> setFirstResult(int i) {
        start = i;
        return this;
    }

    @Override
    public int getFirstResult() {
        return start;
    }

    @Override
    public TypedQuery<X> setHint(String s, Object o) {
        return null;
    }

    @Override
    public Map<String, Object> getHints() {
        return null;
    }

    @Override
    public <T> TypedQuery<X> setParameter(Parameter<T> parameter, T t) {
        return null;
    }

    @Override
    public TypedQuery<X> setParameter(Parameter<Calendar> parameter, Calendar calendar, TemporalType temporalType) {
        return null;
    }

    @Override
    public TypedQuery<X> setParameter(Parameter<Date> parameter, Date date, TemporalType temporalType) {
        return null;
    }

    @Override
    public TypedQuery<X> setParameter(String s, Object o) {
        return this;
    }

    @Override
    public TypedQuery<X> setParameter(String s, Calendar calendar, TemporalType temporalType) {
        return null;
    }

    @Override
    public TypedQuery<X> setParameter(String s, Date date, TemporalType temporalType) {
        return null;
    }

    @Override
    public TypedQuery<X> setParameter(int i, Object o) {
        return null;
    }

    @Override
    public TypedQuery<X> setParameter(int i, Calendar calendar, TemporalType temporalType) {
        return null;
    }

    @Override
    public TypedQuery<X> setParameter(int i, Date date, TemporalType temporalType) {
        return null;
    }

    @Override
    public Set<Parameter<?>> getParameters() {
        return null;
    }

    @Override
    public Parameter<?> getParameter(String s) {
        return null;
    }

    @Override
    public <T> Parameter<T> getParameter(String s, Class<T> aClass) {
        return null;
    }

    @Override
    public Parameter<?> getParameter(int i) {
        return null;
    }

    @Override
    public <T> Parameter<T> getParameter(int i, Class<T> aClass) {
        return null;
    }

    @Override
    public boolean isBound(Parameter<?> parameter) {
        return false;
    }

    @Override
    public <T> T getParameterValue(Parameter<T> parameter) {
        return null;
    }

    @Override
    public Object getParameterValue(String s) {
        return null;
    }

    @Override
    public Object getParameterValue(int i) {
        return null;
    }

    @Override
    public TypedQuery<X> setFlushMode(FlushModeType flushModeType) {
        return null;
    }

    @Override
    public FlushModeType getFlushMode() {
        return null;
    }

    @Override
    public TypedQuery<X> setLockMode(LockModeType lockModeType) {
        return null;
    }

    @Override
    public LockModeType getLockMode() {
        return null;
    }

    @Override
    public <T> T unwrap(Class<T> aClass) {
        return null;
    }
}
