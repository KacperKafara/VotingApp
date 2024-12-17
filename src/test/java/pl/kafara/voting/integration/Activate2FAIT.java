package pl.kafara.voting.integration;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.SeedStrategy;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import pl.kafara.voting.users.dto.Change2FaStateRequest;

import static io.restassured.RestAssured.given;

public class Activate2FAIT extends IntegrationTestConfiguration {

    @Test
    @DataSet(value = "/dataset/users.json", strategy = SeedStrategy.REFRESH)
    public void activate2FA_WhenEverythingIsCorrect_Return200() {
        Change2FaStateRequest change2FaStateRequest = new Change2FaStateRequest(true);

        given()
                .contentType("application/json")
                .when()
                .header("Authorization", "Bearer " + token)
                .body(change2FaStateRequest)
                .patch(baseUrl + "/me/2fa")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DataSet(value = "/dataset/users.json", strategy = SeedStrategy.REFRESH)
    public void activate2FA_WhenUserIsNotAuthenticated_Return403() {
        Change2FaStateRequest change2FaStateRequest = new Change2FaStateRequest(true);

        given()
                .contentType("application/json")
                .when()
                .body(change2FaStateRequest)
                .patch(baseUrl + "/me/2fa")
                .then()
                .assertThat()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    @Order(1)
    @DataSet(value = "/dataset/users.json", strategy = SeedStrategy.REFRESH)
    public void activate2FA_WhenUserHave2FAActiveAlready_Return400() {
        Change2FaStateRequest change2FaStateRequest = new Change2FaStateRequest(true);
        String jwt = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJWb3RpbmdBcHAiLCJzdWIiOiI1ZTY0MmQwYS05NGQ0LTRhNGYtODc2MC1jZDZkNjNjZDEwNDMiLCJ1c2VybmFtZSI6InRqb2huc29uIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9VU0VSIl0sImlhdCI6MTczNDQ2NTk4OCwiZXhwIjoxODI4ODg4Mzg4fQ.5rwJ9ZTnW_SaqQsI3UY4xg6mA-eaSRSt2xhnHorxi0g";

        given()
                .contentType("application/json")
                .when()
                .header("Authorization", "Bearer " + jwt)
                .body(change2FaStateRequest)
                .patch(baseUrl + "/me/2fa")
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @Order(2)
    @DataSet(value = "/dataset/users.json", strategy = SeedStrategy.REFRESH)
    public void deactivate2FA_WhenEverythingIsCorrect_Return200() {
        Change2FaStateRequest change2FaStateRequest = new Change2FaStateRequest(false);
        String jwt = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJWb3RpbmdBcHAiLCJzdWIiOiI1ZTY0MmQwYS05NGQ0LTRhNGYtODc2MC1jZDZkNjNjZDEwNDMiLCJ1c2VybmFtZSI6InRqb2huc29uIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9VU0VSIl0sImlhdCI6MTczNDQ2NTk4OCwiZXhwIjoxODI4ODg4Mzg4fQ.5rwJ9ZTnW_SaqQsI3UY4xg6mA-eaSRSt2xhnHorxi0g";

        given()
                .contentType("application/json")
                .when()
                .header("Authorization", "Bearer " + jwt)
                .body(change2FaStateRequest)
                .patch(baseUrl + "/me/2fa")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DataSet(value = "/dataset/users.json", strategy = SeedStrategy.REFRESH)
    public void deactivate2FA_WhenUserHaveNotActive2FA_Return200() {
        Change2FaStateRequest change2FaStateRequest = new Change2FaStateRequest(false);

        given()
                .contentType("application/json")
                .when()
                .header("Authorization", "Bearer " + token)
                .body(change2FaStateRequest)
                .patch(baseUrl + "/me/2fa")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value());
    }
}
