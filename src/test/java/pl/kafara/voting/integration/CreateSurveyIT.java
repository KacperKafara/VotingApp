package pl.kafara.voting.integration;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.SeedStrategy;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import pl.kafara.voting.model.vote.survey.SurveyKind;
import pl.kafara.voting.users.dto.LoginRequest;
import pl.kafara.voting.vote.dto.CreateSurveyRequest;

import java.time.LocalDateTime;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreateSurveyIT extends IntegrationTestConfiguration {

    @Test
    public void createSurvey_UserIsNotAuthenticated_ShouldReturn403() {
        CreateSurveyRequest createSurveyRequest = new CreateSurveyRequest(
                "Title of survey",
                "Description of survey",
                LocalDateTime.now(),
                SurveyKind.OTHER
        );

        Response responseBefore = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseUrl + "/surveys")
                .then()
                .extract()
                .response();
        int surveysBefore = responseBefore.jsonPath().getList("surveys").size();

        given()
                .contentType("application/json")
                .body(createSurveyRequest)
                .when()
                .post(baseUrl + "/surveys")
                .then()
                .assertThat()
                .statusCode(HttpStatus.FORBIDDEN.value());

        Response responseAfter = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseUrl + "/surveys")
                .then()
                .extract()
                .response();
        int surveysAfter = responseAfter.jsonPath().getList("surveys").size();
        assertEquals(surveysBefore, surveysAfter);
    }

    @Test
    @DataSet(value = "/dataset/users.json", strategy = SeedStrategy.REFRESH)
    public void createSurvey_WhenUserIsNotModerator_Return403() {
        CreateSurveyRequest createSurveyRequest = new CreateSurveyRequest(
                "Title of survey",
                "Description of survey",
                LocalDateTime.now(),
                SurveyKind.OTHER
        );
        LoginRequest loginRequest = new LoginRequest("user1", "password", "pl");
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
        String jwt = response.jsonPath().getString("token");

        Response responseBefore = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseUrl + "/surveys")
                .then()
                .extract()
                .response();
        int surveysBefore = responseBefore.jsonPath().getList("surveys").size();

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + jwt)
                .body(createSurveyRequest)
                .when()
                .post(baseUrl + "/surveys")
                .then()
                .assertThat()
                .statusCode(HttpStatus.FORBIDDEN.value());

        Response responseAfter = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseUrl + "/surveys")
                .then()
                .extract()
                .response();
        int surveysAfter = responseAfter.jsonPath().getList("surveys").size();
        assertEquals(surveysBefore, surveysAfter);
    }

    @Test
    public void createSurvey_WhenEndDateIsInThePast_Return400() {
        CreateSurveyRequest createSurveyRequest = new CreateSurveyRequest(
                "Title of survey",
                "Description of survey",
                LocalDateTime.now().minusDays(1),
                SurveyKind.OTHER
        );

        Response responseBefore = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseUrl + "/surveys")
                .then()
                .extract()
                .response();
        int surveysBefore = responseBefore.jsonPath().getList("surveys").size();

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body(createSurveyRequest)
                .when()
                .post(baseUrl + "/surveys")
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());

        Response responseAfter = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseUrl + "/surveys")
                .then()
                .extract()
                .response();
        int surveysAfter = responseAfter.jsonPath().getList("surveys").size();
        assertEquals(surveysBefore, surveysAfter);
    }

    @Test
    public void createSurvey_WhenTitleIsToShort_Return400() {
        CreateSurveyRequest createSurveyRequest = new CreateSurveyRequest(
                "Ti",
                "Description of survey",
                LocalDateTime.now().plusDays(1),
                SurveyKind.OTHER
        );

        Response responseBefore = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseUrl + "/surveys")
                .then()
                .extract()
                .response();
        int surveysBefore = responseBefore.jsonPath().getList("surveys").size();

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body(createSurveyRequest)
                .when()
                .post(baseUrl + "/surveys")
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());

        Response responseAfter = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseUrl + "/surveys")
                .then()
                .extract()
                .response();
        int surveysAfter = responseAfter.jsonPath().getList("surveys").size();
        assertEquals(surveysBefore, surveysAfter);
    }

    @Test
    public void createSurvey_WhenDescriptionIsToShort_return400() {
        CreateSurveyRequest createSurveyRequest = new CreateSurveyRequest(
                "Title of survey",
                "De",
                LocalDateTime.now().plusDays(1),
                SurveyKind.OTHER
        );

        Response responseBefore = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseUrl + "/surveys")
                .then()
                .extract()
                .response();
        int surveysBefore = responseBefore.jsonPath().getList("surveys").size();

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body(createSurveyRequest)
                .when()
                .post(baseUrl + "/surveys")
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());

        Response responseAfter = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseUrl + "/surveys")
                .then()
                .extract()
                .response();
        int surveysAfter = responseAfter.jsonPath().getList("surveys").size();
        assertEquals(surveysBefore, surveysAfter);
    }

    @Test
    public void createSurvey_WhenKindIsNull_Return400() {
        CreateSurveyRequest createSurveyRequest = new CreateSurveyRequest(
                "Title of survey",
                "Description of survey",
                LocalDateTime.now().plusDays(1),
                null
        );

        Response responseBefore = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseUrl + "/surveys")
                .then()
                .extract()
                .response();
        int surveysBefore = responseBefore.jsonPath().getList("surveys").size();

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body(createSurveyRequest)
                .when()
                .post(baseUrl + "/surveys")
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());

        Response responseAfter = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseUrl + "/surveys")
                .then()
                .extract()
                .response();
        int surveysAfter = responseAfter.jsonPath().getList("surveys").size();
        assertEquals(surveysBefore, surveysAfter);
    }

    @Test
    public void createSurvey_WhenSurveyWithGivenTitleExists_Return409() {
        CreateSurveyRequest createSurveyRequest = new CreateSurveyRequest(
                "Survey 1",
                "Description of survey",
                LocalDateTime.now().plusDays(1),
                SurveyKind.OTHER
        );

        Response responseBefore = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseUrl + "/surveys")
                .then()
                .extract()
                .response();
        int surveysBefore = responseBefore.jsonPath().getList("surveys").size();

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body(createSurveyRequest)
                .when()
                .post(baseUrl + "/surveys")
                .then()
                .assertThat()
                .statusCode(HttpStatus.CONFLICT.value());

        Response responseAfter = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseUrl + "/surveys")
                .then()
                .extract()
                .response();
        int surveysAfter = responseAfter.jsonPath().getList("surveys").size();
        assertEquals(surveysBefore, surveysAfter);
    }

    @Test
    public void createSurvey_WhenEverythingGoesOk_Return200() {
        CreateSurveyRequest createSurveyRequest = new CreateSurveyRequest(
                "newly created survey",
                "Description of survey",
                LocalDateTime.now().plusDays(1),
                SurveyKind.OTHER
        );

        Response responseBefore = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseUrl + "/surveys")
                .then()
                .extract()
                .response();

        int surveysBefore = responseBefore.jsonPath().getList("surveys").size();

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body(createSurveyRequest)
                .when()
                .post(baseUrl + "/surveys")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value());

        Response responseAfter = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseUrl + "/surveys")
                .then()
                .extract()
                .response();

        int surveysAfter = responseAfter.jsonPath().getList("surveys").size();

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseUrl + "/surveys/latest")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("title", equalTo("newly created survey"));

        assertEquals(surveysBefore + 1, surveysAfter);
    }
}
