package pl.kafara.voting.integration;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.SeedStrategy;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import pl.kafara.voting.users.dto.RoleRequest;

import java.util.UUID;

import static io.restassured.RestAssured.given;

public class RoleIT extends IntegrationTestConfiguration {

    @Test
    @DataSet(value = "/dataset/users.json")
    public void addRole_WhenUserNotFound_Return404() {
        RoleRequest roleRequest = new RoleRequest(UUID.randomUUID(), "USER");
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .body(roleRequest)
                .put(baseUrl + "/users/role")
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DataSet(value = "/dataset/users.json", strategy = SeedStrategy.REFRESH)
    public void addRole_WhenUserFound_Return200() {
        RoleRequest roleRequest = new RoleRequest(UUID.fromString("f77cf369-337e-4bfd-bc85-aa7d63fa244b"), "MODERATOR");
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .body(roleRequest)
                .put(baseUrl + "/users/role")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DataSet(value = "/dataset/users.json", strategy = SeedStrategy.REFRESH)
    public void addRole_WhenRoleAlreadyExists_Return200() {
        RoleRequest roleRequest = new RoleRequest(UUID.fromString("f77cf369-337e-4bfd-bc85-aa7d63fa244b"), "USER");
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .body(roleRequest)
                .put(baseUrl + "/users/role")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DataSet(value = "/dataset/users.json", strategy = SeedStrategy.REFRESH)
    public void deleteRole_WhenUserNotFound_Return404() {
        RoleRequest roleRequest = new RoleRequest(UUID.randomUUID(), "USER");
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .body(roleRequest)
                .delete(baseUrl + "/users/role")
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DataSet(value = "/dataset/users.json", strategy = SeedStrategy.REFRESH)
    public void deleteRole_WhenRoleIsNotValid_Return400() {
        RoleRequest roleRequest = new RoleRequest(UUID.fromString("f77cf369-337e-4bfd-bc85-aa7d63fa244b"), "ADMIN");
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .body(roleRequest)
                .delete(baseUrl + "/users/role")
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DataSet(value = "/dataset/users.json", strategy = SeedStrategy.REFRESH)
    public void deleteRole_WhenRoleNotAssignedToUser_Return200() {
        RoleRequest roleRequest = new RoleRequest(UUID.fromString("f77cf369-337e-4bfd-bc85-aa7d63fa244b"), "ADMINISTRATOR");
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .body(roleRequest)
                .delete(baseUrl + "/users/role")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DataSet(value = "/dataset/users.json", strategy = SeedStrategy.REFRESH)
    public void deleteRole_WhenItsLastRole_Return400() {
        RoleRequest roleRequest = new RoleRequest(UUID.fromString("f77cf369-337e-4bfd-bc85-aa7d63fa244b"), "USER");
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .body(roleRequest)
                .delete(baseUrl + "/users/role")
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}
