package pl.kafara.voting.integration;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.SeedStrategy;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import pl.kafara.voting.model.users.UserRoleEnum;
import pl.kafara.voting.users.dto.RoleRequest;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ModifyRolesIT extends IntegrationTestConfiguration {

    @Test
    @DataSet(value = "/dataset/users.json", strategy = SeedStrategy.REFRESH)
    public void modifyRoles_WhenUserIsNotAuthenticated_Return403() {
        RoleRequest roleRequest = new RoleRequest(Set.of(UserRoleEnum.ADMINISTRATOR));

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
                .when()
                .body(roleRequest)
                .header("If-Match", etag)
                .put(baseUrl + "/users/"+ UUID.randomUUID() +"/roles")
                .then()
                .assertThat()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    @DataSet(value = "/dataset/users.json", strategy = SeedStrategy.REFRESH)
    public void modifyRoles_WhenUserIsNotFound_Return404() {
        RoleRequest roleRequest = new RoleRequest(Set.of(UserRoleEnum.ADMINISTRATOR));
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
                .when()
                .header("Authorization", "Bearer " + token)
                .header("If-Match", etag)
                .body(roleRequest)
                .put(baseUrl + "/users/"+ UUID.randomUUID() +"/roles")
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DataSet(value = "/dataset/users.json", strategy = SeedStrategy.REFRESH)
    public void modifyRoles_WhenUserIsYourself_Return400() {
        RoleRequest roleRequest = new RoleRequest(Set.of(UserRoleEnum.ADMINISTRATOR));

        Response response = given()
                .contentType("application/json")
                .when()
                .header("Authorization", "Bearer " + token)
                .get(baseUrl + "/users/user")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .response();

        String etag = response.getHeader("ETag");
        etag = etag.substring(1, etag.length() - 1);

        Set<String> roles = new HashSet<>(response.jsonPath().getList("roles", String.class));

        given()
                .contentType("application/json")
                .when()
                .header("Authorization", "Bearer " + token)
                .header("If-Match", etag)
                .body(roleRequest)
                .put(baseUrl + "/users/5e642d0a-94d4-4a4f-8760-cd6d63cd1038/roles")
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());

        Set<String> newRoles = new HashSet<>(given()
                .contentType("application/json")
                .when()
                .header("Authorization", "Bearer " + token)
                .get(baseUrl + "/users/user")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath()
                .getList("roles", String.class));

        assertEquals(roles, newRoles);
    }

    @Test
    @DataSet(value = "/dataset/users.json", strategy = SeedStrategy.REFRESH)
    public void modifyRoles_WhenRolesAreEmpty_Return400() {
        RoleRequest roleRequest = new RoleRequest(Set.of());

        Response response = given()
                .contentType("application/json")
                .when()
                .header("Authorization", "Bearer " + token)
                .get(baseUrl + "/users/user1")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .response();

        String etag = response.getHeader("ETag");
        etag = etag.substring(1, etag.length() - 1);

        Set<String> roles = new HashSet<>(response.jsonPath().getList("roles", String.class));

        given()
                .contentType("application/json")
                .when()
                .header("Authorization", "Bearer " + token)
                .header("If-Match", etag)
                .body(roleRequest)
                .put(baseUrl + "/users/f77cf369-337e-4bfd-bc85-aa7d63fa244b/roles")
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());

        Set<String> newRoles = new HashSet<>(given()
                .contentType("application/json")
                .when()
                .header("Authorization", "Bearer " + token)
                .get(baseUrl + "/users/user1")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath()
                .getList("roles", String.class));

        assertEquals(roles, newRoles);
    }

    @Test
    @DataSet(value = "/dataset/users.json", strategy = SeedStrategy.REFRESH)
    public void modifyRoles_WhenRolesAreCorrect_Return200() {
        RoleRequest roleRequest = new RoleRequest(Set.of(UserRoleEnum.ADMINISTRATOR, UserRoleEnum.USER));

        Response response = given()
                .contentType("application/json")
                .when()
                .header("Authorization", "Bearer " + token)
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
                .when()
                .header("Authorization", "Bearer " + token)
                .header("If-Match", etag)
                .body(roleRequest)
                .put(baseUrl + "/users/f77cf369-337e-4bfd-bc85-aa7d63fa244b/roles")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value());

        Set<String> newRoles = new HashSet<>(given()
                .contentType("application/json")
                .when()
                .header("Authorization", "Bearer " + token)
                .get(baseUrl + "/users/user1")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath()
                .getList("roles", String.class));

        assertEquals(Set.of(UserRoleEnum.ADMINISTRATOR.name(), UserRoleEnum.USER.name()), newRoles);
    }

    @Test
    @DataSet(value = "/dataset/users.json", strategy = SeedStrategy.REFRESH)
    public void modifyRoles_WhenUserIsVoterAndRequestRemoveUserRole_Return200WithoutVoterRole() {
        RoleRequest roleRequest = new RoleRequest(Set.of(UserRoleEnum.ADMINISTRATOR));

        Response response = given()
                .contentType("application/json")
                .when()
                .header("Authorization", "Bearer " + token)
                .get(baseUrl + "/users/user3")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .response();

        String etag = response.getHeader("ETag");
        etag = etag.substring(1, etag.length() - 1);

        given()
                .contentType("application/json")
                .when()
                .header("Authorization", "Bearer " + token)
                .header("If-Match", etag)
                .body(roleRequest)
                .put(baseUrl + "/users/f77cf369-337e-4bfd-bc85-aa7d63fa244d/roles")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value());

        Set<String> newRoles = new HashSet<>(given()
                .contentType("application/json")
                .when()
                .header("Authorization", "Bearer " + token)
                .get(baseUrl + "/users/user3")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath()
                .getList("roles", String.class));

        assertEquals(Set.of(UserRoleEnum.ADMINISTRATOR.name()), newRoles);
    }

    @Test
    @DataSet(value = "/dataset/users.json", strategy = SeedStrategy.REFRESH)
    public void modifyRoles_WhenUserIsVoterAndRequestDontRemoveUserRole_Return200WithVoterRole() {
        RoleRequest roleRequest = new RoleRequest(Set.of(UserRoleEnum.ADMINISTRATOR, UserRoleEnum.USER));

        Response response = given()
                .contentType("application/json")
                .when()
                .header("Authorization", "Bearer " + token)
                .get(baseUrl + "/users/user3")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .response();

        String etag = response.getHeader("ETag");
        etag = etag.substring(1, etag.length() - 1);

        given()
                .contentType("application/json")
                .when()
                .header("Authorization", "Bearer " + token)
                .header("If-Match", etag)
                .body(roleRequest)
                .put(baseUrl + "/users/f77cf369-337e-4bfd-bc85-aa7d63fa244d/roles")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value());

        Set<String> newRoles = new HashSet<>(given()
                .contentType("application/json")
                .when()
                .header("Authorization", "Bearer " + token)
                .get(baseUrl + "/users/user3")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath()
                .getList("roles", String.class));

        assertEquals(Set.of(UserRoleEnum.ADMINISTRATOR.name(), UserRoleEnum.USER.name(), UserRoleEnum.VOTER.name()), newRoles);
    }
}
