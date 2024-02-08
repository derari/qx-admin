package org.cthul.qxadmin.demo.rest;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import org.cthul.qxadmin.demo.model.Author;
import org.cthul.qxadmin.demo.repository.AuthorRepository;
import org.jboss.resteasy.reactive.RestPath;

@Path("api/authors")
public class AuthorResource {

    @Inject
    AuthorRepository repository;

    @GET
    @Path("{id}")
    public Author get(@RestPath long id) {
        return repository.get(id);
    }

    @GET
    @Path("reset")
    @Transactional
    public int reset() {
        repository.create(new Author("Bob", "Loblaw"));
        return 0;
    }
}
