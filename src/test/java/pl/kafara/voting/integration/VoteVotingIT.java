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

public class VoteVotingIT extends IntegrationTestConfiguration {
    String key = "52VTQSSL4CSBF5NVCL6BHOX4BGQ7F43R";
    TimeProvider timeProvider = new SystemTimeProvider();
    CodeGenerator codeGenerator = new DefaultCodeGenerator();

    @Test
    @DataSet(value = "/dataset/users.json", strategy = SeedStrategy.REFRESH)
    public void voteVoting_UserIsNotAVoter_ShouldReturn403() {
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
                .post(baseUrl + "/votings/5e642d0a-94d4-4a4f-8760-cd6d63cd1058/vote")
                .then()
                .assertThat()
                .statusCode(403);
    }

    @Test
    @DataSet(value = "/dataset/vote_voting.json", strategy = SeedStrategy.REFRESH)
    public void voteVoting_WhenOTPIsNotValid_ShouldReturn400() {
        CreateUserVoteRequest createUserVoteRequest = new CreateUserVoteRequest("123456", "YES");
        CreateUserVoteRequest createUserVoteRequest2 = new CreateUserVoteRequest("12345", "YES");

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body(createUserVoteRequest)
                .when()
                .post(baseUrl + "/votings/757847a7-58eb-4930-8351-2301c54d7914/vote")
                .then()
                .assertThat()
                .statusCode(400);

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body(createUserVoteRequest2)
                .when()
                .post(baseUrl + "/votings/757847a7-58eb-4930-8351-2301c54d7915/vote")
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Test
    public void voteVoting_VotingDoesNotExist_ShouldReturn404() throws CodeGenerationException {
        long timePeriod = timeProvider.getTime() / 30;
        String code = codeGenerator.generate(key, timePeriod);
        CreateUserVoteRequest createUserVoteRequest = new CreateUserVoteRequest(code, "YES");

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body(createUserVoteRequest)
                .when()
                .post(baseUrl + "/votings/" + UUID.randomUUID() + "/vote")
                .then()
                .assertThat()
                .statusCode(404);
    }

    @Test
    @DataSet(value = "/dataset/vote_voting.json", strategy = SeedStrategy.REFRESH)
    public void voteVoting_WhenUserHasAlreadyVoted_ShouldReturn409() throws CodeGenerationException {
        long timePeriod = timeProvider.getTime() / 30;
        String code = codeGenerator.generate(key, timePeriod);
        CreateUserVoteRequest createUserVoteRequest = new CreateUserVoteRequest(code, "YES");

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body(createUserVoteRequest)
                .when()
                .post(baseUrl + "/votings/757847a7-58eb-4930-8351-2301c54d7914/vote")
                .then()
                .assertThat()
                .statusCode(200);

        long timePeriod2 = timeProvider.getTime() / 30;
        String code2 = codeGenerator.generate(key, timePeriod2);
        CreateUserVoteRequest createUserVoteRequest2 = new CreateUserVoteRequest(code2, "NO");

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body(createUserVoteRequest2)
                .when()
                .post(baseUrl + "/votings/757847a7-58eb-4930-8351-2301c54d7914/vote")
                .then()
                .assertThat()
                .statusCode(409);
    }

    @Test
    @DataSet(value = "/dataset/vote_voting.json", strategy = SeedStrategy.REFRESH)
    public void voteVoting_WhenEverythingIsOk_ShouldReturn200() throws CodeGenerationException {
        long timePeriod = timeProvider.getTime() / 30;
        String code = codeGenerator.generate(key, timePeriod);
        CreateUserVoteRequest createUserVoteRequest = new CreateUserVoteRequest(code, "YES");

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body(createUserVoteRequest)
                .when()
                .post(baseUrl + "/votings/757847a7-58eb-4930-8351-2301c54d7915/vote")
                .then()
                .assertThat()
                .statusCode(200);
    }

    @Test
    @DataSet(value = "/dataset/vote_voting.json", strategy = SeedStrategy.REFRESH)
    public void voteNotOnList_UserPassInvalidVoteResult_ShouldReturn400() throws CodeGenerationException {
        long timePeriod = timeProvider.getTime() / 30;
        String code = codeGenerator.generate(key, timePeriod);
        CreateUserVoteRequest createUserVoteRequest = new CreateUserVoteRequest(code, "wrong_data");

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body(createUserVoteRequest)
                .when()
                .post(baseUrl + "/votings/757847a7-58eb-4930-8351-2301c54d7914/vote")
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Test
    @DataSet(value = "/dataset/vote_voting.json", strategy = SeedStrategy.REFRESH)
    public void voteVoting_WhenUserIsNotAuthenticated_ShouldReturn403() throws CodeGenerationException {
        long timePeriod = timeProvider.getTime() / 30;
        String code = codeGenerator.generate(key, timePeriod);
        CreateUserVoteRequest createUserVoteRequest = new CreateUserVoteRequest(code, "YES");

        given()
                .contentType("application/json")
                .body(createUserVoteRequest)
                .when()
                .post(baseUrl + "/votings/757847a7-58eb-4930-8351-2301c54d7915/vote")
                .then()
                .assertThat()
                .statusCode(403);
    }

    @Test
    @DataSet(value = "/dataset/vote_voting.json", strategy = SeedStrategy.REFRESH)
    public void voteOnList_WhenVotingOptionDoesNotExists_ShouldReturn404() throws CodeGenerationException {
        long timePeriod = timeProvider.getTime() / 30;
        String code = codeGenerator.generate(key, timePeriod);
        CreateUserVoteRequest createUserVoteRequest = new CreateUserVoteRequest(code, "757847a7-58eb-4930-8351-2301c54d7933");

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body(createUserVoteRequest)
                .when()
                .post(baseUrl + "/votings/757847a7-58eb-4930-8351-2301c54d7917/vote")
                .then()
                .assertThat()
                .statusCode(404);
    }

    @Test
    @DataSet(value = "/dataset/vote_voting.json", strategy = SeedStrategy.REFRESH)
    public void voteOnList_WhenVotingOptionIsNotOnList_ShouldReturn404() throws CodeGenerationException {
        long timePeriod = timeProvider.getTime() / 30;
        String code = codeGenerator.generate(key, timePeriod);
        CreateUserVoteRequest createUserVoteRequest = new CreateUserVoteRequest(code, "757847a7-58eb-4930-8351-2301c54d792");

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body(createUserVoteRequest)
                .when()
                .post(baseUrl + "/votings/757847a7-58eb-4930-8351-2301c54d7917/vote")
                .then()
                .assertThat()
                .statusCode(404);
    }

    @Test
    @DataSet(value = "/dataset/vote_voting.json", strategy = SeedStrategy.REFRESH)
    public void voteOnList_WhenVotingIsNotOnList_ShouldReturn400() throws CodeGenerationException {
        long timePeriod = timeProvider.getTime() / 30;
        String code = codeGenerator.generate(key, timePeriod);
        CreateUserVoteRequest createUserVoteRequest = new CreateUserVoteRequest(code, "757847a7-58eb-4930-8351-2301c54d792");

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body(createUserVoteRequest)
                .when()
                .post(baseUrl + "/votings/757847a7-58eb-4930-8351-2301c54d7914/vote")
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Test
    @DataSet(value = "/dataset/vote_voting.json", strategy = SeedStrategy.REFRESH)
    public void voteVoting_WhenVotingExpire_ShouldReturn400() throws CodeGenerationException {
        long timePeriod = timeProvider.getTime() / 30;
        String code = codeGenerator.generate(key, timePeriod);
        CreateUserVoteRequest createUserVoteRequest = new CreateUserVoteRequest(code, "YES");

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body(createUserVoteRequest)
                .when()
                .post(baseUrl + "/votings/757847a7-58eb-4930-8351-2301c54d7916/vote")
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Test
    @DataSet(value = "/dataset/vote_voting.json", strategy = SeedStrategy.REFRESH)
    public void voteVoting_WhenResultIsNull_ShouldReturn400() throws CodeGenerationException {
        long timePeriod = timeProvider.getTime() / 30;
        String code = codeGenerator.generate(key, timePeriod);
        CreateUserVoteRequest createUserVoteRequest = new CreateUserVoteRequest(code, null);

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body(createUserVoteRequest)
                .when()
                .post(baseUrl + "/votings/757847a7-58eb-4930-8351-2301c54d7915/vote")
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Test
    @DataSet(value = "/dataset/vote_voting.json", strategy = SeedStrategy.REFRESH)
    public void voteVoting_WhenResultIsEmpty_ShouldReturn400() throws CodeGenerationException {
        long timePeriod = timeProvider.getTime() / 30;
        String code = codeGenerator.generate(key, timePeriod);
        CreateUserVoteRequest createUserVoteRequest = new CreateUserVoteRequest(code, "");

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body(createUserVoteRequest)
                .when()
                .post(baseUrl + "/votings/757847a7-58eb-4930-8351-2301c54d7915/vote")
                .then()
                .assertThat()
                .statusCode(400);
    }
}
