package pl.kafara.voting.integration;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.SeedStrategy;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import pl.kafara.voting.users.dto.LoginRequest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RoleRequestIT extends IntegrationTestConfiguration {

    @Test
    @DataSet(value = "/dataset/role_request_users.json", strategy = SeedStrategy.REFRESH)
    public void createRoleRequest_userWithVoterRole_shouldReturn403() {
        LoginRequest loginRequest = new LoginRequest("user2", "password", "pl");

        Response response = given()
                .contentType("application/json")
                .body(loginRequest)
                .when()
                .post(baseUrl + "/authenticate")
                .then()
                .extract()
                .response();
        String token = response.jsonPath().getString("token");

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .post(baseUrl + "/voterRoleRequest")
                .then()
                .assertThat()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    @DataSet(value = "/dataset/role_request_users.json", strategy = SeedStrategy.REFRESH)
    public void createRoleRequest_userWithoutVoterRole_shouldReturn200() {
        Response listResponse = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseUrl + "/voterRoleRequest")
                .then()
                .extract()
                .response();

        int numberOfRequestsBefore = listResponse.jsonPath().getList("requests").size();

        LoginRequest loginRequest = new LoginRequest("user4", "password", "pl");

        Response response = given()
                .contentType("application/json")
                .body(loginRequest)
                .when()
                .post(baseUrl + "/authenticate")
                .then()
                .extract()
                .response();
        String jwt = response.jsonPath().getString("token");

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + jwt)
                .when()
                .post(baseUrl + "/voterRoleRequest")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value());

        int sizeAfter = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseUrl + "/voterRoleRequest")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath()
                .getList("requests")
                .size();

        assertEquals(numberOfRequestsBefore + 1, sizeAfter);
    }

    @Test
    @DataSet(value = "/dataset/role_request_users.json", strategy = SeedStrategy.REFRESH)
    public void acceptRoleRequest_everythingIsOk_shouldReturn200() {
        Response listResponse = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseUrl + "/voterRoleRequest")
                .then()
                .extract()
                .response();

        int numberOfRequestsBefore = listResponse.jsonPath().getList("requests").size();

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .post(baseUrl + "/voterRoleRequest/1a678822-b202-4316-9231-4a13cbe5cc91")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value());

        int sizeAfter = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseUrl + "/voterRoleRequest")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath()
                .getList("requests")
                .size();

        assertEquals(numberOfRequestsBefore - 1, sizeAfter);
    }

    @Test
    @DataSet(value = "/dataset/role_request_users.json", strategy = SeedStrategy.REFRESH)
    public void rejectRoleRequest_everythingIsOk_shouldReturn200() {
        Response listResponse = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseUrl + "/voterRoleRequest")
                .then()
                .extract()
                .response();

        int numberOfRequestsBefore = listResponse.jsonPath().getList("requests").size();

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .delete(baseUrl + "/voterRoleRequest/1a678822-b202-4316-9231-4a13cbe5cc91")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value());

        int sizeAfter = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseUrl + "/voterRoleRequest")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath()
                .getList("requests")
                .size();

        assertEquals(numberOfRequestsBefore - 1, sizeAfter);
    }

    @Test
    @DataSet(value = "/dataset/role_request_users.json", strategy = SeedStrategy.REFRESH)
    public void acceptRoleRequest_acceptThenReject_return200and400() {
        Response listResponse = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseUrl + "/voterRoleRequest")
                .then()
                .extract()
                .response();

        int numberOfRequestsBefore = listResponse.jsonPath().getList("requests").size();

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .post(baseUrl + "/voterRoleRequest/1a678822-b202-4316-9231-4a13cbe5cc91")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value());

        int sizeAfterAccept = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseUrl + "/voterRoleRequest")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath()
                .getList("requests")
                .size();

        assertEquals(numberOfRequestsBefore - 1, sizeAfterAccept);

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .delete(baseUrl + "/voterRoleRequest/1a678822-b202-4316-9231-4a13cbe5cc91")
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());

        int sizeAfterReject = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseUrl + "/voterRoleRequest")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath()
                .getList("requests")
                .size();

        assertEquals(numberOfRequestsBefore - 1, sizeAfterReject);
    }

    @Test
    @DataSet(value = "/dataset/role_request_users.json", strategy = SeedStrategy.REFRESH)
    public void rejectRole_rejectThenAccept_return200and400() {
        Response listResponse = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseUrl + "/voterRoleRequest")
                .then()
                .extract()
                .response();

        int numberOfRequestsBefore = listResponse.jsonPath().getList("requests").size();

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .delete(baseUrl + "/voterRoleRequest/b2d44bb0-1665-437d-98ec-ee8061aab53b")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value());

        int sizeAfterReject = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseUrl + "/voterRoleRequest")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath()
                .getList("requests")
                .size();

        assertEquals(numberOfRequestsBefore - 1, sizeAfterReject);

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .post(baseUrl + "/voterRoleRequest/b2d44bb0-1665-437d-98ec-ee8061aab53b")
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());

        int sizeAfterAccept = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseUrl + "/voterRoleRequest")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath()
                .getList("requests")
                .size();

        assertEquals(numberOfRequestsBefore - 1, sizeAfterAccept);
    }

    @Test
    @DataSet(value = "/dataset/role_request_users.json", strategy = SeedStrategy.REFRESH)
    public void rejectRoleRequest_rejectThenCreateAnother_shouldReturn200() {
        Response listResponse = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseUrl + "/voterRoleRequest")
                .then()
                .extract()
                .response();

        int numberOfRequestsBefore = listResponse.jsonPath().getList("requests").size();

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .delete(baseUrl + "/voterRoleRequest/1a678822-b202-4316-9231-4a13cbe5cc91")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value());

        int sizeAfterReject = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseUrl + "/voterRoleRequest")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath()
                .getList("requests")
                .size();

        assertEquals(numberOfRequestsBefore - 1, sizeAfterReject);

        LoginRequest loginRequest = new LoginRequest("user1", "password", "pl");

        Response response = given()
                .contentType("application/json")
                .body(loginRequest)
                .when()
                .post(baseUrl + "/authenticate")
                .then()
                .extract()
                .response();
        String jwt = response.jsonPath().getString("token");

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + jwt)
                .when()
                .post(baseUrl + "/voterRoleRequest")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value());

        int sizeAfterCreateAnother = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseUrl + "/voterRoleRequest")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath()
                .getList("requests")
                .size();

        assertEquals(numberOfRequestsBefore, sizeAfterCreateAnother);
    }

    @Test
    @DataSet(value = "/dataset/role_request_users.json", strategy = SeedStrategy.REFRESH)
    public void acceptRequest_acceptThenCreateAnother_shouldReturn400() {
        LoginRequest loginRequest = new LoginRequest("user1", "password", "pl");

        Response response = given()
                .contentType("application/json")
                .body(loginRequest)
                .when()
                .post(baseUrl + "/authenticate")
                .then()
                .extract()
                .response();
        String jwt = response.jsonPath().getString("token");

        Response listResponse = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseUrl + "/voterRoleRequest")
                .then()
                .extract()
                .response();

        int numberOfRequestsBefore = listResponse.jsonPath().getList("requests").size();

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .post(baseUrl + "/voterRoleRequest/1a678822-b202-4316-9231-4a13cbe5cc91")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value());

        int sizeAfterAccept = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseUrl + "/voterRoleRequest")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath()
                .getList("requests")
                .size();

        assertEquals(numberOfRequestsBefore - 1, sizeAfterAccept);

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + jwt)
                .when()
                .post(baseUrl + "/voterRoleRequest")
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}
