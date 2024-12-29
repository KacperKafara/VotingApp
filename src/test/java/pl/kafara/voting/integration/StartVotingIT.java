package pl.kafara.voting.integration;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.SeedStrategy;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import pl.kafara.voting.vote.dto.ActivateVotingRequest;

import java.time.LocalDateTime;

import static io.restassured.RestAssured.given;

public class StartVotingIT extends IntegrationTestConfiguration {

    @Test
    @DataSet(value = "dataset/vote_voting.json", strategy = SeedStrategy.REFRESH)
    public void startVoting_EverythingIsOk_ShouldReturn200() {
        Response response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseUrl + "/votings/757847a7-58eb-4930-8351-2301c54d7918/details")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .response();

        String etag = response.getHeader("ETag");
        etag = etag.substring(1, etag.length() - 1);

        ActivateVotingRequest activateVotingRequest = new ActivateVotingRequest(LocalDateTime.now().plusDays(1));

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .header("If-Match", etag)
                .when()
                .body(activateVotingRequest)
                .patch(baseUrl + "/votings/757847a7-58eb-4930-8351-2301c54d7918")
                .then()
                .assertThat()
                .statusCode(200);
    }

    @Test
    @DataSet(value = "dataset/vote_voting.json", strategy = SeedStrategy.REFRESH)
    public void startVoting_DateInPast_ShouldReturn400() {
        Response response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseUrl + "/votings/757847a7-58eb-4930-8351-2301c54d7918/details")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .response();

        String etag = response.getHeader("ETag");
        etag = etag.substring(1, etag.length() - 1);

        ActivateVotingRequest activateVotingRequest = new ActivateVotingRequest(LocalDateTime.now().minusDays(1));

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .header("If-Match", etag)
                .when()
                .body(activateVotingRequest)
                .patch(baseUrl + "/votings/757847a7-58eb-4930-8351-2301c54d7918")
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Test
    @DataSet(value = "dataset/vote_voting.json", strategy = SeedStrategy.REFRESH)
    public void startVoting_DateIsNull_ShouldReturn400() {
        Response response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseUrl + "/votings/757847a7-58eb-4930-8351-2301c54d7918/details")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .response();

        String etag = response.getHeader("ETag");
        etag = etag.substring(1, etag.length() - 1);

        ActivateVotingRequest activateVotingRequest = new ActivateVotingRequest(null);

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .header("If-Match", etag)
                .when()
                .body(activateVotingRequest)
                .patch(baseUrl + "/votings/757847a7-58eb-4930-8351-2301c54d7918")
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Test
    @DataSet(value = "dataset/vote_voting.json", strategy = SeedStrategy.REFRESH)
    public void startVoting_EtagIsNull_ShouldReturn400() {
        ActivateVotingRequest activateVotingRequest = new ActivateVotingRequest(LocalDateTime.now().plusDays(1));

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .body(activateVotingRequest)
                .patch(baseUrl + "/votings/757847a7-58eb-4930-8351-2301c54d7918")
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Test
    @DataSet(value = "dataset/vote_voting.json", strategy = SeedStrategy.REFRESH)
    public void startVoting_ConcurentOperation_ShouldReturn200Then412() {
        Response response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseUrl + "/votings/757847a7-58eb-4930-8351-2301c54d7918/details")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .response();

        String etag = response.getHeader("ETag");
        etag = etag.substring(1, etag.length() - 1);

        ActivateVotingRequest activateVotingRequest1 = new ActivateVotingRequest(LocalDateTime.now().plusDays(1));
        ActivateVotingRequest activateVotingRequest2 = new ActivateVotingRequest(LocalDateTime.now().plusDays(2));

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .header("If-Match", etag)
                .when()
                .body(activateVotingRequest1)
                .patch(baseUrl + "/votings/757847a7-58eb-4930-8351-2301c54d7918")
                .then()
                .assertThat()
                .statusCode(200);

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .header("If-Match", etag)
                .when()
                .body(activateVotingRequest2)
                .patch(baseUrl + "/votings/757847a7-58eb-4930-8351-2301c54d7918")
                .then()
                .assertThat()
                .statusCode(412);
    }

    @Test
    @DataSet(value = "dataset/vote_voting.json", strategy = SeedStrategy.REFRESH)
    public void startVoting_VotingAlreadyStarted_ShouldReturn400() {
        Response response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseUrl + "/votings/757847a7-58eb-4930-8351-2301c54d7916/details")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .response();

        String etag = response.getHeader("ETag");
        etag = etag.substring(1, etag.length() - 1);

        ActivateVotingRequest activateVotingRequest = new ActivateVotingRequest(LocalDateTime.now().plusDays(1));

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .header("If-Match", etag)
                .when()
                .body(activateVotingRequest)
                .patch(baseUrl + "/votings/757847a7-58eb-4930-8351-2301c54d7916")
                .then()
                .assertThat()
                .statusCode(400);
    }
}
