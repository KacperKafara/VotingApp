package pl.kafara.voting.integration;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.SeedStrategy;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import pl.kafara.voting.users.dto.ChangePasswordRequest;

import static io.restassured.RestAssured.given;

public class ChangePasswordIT extends IntegrationTestConfiguration {

    @Test
    @DataSet(value = "/dataset/users.json", strategy = SeedStrategy.REFRESH)
    public void changePassword_WhenOldPasswordIsIncorrect_Return400() {
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest("incorrect", "newPassword");
        given()
                .contentType("application/json")
                .when()
                .header("Authorization", "Bearer " + token)
                .body(changePasswordRequest)
                .patch(baseUrl + "/me/password")
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DataSet(value = "/dataset/users.json", strategy = SeedStrategy.REFRESH)
    public void changePassword_WhenNewPasswordIsTooShort_Return400() {
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest("password", "short");
        given()
                .contentType("application/json")
                .when()
                .header("Authorization", "Bearer " + token)
                .body(changePasswordRequest)
                .patch(baseUrl + "/me/password")
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DataSet(value = "/dataset/users.json", strategy = SeedStrategy.REFRESH)
    public void changePassword_whenEverythingIsCorrect_Return200() {
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest("password", "newPassword");
        given()
                .contentType("application/json")
                .when()
                .header("Authorization", "Bearer " + token)
                .body(changePasswordRequest)
                .patch(baseUrl + "/me/password")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value());
    }
}
