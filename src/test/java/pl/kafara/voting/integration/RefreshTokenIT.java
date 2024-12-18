package pl.kafara.voting.integration;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.SeedStrategy;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import pl.kafara.voting.util.SensitiveData;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class RefreshTokenIT extends IntegrationTestConfiguration {

    @Test
    @DataSet(value = "/dataset/users.json", strategy = SeedStrategy.REFRESH)
    public void refreshToken_WhenEverythingIsOk_return200() {
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJWb3RpbmdBcHAiLCJzdWIiOiI1ZTY0MmQwYS05NGQ0LTRhNGYtODc2MC1jZDZkNjNjZDEwNDMiLCJ1c2VybmFtZSI6InRqb2huc29uIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9VU0VSIl0sImlhdCI6MTczNDQ2NTk4OCwiZXhwIjoxODI4ODg4Mzg4fQ.5rwJ9ZTnW_SaqQsI3UY4xg6mA-eaSRSt2xhnHorxi0g";
        String refreshToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJWb3RpbmdBcHAiLCJzdWIiOiI1ZTY0MmQwYS05NGQ0LTRhNGYtODc2MC1jZDZkNjNjZDEwNDMiLCJpYXQiOjE3MzQ0NjU5ODgsImV4cCI6MTgyMDg2MjM4OH0.VHj5EKjdkCzAWSZ3IWLv31VLigGJ845OvmoChsk-Pgc";
        SensitiveData sensitiveData = new SensitiveData(refreshToken);

        Response response2 = given()
                .contentType("application/json")
                .when()
                .body(sensitiveData)
                .post(baseUrl + "/refresh")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract().response();

        String newJwtToken = response2.jsonPath().getString("token");
        assertNotEquals(newJwtToken, token);
        assertEquals(refreshToken, response2.jsonPath().getString("refreshToken"));
    }

    @Test
    @DataSet(value = "/dataset/users.json", strategy = SeedStrategy.REFRESH)
    public void refreshToken_WhenTokenIsInvalid_return401() {
        String refreshToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJWb3RpbmdBcHAiLCJzdWIiOiI1ZTY0MmQwYS05NGQ0LTRhNGYtODc2MC1jZDZkNjNjZDEwMjIiLCJpYXQiOjE3MzQ0NjU5ODgsImV4cCI6MTgyMDg2MjM4OH0.qsf-FxjPElHF0uBOrbaHjPnNd-pHRg-GGCReskNGQrM";
        SensitiveData sensitiveData = new SensitiveData(refreshToken);

        given()
                .contentType("application/json")
                .when()
                .body(sensitiveData)
                .post(baseUrl + "/refresh")
                .then()
                .assertThat()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }
}
