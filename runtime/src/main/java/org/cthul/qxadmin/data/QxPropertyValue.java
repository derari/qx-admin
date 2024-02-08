package org.cthul.qxadmin.data;

public class QxPropertyValue<T> {

    private final T entity;
    private final QxProperty<T, ?> property;

    public QxPropertyValue(T entity, QxProperty<T, ?> property) {
        this.entity = entity;
        this.property = property;
    }

    public String getLabel() {
        return property.getLabel();
    }

    public boolean isReadOnly() {
        return property.isReadOnly();
    }

    public String getFormattedValue() {
        return property.getFormatted(entity);
    }
}
