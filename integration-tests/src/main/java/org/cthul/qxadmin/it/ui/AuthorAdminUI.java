package org.cthul.qxadmin.it.ui;

import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import org.cthul.qxadmin.data.*;
import org.cthul.qxadmin.it.model.Author;
import org.cthul.qxadmin.it.repository.AuthorRepository;
import org.cthul.qxadmin.ui.QxController;

@Path("ui/authors")
public class AuthorAdminUI extends QxController<Author> {

    @Inject
    AuthorRepository repository;

    @Override
    protected QxRepository<Author> getRepository() {
        return repository;
    }

    @Override
    protected QxType<Author> getType() {
        return TYPE;
    }

    private static final QxType<Author> TYPE = new ReflectionTypeInfo<>(Author.class);
}
