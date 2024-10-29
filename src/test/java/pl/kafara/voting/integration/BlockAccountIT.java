package pl.kafara.voting.integration;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.SeedStrategy;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class BlockAccountIT extends IntegrationTestConfiguration {

    @Test
    @DataSet(value = "/dataset/users.json", strategy = SeedStrategy.REFRESH)
    public void blockAccount_WhenUserNotFound_Return404() {
        UUID userId = UUID.randomUUID();
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

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .header("If-Match", etag)
                .when()
                .put(baseUrl + "/users/" + userId + "/block")
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DataSet(value = "/dataset/users.json", strategy = SeedStrategy.REFRESH)
    public void blockAccount_WhenUserIsBlocked_Return200() {
        UUID userId = UUID.fromString("f77cf369-337e-4bfd-bc85-aa7d63fa244c");
        Response response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseUrl + "/users/user2")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .response();

        String etag = response.getHeader("ETag");
        etag = etag.substring(1, etag.length() - 1);

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .header("If-Match", etag)
                .when()
                .put(baseUrl + "/users/" + userId + "/block")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value());

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseUrl + "/users/user2")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("blocked", equalTo(true));
    }

    @Test
    @DataSet(value = "/dataset/users.json", strategy = SeedStrategy.REFRESH)
    public void blockAccount_WhenUserIsNotBlocked_Return200() {
        UUID userId = UUID.fromString("f77cf369-337e-4bfd-bc85-aa7d63fa244b");

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

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .header("If-Match", etag)
                .when()
                .put(baseUrl + "/users/" + userId + "/block")
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
                .body("blocked", equalTo(true));
    }

    @Test
    @DataSet(value = "/dataset/users.json", strategy = SeedStrategy.REFRESH)
    public void blockAccount_WhenUserIsYourself_Return400() {
        UUID userId = UUID.fromString("5e642d0a-94d4-4a4f-8760-cd6d63cd1038");

        Response response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseUrl + "/users/user")
                .then()
                .extract()
                .response();

        String etag = response.getHeader("ETag");
        etag = etag.substring(1, etag.length() - 1);

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .header("If-Match", etag)
                .when()
                .put(baseUrl + "/users/" + userId + "/block")
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseUrl + "/users/user")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("blocked", equalTo(false));
    }

    @Test
    @DataSet(value = "/dataset/users.json", strategy = SeedStrategy.REFRESH)
    public void unblockAccount_WhenUserNotFound_Return404() {
        UUID userId = UUID.randomUUID();

        Response response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseUrl + "/users/user")
                .then()
                .extract()
                .response();

        String etag = response.getHeader("ETag");
        etag = etag.substring(1, etag.length() - 1);

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .header("If-Match", etag)
                .when()
                .delete(baseUrl + "/users/" + userId + "/block")
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DataSet(value = "/dataset/users.json", strategy = SeedStrategy.REFRESH)
    public void unblockAccount_WhenUserIsNotBlocked_Return200() {
        UUID userId = UUID.fromString("f77cf369-337e-4bfd-bc85-aa7d63fa244b");

        Response response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseUrl + "/users/user1")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .response();

        String etag = response.getHeader("ETag");
        etag = etag.substring(1, etag.length() - 1);

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .header("If-Match", etag)
                .when()
                .delete(baseUrl + "/users/" + userId + "/block")
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
                .body("blocked", equalTo(false));
    }

    @Test
    @DataSet(value = "/dataset/users.json", strategy = SeedStrategy.REFRESH)
    public void unblockAccount_WhenUserIsBlocked_Return200() {
        UUID userId = UUID.fromString("f77cf369-337e-4bfd-bc85-aa7d63fa244c");

        Response response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseUrl + "/users/user2")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .response();

        String etag = response.getHeader("ETag");
        etag = etag.substring(1, etag.length() - 1);

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .header("If-Match", etag)
                .when()
                .delete(baseUrl + "/users/" + userId + "/block")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value());

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseUrl + "/users/user2")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("blocked", equalTo(false));
    }

    @Test
    @DataSet(value = "/dataset/users.json", strategy = SeedStrategy.REFRESH)
    public void unblockAccount_WhenUserIsYourself_Return400() {
        UUID userId = UUID.fromString("5e642d0a-94d4-4a4f-8760-cd6d63cd1038");

        Response response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseUrl + "/users/user")
                .then()
                .extract()
                .response();

        String etag = response.getHeader("ETag");
        etag = etag.substring(1, etag.length() - 1);

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .header("If-Match", etag)
                .when()
                .delete(baseUrl + "/users/" + userId + "/block")
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseUrl + "/users/user")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("blocked", equalTo(false));
    }

    @Test
    @DataSet(value = "/dataset/users.json", strategy = SeedStrategy.REFRESH)
    public void blockAccount_WhenOptimisticLock_Return412() {
        UUID userId = UUID.fromString("f77cf369-337e-4bfd-bc85-aa7d63fa244b");

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

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .header("If-Match", etag)
                .when()
                .put(baseUrl + "/users/" + userId + "/block")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value());

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .header("If-Match", etag)
                .when()
                .delete(baseUrl + "/users/" + userId + "/block")
                .then()
                .assertThat()
                .statusCode(HttpStatus.PRECONDITION_FAILED.value());
    }
}
