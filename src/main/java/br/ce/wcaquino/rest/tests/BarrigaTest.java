package br.ce.wcaquino.rest.tests;

import org.junit.Test;

import io.restassured.RestAssured;

public class BarrigaTest {

    @Test
    public void naoDeveAcessarAPISemToken() {
        RestAssured.given()
            .when()
                .get("/contas")
            .then()
                .statusCode(401);
    }

}
