package org.cthul.qxadmin.data;

import java.util.Map;
import java.util.stream.Stream;

public interface QxRepository<T> {

    Stream<T> getAll();

    T get(String id);

    default void delete(String id) {
        throw new UnsupportedOperationException();
    }

    default void persist(T entity) {
        throw new UnsupportedOperationException();
    }
}
