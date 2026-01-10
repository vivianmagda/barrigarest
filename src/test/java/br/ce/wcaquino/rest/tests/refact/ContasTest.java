package br.ce.wcaquino.rest.tests.refact;

import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;

import io.restassured.RestAssured;

public class ContasTest {

     @BeforeClass //executa uma vez antes da classe
    public static void login(){
         Map<String, String> login = new HashMap<>();
        login.put("email", "vivianmagda@live.com");
        login.put("senha", "6ir.Le,J5G6'FA6");

        String TOKEN = given()
            .body(login)
        .when()
            .post("/signin")
        .then()
            .statusCode(200)
            .extract().path("token");

        RestAssured.requestSpecification.header("Authorization", "JWT " + TOKEN); //API mais recente seria bearer

        RestAssured.get("/reset").then().statusCode(200);
    }

}
