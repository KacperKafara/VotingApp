package pl.kafara.voting.integration;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.SeedStrategy;
import dev.samstevens.totp.code.CodeGenerator;
import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.exceptions.CodeGenerationException;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import pl.kafara.voting.users.dto.LoginRequest;
import pl.kafara.voting.users.dto.TotpLoginRequest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class OTPAuthenticationIT extends IntegrationTestConfiguration {

    String key = "52VTQSSL4CSBF5NVCL6BHOX4BGQ7F43R";
    TimeProvider timeProvider = new SystemTimeProvider();
    CodeGenerator codeGenerator = new DefaultCodeGenerator();

    @Test
    @Order(1)
    @DataSet(value = "/dataset/otp_authentication_users.json", strategy = SeedStrategy.REFRESH)
    public void authenticate_WhenEverythingIsOk_return200() throws CodeGenerationException {
        long timePeriod = timeProvider.getTime() / 30;
        String code = codeGenerator.generate(key, timePeriod);
        TotpLoginRequest loginRequest = new TotpLoginRequest("user1", code);

        given()
                .contentType("application/json")
                .when()
                .body(loginRequest)
                .post(baseUrl + "/verifyTotp")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("token", notNullValue());
    }

    @Test
    @DataSet(value = "/dataset/otp_authentication_users_blocked.json", strategy = SeedStrategy.REFRESH, cleanBefore = true)
    public void authenticate_WhenAccountIsNotActiveOrBlocked_return403() throws CodeGenerationException {
        long timePeriod = timeProvider.getTime() / 30;
        String code = codeGenerator.generate(key, timePeriod);
        TotpLoginRequest loginRequest = new TotpLoginRequest("user2", code);

        given()
                .contentType("application/json")
                .when()
                .body(loginRequest)
                .post(baseUrl + "/verifyTotp")
                .then()
                .assertThat()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    @Order(2)
    @DataSet(value = "/dataset/otp_authentication_users.json", strategy = SeedStrategy.REFRESH)
    public void authenticate_WhenInvalidPasswordData_return401() throws CodeGenerationException {
        String key = "52VTQSSL4CSBF5NVCL6BHOX4BGQ7F43F";
        long timePeriod = timeProvider.getTime() / 30;
        String code = codeGenerator.generate(key, timePeriod);
        TotpLoginRequest loginRequest = new TotpLoginRequest("user1", code);

        given()
                .contentType("application/json")
                .when()
                .body(loginRequest)
                .post(baseUrl + "/verifyTotp")
                .then()
                .assertThat()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    @Order(3)
    @DataSet(value = "/dataset/otp_authentication_users.json", strategy = SeedStrategy.REFRESH)
    public void authenticate_WhenMFARequired_return200() {
        LoginRequest loginRequest = new LoginRequest("user1", "password", "pl");

        given()
                .contentType("application/json")
                .when()
                .body(loginRequest)
                .post(baseUrl + "/authenticate")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("code", equalTo("MFA_REQUIRED"));
    }

    @Test
    @Order(4)
    @DataSet(value = "/dataset/otp_authentication_users.json", strategy = SeedStrategy.REFRESH)
    public void authenticate_WhenUserDoesNotExist_return401() throws CodeGenerationException {
        long timePeriod = timeProvider.getTime() / 30;
        String code = codeGenerator.generate(key, timePeriod);
        TotpLoginRequest loginRequest = new TotpLoginRequest("user10", code);

        given()
                .contentType("application/json")
                .when()
                .body(loginRequest)
                .post(baseUrl + "/verifyTotp")
                .then()
                .assertThat()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

}
