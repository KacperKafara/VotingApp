package pl.kafara.voting.integration;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.SeedStrategy;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import pl.kafara.voting.users.dto.LoginRequest;
import pl.kafara.voting.users.dto.UpdateUserDataRequest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

public class EditUserDataIT extends IntegrationTestConfiguration {

    @Test
    @DataSet(value = "/dataset/users.json", strategy = SeedStrategy.REFRESH)
    public void updateOwnData_WhenIfMatchHeaderIsNotProvided_ShouldReturnBadRequest() {
        UpdateUserDataRequest request = new UpdateUserDataRequest(
                "newUsername",
                "newFirstName",
                "newLastName",
                "+48123456789",
                "abcd@abcd.com",
                "MALE"
        );
        given()
                .contentType("application/json")
                .body(request)
                .when()
                .put(baseUrl + "/me")
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DataSet(value = "/dataset/users.json", strategy = SeedStrategy.REFRESH)
    public void updateOwnData_WhenDataIsCorrect_ShouldReturnOk() {
        Response response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseUrl + "/me")
                .then()
                .extract()
                .response();

        String etag = response.getHeader("ETag");
        etag = etag.substring(1, etag.length() - 1);

        UpdateUserDataRequest request = new UpdateUserDataRequest(
                "newUsername",
                "newFirstName",
                "newLastName",
                "+48123456789",
                "email@notexisting.com",
                "MALE"
        );

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .header("If-Match", etag)
                .body(request)
                .when()
                .put(baseUrl + "/me")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value());

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseUrl + "/me")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("username", equalTo("newUsername"))
                .body("firstName", equalTo("newFirstName"))
                .body("lastName", equalTo("newLastName"))
                .body("phoneNumber", equalTo("+48123456789"))
                .body("email", equalTo("email@notexisting.com"));
    }

    @Test
    @DataSet(value = "/dataset/users.json", strategy = SeedStrategy.REFRESH)
    public void updateOwnData_WhenDataIsIncorrect_ShouldReturnBadRequest() {
        Response response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseUrl + "/me")
                .then()
                .extract()
                .response();

        String etag = response.getHeader("ETag");
        etag = etag.substring(1, etag.length() - 1);
        UpdateUserDataRequest request = new UpdateUserDataRequest(
                "ew",
                "newFirstName",
                "newLastName",
                "+48123456789",
                "hahah@ehhe.com",
                "MALE"
        );

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .header("If-Match", etag)
                .body(request)
                .when()
                .put(baseUrl + "/me")
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DataSet(value = "/dataset/users.json", strategy = SeedStrategy.REFRESH)
    public void updateOwnData_WhenDataIsCorrectButUserIsNotAuthenticated_ShouldReturnForbidden() {
        Response response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseUrl + "/me")
                .then()
                .extract()
                .response();

        String etag = response.getHeader("ETag");
        etag = etag.substring(1, etag.length() - 1);

        UpdateUserDataRequest request = new UpdateUserDataRequest(
                "newUsername",
                "newFirstName",
                "newLastName",
                "+48123456789",
                "email@notexisting.com",
                "MALE"
        );

        given()
                .contentType("application/json")
                .header("If-Match", etag)
                .body(request)
                .when()
                .put(baseUrl + "/me")
                .then()
                .assertThat()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    @DataSet(value = "/dataset/users.json", strategy = SeedStrategy.REFRESH)
    public void updateOwnData_WhenRaceCondition_ShouldReturnPreconditionFailed() {
        Response response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseUrl + "/me")
                .then()
                .extract()
                .response();

        String etag = response.getHeader("ETag");
        etag = etag.substring(1, etag.length() - 1);

        UpdateUserDataRequest request = new UpdateUserDataRequest(
                "newUsername",
                "newFirstName",
                "newLastName",
                "+48123456789",
                "newmail@newmail.com",
                "OTHER"
        );
        UpdateUserDataRequest request2 = new UpdateUserDataRequest(
                "newUsername2",
                "newFirstName2",
                "newLastName2",
                "+48123456789",
                "amama@mama.com",
                "FEMALE"
        );

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .header("If-Match", etag)
                .body(request)
                .when()
                .put(baseUrl + "/me")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value());

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .header("If-Match", etag)
                .body(request2)
                .when()
                .put(baseUrl + "/me")
                .then()
                .assertThat()
                .statusCode(HttpStatus.PRECONDITION_FAILED.value());

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseUrl + "/me")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("username", equalTo("newUsername"))
                .body("firstName", equalTo("newFirstName"))
                .body("lastName", equalTo("newLastName"))
                .body("phoneNumber", equalTo("+48123456789"))
                .body("email", equalTo("newmail@newmail.com"));
    }

    @Test
    @DataSet(value = "/dataset/users.json", strategy = SeedStrategy.REFRESH)
    public void updateOtherUser_WhenDataIsCorrect_ShouldReturnOk() {
        Response response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseUrl + "/users/user1")
                .then()
                .extract()
                .response();

        String etag = response.getHeader("ETag");
        etag = etag.substring(1, etag.length() - 1);

        UpdateUserDataRequest request = new UpdateUserDataRequest(
                "user1",
                "newFirstName2",
                "newLastName2",
                "+48111111111",
                "amama@mama.com",
                "FEMALE"
        );

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .header("If-Match", etag)
                .body(request)
                .when()
                .put(baseUrl + "/users/f77cf369-337e-4bfd-bc85-aa7d63fa244b")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value());

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseUrl + "/users/user1")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("username", equalTo("user1"))
                .body("firstName", equalTo("newFirstName2"))
                .body("lastName", equalTo("newLastName2"))
                .body("phoneNumber", equalTo("+48111111111"))
                .body("email", equalTo("amama@mama.com"));
    }

    @Test
    @DataSet(value = "/dataset/users.json", strategy = SeedStrategy.REFRESH)
    public void updateOtherUser_WhenDataIsIncorrect_ShouldReturnBadRequest() {
        Response response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseUrl + "/users/user1")
                .then()
                .extract()
                .response();

        String etag = response.getHeader("ETag");
        etag = etag.substring(1, etag.length() - 1);
        UpdateUserDataRequest request = new UpdateUserDataRequest(
                "ew",
                "newFirstName",
                "newLastName",
                "+48123456789",
                "amama@mama.com",
                "MALE"
        );

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .header("If-Match", etag)
                .body(request)
                .when()
                .put(baseUrl + "/users/f77cf369-337e-4bfd-bc85-aa7d63fa244b")
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseUrl + "/users/user1")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("firstName", not(equalTo("newFirstName")))
                .body("username", not(equalTo("username1")));
    }

    @Test
    @DataSet(value = "/dataset/users.json", strategy = SeedStrategy.REFRESH)
    public void updateOtherUser_WhenDataIsCorrectButUserIsNotAuthenticated_ShouldReturnForbidden() {
        Response response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseUrl + "/users/user1")
                .then()
                .extract()
                .response();

        String etag = response.getHeader("ETag");
        etag = etag.substring(1, etag.length() - 1);
        UpdateUserDataRequest request = new UpdateUserDataRequest(
                "newUsername",
                "newFirstName",
                "newLastName",
                "+48123456789",
                "dfskun@djnds.com",
                "MALE"
        );

        given()
                .contentType("application/json")
                .header("If-Match", etag)
                .body(request)
                .when()
                .put(baseUrl + "/users/f77cf369-337e-4bfd-bc85-aa7d63fa244b")
                .then()
                .assertThat()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    @DataSet(value = "/dataset/users.json", strategy = SeedStrategy.REFRESH)
    public void updateOtherUser_WhenRaceCondition_ShouldReturnPreconditionFailed() {
        Response response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseUrl + "/users/user1")
                .then()
                .extract()
                .response();

        String etag = response.getHeader("ETag");
        etag = etag.substring(1, etag.length() - 1);

        UpdateUserDataRequest request = new UpdateUserDataRequest(
                "user1",
                "newFirstName2",
                "newLastName2",
                "+48222222222",
                "amama@mama.com",
                "FEMALE"
        );
        UpdateUserDataRequest request2 = new UpdateUserDataRequest(
                "newUsername3",
                "newFirstName3",
                "newLastName3",
                "+48111111111",
                "dfgfd@sdf.com",
                "OTHER"
        );

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .header("If-Match", etag)
                .body(request)
                .when()
                .put(baseUrl + "/users/f77cf369-337e-4bfd-bc85-aa7d63fa244b")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value());

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .header("If-Match", etag)
                .body(request2)
                .when()
                .put(baseUrl + "/users/f77cf369-337e-4bfd-bc85-aa7d63fa244b")
                .then()
                .assertThat()
                .statusCode(HttpStatus.PRECONDITION_FAILED.value());

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseUrl + "/users/user1")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("username", equalTo("user1"))
                .body("firstName", equalTo("newFirstName2"))
                .body("lastName", equalTo("newLastName2"))
                .body("phoneNumber", equalTo("+48222222222"))
                .body("email", equalTo("amama@mama.com"));
    }

    @Test
    @DataSet(value = "/dataset/users.json", strategy = SeedStrategy.REFRESH)
    public void updateOtherUser_WhenUserIsNotAdmin_ShouldReturnForbidden() {
        Response userResponse = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseUrl + "/users/user1")
                .then()
                .extract()
                .response();

        String etag = userResponse.getHeader("ETag");
        etag = etag.substring(1, etag.length() - 1);

        LoginRequest loginRequest = new LoginRequest("user3", "password", "pl");
        Response response = given()
                .contentType("application/json")
                .body(loginRequest)
                .when()
                .post(baseUrl + "/authenticate")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .response();

        String token2 = response.path("token");

        UpdateUserDataRequest request = new UpdateUserDataRequest(
                "newUsername",
                "newFirstName",
                "newLastName",
                "+48123456789",
                "dfskun@djnds.com",
                "MALE"
        );

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token2)
                .header("If-Match", etag)
                .body(request)
                .when()
                .put(baseUrl + "/users/f77cf369-337e-4bfd-bc85-aa7d63fa244b")
                .then()
                .assertThat()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    @DataSet(value = "/dataset/users.json", strategy = SeedStrategy.REFRESH)
    public void updateOtherUser_WhenIfMatchHeaderIsNotProvided_ShouldReturnBadRequest() {
        UpdateUserDataRequest request = new UpdateUserDataRequest(
                "newUsername",
                "newFirstName",
                "newLastName",
                "+48123456789",
                "dfskun@djnds.com",
                "MALE"
        );

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body(request)
                .when()
                .put(baseUrl + "/users/f77cf369-337e-4bfd-bc85-aa7d63fa244b")
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}
