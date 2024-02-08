package org.cthul.qxadmin.data;

import java.util.*;

public interface QxType<T> {

    List<QxProperty<T, ?>> getProperties();

    QxProperty<T, ?> getId();

    default void update(T entity, Map<String, String> body) {
        for (var property : getProperties()) {
            var value = body.get(property.getLabel());
            if (value != null) {
                property.set(entity, value);
            }
        }
    }

    default T getNew() {
        throw new UnsupportedOperationException();
    }

    default T getNew(Map<String, String> body) {
        var entity = getNew();
        update(entity, body);
        return entity;
    }
}
