package org.cthul.qxadmin.data;

import java.util.function.*;

public class QxProperty<T, P> {

    private final String name;
    private final Class<P> type;
    private final Function<T, P> getter;
    private final Function<P, String> raw;
    private final Function<P, String> formatted;
    private final BiConsumer<T, String> setter;

    public QxProperty(String name, Class<P> type, Function<T, P> getter, Function<P, String> raw, Function<P, String> formatted, BiConsumer<T, String> setter) {
        this.name = name;
        this.type = type;
        this.getter = getter;
        this.raw = raw;
        this.formatted = formatted;
        this.setter = setter;
    }

    public String getLabel() {
        return name;
    }

    public Class<P> getType() {
        return type;
    }

    public P get(T entity) {
        return getter.apply(entity);
    }

    public String getRaw(T entity) {
        return raw.apply(get(entity));
    }

    public String getFormatted(T entity) {
        return formatted.apply(get(entity));
    }

    public boolean isReadOnly() {
        return setter == null;
    }

    public void set(T entity, String value) {
        if (isReadOnly()) return;
        setter.accept(entity, value);
    }
}
