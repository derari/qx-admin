package org.cthul.qxadmin.data;

import jakarta.ws.rs.core.MultivaluedMap;

import java.util.List;
import java.util.stream.Collectors;

public class QxTable<T> {

    private final QxRepository<T> repository;
    private final QxType<T> type;
    private final MultivaluedMap<String, String> query;

    public QxTable(QxRepository<T> repository, QxType<T> type, MultivaluedMap<String, String> query) {
        this.repository = repository;
        this.type = type;
        this.query = query;
    }

    public List<String> getColumnLabels() {
        return type.getProperties().stream()
                .map(QxProperty::getLabel)
                .collect(Collectors.toList());
    }

    public List<QxEntity<T>> getRows() {
        return repository.getAll()
                .map(e -> new QxEntity<>(e, type))
                .collect(Collectors.toList());
    }
}
