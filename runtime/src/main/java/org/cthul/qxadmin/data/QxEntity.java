package org.cthul.qxadmin.data;

import java.util.List;
import java.util.stream.Collectors;

public class QxEntity<T> {

    private final T entity;
    private final QxType<T> type;

    public QxEntity(T entity, QxType<T> type) {
        this.entity = entity;
        this.type = type;
    }

    public Object getId() {
        return type.getId().getRaw(entity);
    }

    public List<QxPropertyValue<T>> getProperties() {
        return type.getProperties().stream()
                .map(p -> new QxPropertyValue<>(entity, p))
                .collect(Collectors.toList());
    }
}
