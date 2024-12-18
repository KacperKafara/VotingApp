package pl.kafara.voting.integration;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.SeedStrategy;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import pl.kafara.voting.users.dto.LoginRequest;
import pl.kafara.voting.users.dto.RegistrationRequest;

import java.time.LocalDateTime;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class CreateAccountIT extends IntegrationTestConfiguration {

    @Test
    @DataSet(value = "/dataset/users.json", strategy = SeedStrategy.REFRESH)
    public void createAccount_WhenEverythingIsOk_return201() {
        RegistrationRequest registrationRequest = new RegistrationRequest(
                "test",
                "password",
                "test@password.com",
                "Test",
                "testt",
                "+48509388178",
                LocalDateTime.now().minusYears(20),
                1,
                "pl"
        );

        given()
                .contentType("application/json")
                .when()
                .body(registrationRequest)
                .post(baseUrl + "/register")
                .then()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    @DataSet(value = "/dataset/users.json", strategy = SeedStrategy.REFRESH)
    public void createAccount_WhenEmailIsAlreadyTaken_return409() {
        RegistrationRequest registrationRequest = new RegistrationRequest(
                "test",
                "password",
                "abcd@abcd.com",
                "Test",
                "testt",
                "+48509388178",
                LocalDateTime.now().minusYears(20),
                1,
                "pl"
        );

        given()
                .contentType("application/json")
                .when()
                .body(registrationRequest)
                .post(baseUrl + "/register")
                .then()
                .assertThat()
                .statusCode(HttpStatus.CONFLICT.value());
    }

    @Test
    @DataSet(value = "/dataset/users.json", strategy = SeedStrategy.REFRESH)
    public void createAccount_WhenEmailIsInvalid_return400() {
        RegistrationRequest registrationRequest = new RegistrationRequest(
                "test",
                "password",
                "abcd",
                "Test",
                "testt",
                "+48509388178",
                LocalDateTime.now().minusYears(20),
                1,
                "pl"
        );

        given()
                .contentType("application/json")
                .when()
                .body(registrationRequest)
                .post(baseUrl + "/register")
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DataSet(value = "/dataset/users.json", strategy = SeedStrategy.REFRESH)
    public void createAccount_WhenPasswordIsTooShort_return400() {
        RegistrationRequest registrationRequest = new RegistrationRequest(
                "test",
                "pass",
                "umlumulu@ico.com",
                "Test",
                "testt",
                "+48509388178",
                LocalDateTime.now().minusYears(20),
                1,
                "pl"
        );

        given()
                .contentType("application/json")
                .when()
                .body(registrationRequest)
                .post(baseUrl + "/register")
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DataSet(value = "/dataset/users.json", strategy = SeedStrategy.REFRESH)
    public void createAccount_WhenUsernameIsTaken_return409() {
        RegistrationRequest registrationRequest = new RegistrationRequest(
                "jdoe",
                "password",
                "sdnkfnldsf@snidgf.com",
                "Test",
                "testt",
                "+48509388178",
                LocalDateTime.now().minusYears(20),
                1,
                "pl"
        );

        given()
                .contentType("application/json")
                .when()
                .body(registrationRequest)
                .post(baseUrl + "/register")
                .then()
                .assertThat()
                .statusCode(HttpStatus.CONFLICT.value());
    }

    @Test
    @DataSet(value = "/dataset/users.json", strategy = SeedStrategy.REFRESH)
    public void createAccount_WhenPhoneNumberIsTaken_return409() {
        RegistrationRequest registrationRequest = new RegistrationRequest(
                "test",
                "password",
                "jidfdfnil@dsmn.com",
                "Test",
                "testt",
                "1234567899",
                LocalDateTime.now().minusYears(20),
                1,
                "pl"
        );

        given()
                .contentType("application/json")
                .when()
                .body(registrationRequest)
                .post(baseUrl + "/register")
                .then()
                .assertThat()
                .statusCode(HttpStatus.CONFLICT.value());
    }

    @Test
    @DataSet(value = "/dataset/users.json", strategy = SeedStrategy.REFRESH)
    public void createAccount_WhenPhoneNumberIsInvalid_return400() {
        RegistrationRequest registrationRequest = new RegistrationRequest(
                "test",
                "password",
                "sdf@dgsdfg.com",
                "Test",
                "testt",
                "1123abcd",
                LocalDateTime.now().minusYears(20),
                1,
                "pl"
        );

        given()
                .contentType("application/json")
                .when()
                .body(registrationRequest)
                .post(baseUrl + "/register")
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DataSet(value = "/dataset/users.json", strategy = SeedStrategy.REFRESH)
    public void createAccount_WhenBirthDateIsInFuture_return400() {
        RegistrationRequest registrationRequest = new RegistrationRequest(
                "test",
                "password",
                "dsfsd@dsf.com",
                "Test",
                "testt",
                "+48509388178",
                LocalDateTime.now().plusYears(1),
                1,
                "pl"
        );

        given()
                .contentType("application/json")
                .when()
                .body(registrationRequest)
                .post(baseUrl + "/register")
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void createAccount_WhenUsernameIsBlank_return400() {
        RegistrationRequest registrationRequest = new RegistrationRequest(
                "",
                "password",
                "sdffsd@sdf.com",
                "Test",
                "testt",
                "+48509388178",
                LocalDateTime.now().minusYears(20),
                1,
                "pl"
        );

        given()
                .contentType("application/json")
                .when()
                .body(registrationRequest)
                .post(baseUrl + "/register")
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DataSet(value = "/dataset/verify_users.json", strategy = SeedStrategy.REFRESH)
    public void verifyAccount_WhenEverythingIsOk_userCanAuthenticate() {
        String token = "token1";

        given()
                .contentType("application/json")
                .when()
                .post(baseUrl + "/verify/" + token)
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value());

        LoginRequest loginRequest = new LoginRequest("user1", "password", "pl");
        given()
                .contentType("application/json")
                .when()
                .body(loginRequest)
                .post(baseUrl + "/authenticate")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("token", notNullValue());
    }

    @Test
    @DataSet(value = "/dataset/verify_users.json", strategy = SeedStrategy.REFRESH)
    public void verifyAccount_WhenTokenWasAlreadyUsed_return400() {
        String token = "token1";

        given()
                .contentType("application/json")
                .when()
                .post(baseUrl + "/verify/" + token)
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value());

        given()
                .contentType("application/json")
                .when()
                .post(baseUrl + "/verify/" + token)
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DataSet(value = "/dataset/verify_users.json", strategy = SeedStrategy.REFRESH)
    public void verifyAccount_WhenTokenDoesNotExist_return400() {
        String token = "token2";

        given()
                .contentType("application/json")
                .when()
                .post(baseUrl + "/verify/" + token)
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DataSet(value = "/dataset/verify_users.json", strategy = SeedStrategy.REFRESH)
    public void verifyAccount_WhenTokenIsExpired_return400() {
        String token = "token3";

        given()
                .contentType("application/json")
                .when()
                .post(baseUrl + "/verify/" + token)
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());

        LoginRequest loginRequest = new LoginRequest("user2", "password", "pl");
        given()
                .contentType("application/json")
                .when()
                .body(loginRequest)
                .post(baseUrl + "/authenticate")
                .then()
                .assertThat()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }
}
