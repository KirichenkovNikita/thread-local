package ru.company;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class CustomThreadLocal<T> {
    private static CustomThreadLocalMap variablesMap;

    @SuppressWarnings("unchecked")
    public T get() {
        if (variablesMap == null) {
            createMap();
            return null;
        }

        long threadId = Thread.currentThread().getId();
        Optional<CustomThreadLocalMap.Variable> variable = variablesMap.get(threadId);
        return variable.map(value -> (T) value.value).orElse(null);
    }

    public void set(T value) {
        if (variablesMap == null) {
            createMap();
        }

        long threadId = Thread.currentThread().getId();
        variablesMap.set(threadId, value);
    }

    public void remove() {
        long threadId = Thread.currentThread().getId();
        variablesMap.remove(threadId);
    }

    public Collection<CustomThreadLocalMap.Variable> getVariables() {
        return CustomThreadLocalMap.VARIABLES;
    }

    public void cleanMap() {
        if (variablesMap!= null) {
            variablesMap.removeAllValue();
        }
    }

    private void createMap() {
        variablesMap = new CustomThreadLocalMap();
    }

    private static class CustomThreadLocalMap{
        private static final Collection<Variable> VARIABLES = Collections.synchronizedCollection(new ArrayList<>());
        private static final int THREAD_COUNT_FOR_REFRESH = 16;

        private void set(Long threadId, Object value) {
            Variable variable = new Variable(threadId, value);
            if (VARIABLES.contains(variable)) {
                VARIABLES.remove(variable);
                VARIABLES.add(variable);
            } else {
                VARIABLES.add(variable);
            }

            if (VARIABLES.size() >= THREAD_COUNT_FOR_REFRESH) {
                refreshCollection();
            }
        }

        private Optional<Variable> get(Long threadId) {
            return VARIABLES.stream().filter(v -> Objects.equals(v.threadId, threadId)).findFirst();
        }

        private void remove(long threadId) {
            Variable variable = new Variable(threadId, null);
            VARIABLES.remove(variable);
        }

        private void refreshCollection() {
            Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
            Set<Long> threadIdSet = new HashSet<>();
            threadSet.forEach(t -> threadIdSet.add(t.getId()));
            VARIABLES.stream().filter(v -> threadIdSet.contains(v.threadId))
                    .forEach(v -> remove(v.threadId));
        }

        public void removeAllValue() {
            VARIABLES.clear();
        }

        private static class Variable {
            Long threadId;
            Object value;

            private Variable(Long threadId, Object value) {
                this.threadId = threadId;
                this.value = value;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Variable variable = (Variable) o;
                return Objects.equals(threadId, variable.threadId);
            }

            @Override
            public int hashCode() {
                return Objects.hash(threadId);
            }
        }
    }
}
