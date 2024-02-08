package org.cthul.qxadmin.demo.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.cthul.qxadmin.data.QxRepository;

import java.util.stream.Stream;

public class QxPanacheRepository<T> implements QxRepository<T> {

    private final PanacheRepository<T> dao;

    public QxPanacheRepository(PanacheRepository<T> dao) {
        this.dao = dao;
    }

    @Override
    public Stream<T> getAll() {
        return dao.streamAll();
    }

    @Override
    public T get(String id) {
        return dao.findById(Long.parseLong(id));
    }

    @Override
    public void delete(String id) {
        dao.delete(get(id));
    }

    @Override
    public void persist(T entity) {
        dao.persist(entity);
    }
}
