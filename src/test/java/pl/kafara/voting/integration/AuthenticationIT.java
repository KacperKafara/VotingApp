package pl.kafara.voting.integration;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.SeedStrategy;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import pl.kafara.voting.users.dto.LoginRequest;

import static io.restassured.RestAssured.given;

public class AuthenticationIT extends IntegrationTestConfiguration {

    @Test
    @DataSet(value = "/dataset/users.json", strategy = SeedStrategy.REFRESH)
    public void authenticate_WhenValidData_ShouldReturnOk() {
        LoginRequest loginRequest = new LoginRequest("user1", "password", "pl");
        given()
                .contentType("application/json")
                .body(loginRequest)
                .when()
                .post(baseUrl + "/authenticate")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DataSet(value = "/dataset/users.json", strategy = SeedStrategy.REFRESH)
    public void authenticate_WhenInvalidData_ShouldReturnUnauthorized() {
        LoginRequest loginRequest = new LoginRequest("user1", "passwordd", "pl");
        given()
                .contentType("application/json")
                .body(loginRequest)
                .when()
                .post(baseUrl + "/authenticate")
                .then()
                .assertThat()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    @DataSet(value = "/dataset/users.json", strategy = SeedStrategy.REFRESH)
    public void authenticate_WhenInactiveAccount_ShouldReturnForbidden() {
        LoginRequest loginRequest = new LoginRequest("user2", "password", "pl");
        given()
                .contentType("application/json")
                .body(loginRequest)
                .when()
                .post(baseUrl + "/authenticate")
                .then()
                .assertThat()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    @DataSet(value = "/dataset/users.json", strategy = SeedStrategy.REFRESH)
    public void authenticate_NotValidData_ShouldReturnBadRequest() {
        LoginRequest loginRequest = new LoginRequest("us", "password", "pl");
        given()
                .contentType("application/json")
                .body(loginRequest)
                .when()
                .post(baseUrl + "/authenticate")
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DataSet(value = "/dataset/users.json", strategy = SeedStrategy.REFRESH)
    public void authenticate_WhenUserNotFound_ShouldReturnUnauthorized() {
        LoginRequest loginRequest = new LoginRequest("notfounduser", "password", "pl");
        given()
                .contentType("application/json")
                .body(loginRequest)
                .when()
                .post(baseUrl + "/authenticate")
                .then()
                .assertThat()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }
}
