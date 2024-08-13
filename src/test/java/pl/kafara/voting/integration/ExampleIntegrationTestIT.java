package pl.kafara.voting.integration;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class ExampleIntegrationTestIT extends IntegrationTestConfiguration {

    @Test
    void exampleTest() {
        given()
                .when()
                .get(baseUrl + "/actuator/health")
                .then()
                .assertThat()
                .statusCode(200);
    }
}
