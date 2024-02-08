package org.cthul.qxadmin.dev;

import io.quarkus.arc.Arc;
import io.quarkus.arc.InstanceHandle;
import jakarta.persistence.EntityManager;
import org.cthul.qxadmin.data.*;

import java.util.List;
import java.util.stream.Stream;

@SuppressWarnings({"unchecked", "rawtypes"})
public class GenericQxFactory {

    public <T> QxRepository<T> newRepository(Class<T> clazz) {
        try (InstanceHandle<EntityManager> em = Arc.container().instance(EntityManager.class)) {
            return new JakartaRepository<>(clazz, em.get());
        }
    }

    public <T> QxRepository<T> newRepository(String name) {
        try {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            Class<T> c = (Class) Class.forName(name, false, cl);
            return newRepository(c);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public <T> QxType<T> newType(Class<T> clazz) {
        return new ReflectionTypeInfo<>(clazz);
    }

    public <T> QxType<T> newType(String name) {
        try {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            Class<T> c = (Class) Class.forName(name, false, cl);
            return newType(c);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
