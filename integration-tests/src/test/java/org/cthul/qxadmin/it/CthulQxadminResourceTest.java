package org.cthul.qxadmin.it;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class CthulQxadminResourceTest {

    @Test
    void get_htmxjs() {
        given()
            .when()
                .get("/qx/htmx.min.js")
            .then()
                .statusCode(200);
    }

    @Test
    void get_missingcss() {
        given()
            .when()
                .get("/qx/missing.min.css")
            .then()
                .statusCode(200);
    }
}
