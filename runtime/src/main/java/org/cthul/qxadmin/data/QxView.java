package org.cthul.qxadmin.data;

public class QxView<T> {

    private final String label;
    private final String path;
    private final QxType<T> type;
    private final QxRepository<T> repository;

    public QxView(String label, String path, QxType<T> type, QxRepository<T> repository) {
        this.label = label;
        this.path = path;
        this.type = type;
        this.repository = repository;
    }

    public String getLabel() {
        return label;
    }

    public String getPath() {
        return path;
    }

    public QxType<T> getType() {
        return type;
    }

    public QxRepository<T> getRepository() {
        return repository;
    }
}
