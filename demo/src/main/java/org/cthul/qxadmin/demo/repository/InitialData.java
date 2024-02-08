package org.cthul.qxadmin.demo.repository;

import io.quarkus.logging.Log;
import io.quarkus.runtime.Startup;
import io.quarkus.runtime.StartupEvent;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.cthul.qxadmin.demo.model.Author;
import org.cthul.qxadmin.demo.model.Book;

@ApplicationScoped
@Startup
public class InitialData {

    @Inject
    AuthorRepository authors;

    @Inject
    BookRepository books;

    @PostConstruct
    void startUp() {
        initialize();
    }

    @Transactional
    void initialize() {
        Log.info("Initializing database");
        var alice = new Author("Alice", "Noi");
        authors.create(alice);
        var bob = new Author("Bob", "Loblaw");
        authors.create(bob);

        var cad = new Book("Ctrl+Alt+Defeat: A Programmer's Guide to Surviving System Crashes");
        cad.setAuthor(alice);
        books.create(cad);
        var cac = new Book("Code and Coffee: Java Jolts for Programmers' Souls");
        cac.setAuthor(bob);
        books.create(cac);
        var git = new Book("Git Happens: A Comedy of Errors in Version Control");
        git.setAuthor(bob);
        books.create(git);
    }
}
