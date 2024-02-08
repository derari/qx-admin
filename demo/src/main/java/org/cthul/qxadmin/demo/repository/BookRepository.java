package org.cthul.qxadmin.demo.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.cthul.qxadmin.data.QxRepository;
import org.cthul.qxadmin.demo.model.Author;
import org.cthul.qxadmin.demo.model.Book;

import java.util.stream.Stream;

@ApplicationScoped
public class BookRepository implements QxRepository<Book> {

    public Book get(long id) {
        return DAO.findById(id);
    }

    public void create(Book author) {
        DAO.persist(author);
    }

    @Override
    public Stream<Book> getAll() {
        return DAO.streamAll();
    }

    @Override
    public Book get(String id) {
        return DAO.findById(Long.parseLong(id));
    }

    private static final PanacheRepository<Book> DAO = new PanacheRepository<>() { };
}
