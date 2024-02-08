package org.cthul.qxadmin.demo.ui;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.ws.rs.Path;
import org.cthul.qxadmin.data.*;
import org.cthul.qxadmin.demo.model.*;
import org.cthul.qxadmin.demo.repository.QxPanacheRepository;
import org.cthul.qxadmin.ui.QxController;

import java.util.List;

@Path("admin/books")
public class BookAdminUI extends QxController<Book> {

    QxRepository<Book> repository = new QxPanacheRepository<>(new PanacheRepository<>() {});

    @Override
    protected QxRepository<Book> getRepository() {
        return repository;
    }

    @Override
    protected QxType<Book> getType() {
        return TYPE;
    }

    private static final QxType<Book> TYPE = new ReflectionTypeInfo<>(Book.class) {
        @Override
        public List<String> includedProperties() {
            return List.of("id", "name", "author", "createdAt", "updatedAt");
        }
    };
}
