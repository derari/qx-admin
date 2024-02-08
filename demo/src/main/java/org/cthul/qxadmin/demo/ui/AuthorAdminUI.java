package org.cthul.qxadmin.demo.ui;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import org.cthul.qxadmin.data.*;
import org.cthul.qxadmin.demo.model.Author;
import org.cthul.qxadmin.demo.repository.*;
import org.cthul.qxadmin.ui.QxController;

import java.util.*;

@Path("admin/authors")
public class AuthorAdminUI extends QxController<Author> {

    QxRepository<Author> repository = new QxPanacheRepository<>(new PanacheRepository<>() {});

    @Override
    protected QxRepository<Author> getRepository() {
        return repository;
    }

    @Override
    protected QxType<Author> getType() {
        return TYPE;
    }

    private static final QxType<Author> TYPE = new ReflectionTypeInfo<>(Author.class) {
        @Override
        public List<String> includedProperties() {
            return List.of("id", "firstName", "lastName", "books", "createdAt", "updatedAt");
        }
    };
}
