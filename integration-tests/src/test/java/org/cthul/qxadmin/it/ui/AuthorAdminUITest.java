package org.cthul.qxadmin.it.ui;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.cthul.qxadmin.it.model.Author;
import org.cthul.qxadmin.it.repository.AuthorRepository;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class AuthorAdminUITest {

    @Inject
    AuthorRepository repository;

    @Test
    void index() {
        withTransaction(() -> repository.create(new Author("Bob", "Loblaw")));

        given()
            .when()
                .get("ui/authors")
            .then()
                .statusCode(200)
                .body(containsString("Author"));
    }

    @Test
    void indexList() {
        withTransaction(() -> repository.create(new Author("Bob", "Loblaw")));

        given()
            .when()
                .headers("HX-Request", "true",
                        "HX-Target", "table-content")
                .get("ui/authors")
            .then()
                .statusCode(200)
                .body(containsString("Loblaw"));
    }

    @Transactional
    void withTransaction(Runnable r) {
        r.run();
    }
}