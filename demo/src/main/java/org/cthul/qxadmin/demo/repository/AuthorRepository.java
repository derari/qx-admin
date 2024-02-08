package org.cthul.qxadmin.demo.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.cthul.qxadmin.demo.model.Author;
import org.cthul.qxadmin.data.QxRepository;

import java.util.stream.Stream;

@ApplicationScoped
public class AuthorRepository implements QxRepository<Author> {

    public Author get(long id) {
        return DAO.findById(id);
    }

    public void create(Author author) {
        DAO.persist(author);
    }

    @Override
    public Stream<Author> getAll() {
        return DAO.streamAll();
    }

    @Override
    public Author get(String id) {
        return DAO.findById(Long.parseLong(id));
    }

    private static final PanacheRepository<Author> DAO = new PanacheRepository<>() { };
}
