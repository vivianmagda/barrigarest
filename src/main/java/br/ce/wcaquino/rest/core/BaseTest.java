package br.ce.wcaquino.rest.core;

import org.junit.BeforeClass;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;  

public class BaseTest implements Constantes {

    @BeforeClass
    public static void setup() {
        RestAssured.baseURI = APP_BASE_URL;
        RestAssured.port = APP_PORT;
        RestAssured.basePath = APP_BASE_PATH;

        RestAssured.requestSpecification = new io.restassured.builder.RequestSpecBuilder()
                .setContentType(APP_CONTENT_TYPE)
                .build();

        RestAssured.responseSpecification = new io.restassured.builder.ResponseSpecBuilder()
                .expectResponseTime(Matchers.lessThan(MAX_TIMEOUT))
                .build();

        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

}
