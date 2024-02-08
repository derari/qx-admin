package org.cthul.qxadmin.it;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
class StaticFilesTest {

    @Test
    void htmx_js() {
        given()
            .when()
                .get("/qx/htmx.min.js")
            .then()
                .statusCode(200);
    }
}
