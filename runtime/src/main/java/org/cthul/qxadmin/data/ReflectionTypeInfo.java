package org.cthul.qxadmin.data;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.*;

public class ReflectionTypeInfo<T> implements QxType<T> {

    private final Class<T> clazz;
    private final List<QxProperty<T, ?>> properties = new ArrayList<>();
    private final Map<String, QxProperty<T, ?>> byName = new HashMap<>();

    public ReflectionTypeInfo(Class<T> clazz) {
        this.clazz = clazz;
        Class<?> c = clazz;
        while (c != null) {
            for (var f: c.getDeclaredFields()) {
                if (f.getName().startsWith("$")) continue;
                var p = property(f);
                byName.put(p.getLabel(), p);
            }
            c = c.getSuperclass();
        }
        var include = includedProperties();
        if (include == null) {
            properties.addAll(byName.values());
        } else {
            include.stream()
                    .map(byName::get)
                    .filter(Objects::nonNull)
                    .forEach(properties::add);
        }
    }

    protected Collection<String> includedProperties() {
        return null;
    }

    private QxProperty<T, ?> property(Field field) {
        return new QxProperty<>(field.getName(), field.getType(),
                getter(field), this::toString, this::toString,
                setter(field));
    }

    protected String toString(Object value) {
        return value == null ? "" : value.toString();
    }

    @SuppressWarnings({"java:S3011", "unchecked"})
    private <P> Function<T, P> getter(Field f) {
        return instance -> {
            try {
                f.setAccessible(true);
                return (P) f.get(instance);
            } catch (ReflectiveOperationException ex) {
                throw new IllegalStateException(ex);
            }
        };
    }

    @SuppressWarnings({"java:S3011"})
    private BiConsumer<T, String> setter(Field f) {
        if (f.getType() != String.class) return null;
        var name = f.getName();
        name = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
        try {
            var method = f.getDeclaringClass().getMethod(name, String.class);
            return (entity, value) -> {
                try {
                    method.invoke(entity, value);
                } catch (ReflectiveOperationException ex) {
                    throw new IllegalStateException(ex);
                }
            };
        } catch (ReflectiveOperationException ex) {
            return null;
        }
    }


    @Override
    public List<QxProperty<T, ?>> getProperties() {
        return properties;
    }

    @Override
    public QxProperty<T, ?> getId() {
        return byName.get("id");
    }

    @Override
    public T getNew() {
        try {
            return clazz.getConstructor().newInstance();
        } catch (ReflectiveOperationException ex) {
            throw new IllegalStateException(ex);
        }
    }
}
