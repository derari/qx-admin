package org.cthul.qxadmin.data;

import jakarta.persistence.EntityManager;

import java.util.stream.Stream;

public class JakartaRepository<T> implements QxRepository<T> {

    private final Class<T> clazz;
    private final EntityManager entityManager;

    public JakartaRepository(Class<T> clazz, EntityManager entityManager) {
        this.clazz = clazz;
        this.entityManager = entityManager;
    }

    @Override
    public Stream<T> getAll() {
        return entityManager.createQuery("FROM " + clazz.getName(), clazz).getResultStream();
    }

    @Override
    public T get(String id) {
        return entityManager.find(clazz, id);
    }

    @Override
    public void delete(String id) {
        entityManager.remove(get(id));
    }

    @Override
    public void persist(T entity) {
        entityManager.persist(entity);
    }
}
