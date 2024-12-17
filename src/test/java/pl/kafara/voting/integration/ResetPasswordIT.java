package pl.kafara.voting.integration;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.SeedStrategy;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import pl.kafara.voting.users.dto.LoginRequest;
import pl.kafara.voting.users.dto.ResetPasswordFormRequest;
import pl.kafara.voting.users.dto.ResetPasswordRequest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.not;

public class ResetPasswordIT extends IntegrationTestConfiguration {

    @Test
    @DataSet(value = "/dataset/reset_password_users.json", strategy = SeedStrategy.REFRESH)
    public void resetPassword_WhenUserNotExists_Return404() {
        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest("mlemlemle@mle.com", "pl");

        given()
                .contentType("application/json")
                .when()
                .body(resetPasswordRequest)
                .post(baseUrl + "/resetPassword")
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DataSet(value = "/dataset/reset_password_users.json", strategy = SeedStrategy.REFRESH)
    public void resetPassword_WhenUserIsBlocked_Return403() {
        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest("abcd@abcd.com", "pl");

        given()
                .contentType("application/json")
                .when()
                .body(resetPasswordRequest)
                .post(baseUrl + "/resetPassword")
                .then()
                .assertThat()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    @DataSet(value = "/dataset/reset_password_users.json", strategy = SeedStrategy.REFRESH)
    public void resetPassword_TokenVerificationFailed_return400() {
        given()
                .contentType("application/json")
                .when()
                .post(baseUrl + "/resetPassword/123456/verify")
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DataSet(value = "/dataset/reset_password_users.json", strategy = SeedStrategy.REFRESH)
    public void resetPassword_TokenVerificationSuccess_return200() {
        given()
                .contentType("application/json")
                .when()
                .post(baseUrl + "/resetPassword/token1/verify")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DataSet(value = "/dataset/reset_password_users.json", strategy = SeedStrategy.REFRESH)
    public void resetPassword_WhenTokenVerificationFailed_return400() {
        ResetPasswordFormRequest resetPasswordFormRequest = new ResetPasswordFormRequest("password3");

        given()
                .contentType("application/json")
                .when()
                .body(resetPasswordFormRequest)
                .post(baseUrl + "/resetPassword/123456")
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DataSet(value = "/dataset/reset_password_users.json", strategy = SeedStrategy.REFRESH)
    public void resetPassword_WhenTokenVerificationSuccess_return200() {
        ResetPasswordFormRequest resetPasswordFormRequest = new ResetPasswordFormRequest("password3");

        given()
                .contentType("application/json")
                .when()
                .body(resetPasswordFormRequest)
                .post(baseUrl + "/resetPassword/token1")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value());

        LoginRequest loginRequest = new LoginRequest("user1", "password", "pl");
        given()
                .contentType("application/json")
                .body(loginRequest)
                .when()
                .post(baseUrl + "/authenticate")
                .then()
                .assertThat()
                .statusCode(not(HttpStatus.OK.value()));

        loginRequest = new LoginRequest("user1", "password3", "pl");
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
    @DataSet(value = "/dataset/reset_password_users.json", strategy = SeedStrategy.REFRESH)
    public void resetPassword_whenPasswordTooShort_return400() {
        ResetPasswordFormRequest resetPasswordFormRequest = new ResetPasswordFormRequest("pass");

        given()
                .contentType("application/json")
                .when()
                .body(resetPasswordFormRequest)
                .post(baseUrl + "/resetPassword/token1")
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}
