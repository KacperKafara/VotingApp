package pl.kafara.voting.integration;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.SeedStrategy;
import dev.samstevens.totp.code.CodeGenerator;
import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.exceptions.CodeGenerationException;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import pl.kafara.voting.users.dto.LoginRequest;
import pl.kafara.voting.vote.dto.CreateUserVoteRequest;

import java.util.UUID;

import static io.restassured.RestAssured.given;

public class VoteSurveyIT extends IntegrationTestConfiguration {

    String key = "52VTQSSL4CSBF5NVCL6BHOX4BGQ7F43R";
    TimeProvider timeProvider = new SystemTimeProvider();
    CodeGenerator codeGenerator = new DefaultCodeGenerator();

    @Test
    @DataSet(value = "/dataset/users.json", strategy = SeedStrategy.REFRESH)
    public void voteSurvey_UserIsNotAVoter_ShouldReturn403() {
        CreateUserVoteRequest createUserVoteRequest = new CreateUserVoteRequest("123456", "YES");
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
                .body(createUserVoteRequest)
                .when()
                .post(baseUrl + "/surveys/5e642d0a-94d4-4a4f-8760-cd6d63cd1058/vote")
                .then()
                .assertThat()
                .statusCode(403);
    }

    @Test
    @DataSet(value = "/dataset/vote_survey.json", strategy = SeedStrategy.REFRESH)
    public void voteSurvey_WhenOTPIsNotValid_ShouldReturn400() {
        CreateUserVoteRequest createUserVoteRequest = new CreateUserVoteRequest("123456", "YES");
        CreateUserVoteRequest createUserVoteRequest2 = new CreateUserVoteRequest("12345", "YES");

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body(createUserVoteRequest)
                .when()
                .post(baseUrl + "/surveys/fcc88562-b6a0-4e0b-87d5-2e890cd2e37b/vote")
                .then()
                .assertThat()
                .statusCode(400);

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body(createUserVoteRequest2)
                .when()
                .post(baseUrl + "/surveys/fcc88562-b6a0-4e0b-87d5-2e890cd2e37b/vote")
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Test
    @DataSet(value = "/dataset/vote_survey.json", strategy = SeedStrategy.REFRESH)
    public void voteSurvey_WhenSurveyDoesNotExists_ShouldReturn404() throws CodeGenerationException {
        long timePeriod = timeProvider.getTime() / 30;
        String code = codeGenerator.generate(key, timePeriod);
        CreateUserVoteRequest createUserVoteRequest = new CreateUserVoteRequest(code, "YES");

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body(createUserVoteRequest)
                .when()
                .post(baseUrl + "/surveys/5e642d0a-94d4-4a4f-8760-cd6d63c55555/vote")
                .then()
                .assertThat()
                .statusCode(404);
    }

    @Test
    @DataSet(value = "/dataset/vote_survey.json", strategy = SeedStrategy.REFRESH)
    public void voteOtherSurvey_WhenEverythingIsOk_ShouldReturn200() throws CodeGenerationException {
        long timePeriod = timeProvider.getTime() / 30;
        String code = codeGenerator.generate(key, timePeriod);
        CreateUserVoteRequest createUserVoteRequest = new CreateUserVoteRequest(code, "YES");

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body(createUserVoteRequest)
                .when()
                .post(baseUrl + "/surveys/fcc88562-b6a0-4e0b-87d5-2e890cd2e37b/vote")
                .then()
                .assertThat()
                .statusCode(200);
    }

    @Test
    @DataSet(value = "/dataset/vote_survey.json", strategy = SeedStrategy.REFRESH)
    public void voteSurvey_WhenUserAlreadyVoted_ShouldReturn409() throws CodeGenerationException {
        long timePeriod = timeProvider.getTime() / 30;
        String code = codeGenerator.generate(key, timePeriod);
        CreateUserVoteRequest createUserVoteRequest = new CreateUserVoteRequest(code, "YES");

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body(createUserVoteRequest)
                .when()
                .post(baseUrl + "/surveys/fcc88562-b6a0-4e0b-87d5-2e890cd2e37d/vote")
                .then()
                .assertThat()
                .statusCode(200);

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body(createUserVoteRequest)
                .when()
                .post(baseUrl + "/surveys/fcc88562-b6a0-4e0b-87d5-2e890cd2e37d/vote")
                .then()
                .assertThat()
                .statusCode(409);
    }

    @Test
    @DataSet(value = "/dataset/vote_survey.json", strategy = SeedStrategy.REFRESH)
    public void voteParliamentaryClub_WhenUserPassInvalidVoteResult_ShouldReturn400() throws CodeGenerationException {
        long timePeriod = timeProvider.getTime() / 30;
        String code = codeGenerator.generate(key, timePeriod);
        CreateUserVoteRequest createUserVoteRequest = new CreateUserVoteRequest(code, "YES");

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body(createUserVoteRequest)
                .when()
                .post(baseUrl + "/surveys/fcc88562-b6a0-4e0b-87d5-2e890cd2e37c/vote")
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Test
    @DataSet(value = "/dataset/vote_survey.json", strategy = SeedStrategy.REFRESH)
    public void voteParliamentaryClub_WhenUserPassRandomValue_ShouldReturn404() throws CodeGenerationException {
        long timePeriod = timeProvider.getTime() / 30;
        String code = codeGenerator.generate(key, timePeriod);
        CreateUserVoteRequest createUserVoteRequest = new CreateUserVoteRequest(code, "random_value");

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body(createUserVoteRequest)
                .when()
                .post(baseUrl + "/surveys/fcc88562-b6a0-4e0b-87d5-2e890cd2e37c/vote")
                .then()
                .assertThat()
                .statusCode(404);
    }

    @Test
    @DataSet(value = "/dataset/vote_survey.json", strategy = SeedStrategy.REFRESH)
    public void voteOtherSurvey_PassUUID_ShouldReturn400() throws CodeGenerationException {
        long timePeriod = timeProvider.getTime() / 30;
        String code = codeGenerator.generate(key, timePeriod);
        CreateUserVoteRequest createUserVoteRequest = new CreateUserVoteRequest(code, UUID.randomUUID().toString());

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body(createUserVoteRequest)
                .when()
                .post(baseUrl + "/surveys/fcc88562-b6a0-4e0b-87d5-2e890cd2e37b/vote")
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Test
    @DataSet(value = "/dataset/vote_survey.json", strategy = SeedStrategy.REFRESH)
    public void voteParliamentaryClub_EverythingIsOk_ShouldReturn200() throws CodeGenerationException {
        long timePeriod = timeProvider.getTime() / 30;
        String code = codeGenerator.generate(key, timePeriod);
        CreateUserVoteRequest createUserVoteRequest = new CreateUserVoteRequest(code, "PC");

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body(createUserVoteRequest)
                .when()
                .post(baseUrl + "/surveys/fcc88562-b6a0-4e0b-87d5-2e890cd2e37c/vote")
                .then()
                .assertThat()
                .statusCode(200);
    }

}
