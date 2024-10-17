package pl.kafara.voting.integration;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.connection.ConnectionHolder;
import com.github.database.rider.junit5.DBUnitExtension;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;
import pl.kafara.voting.users.dto.LoginRequest;

import java.nio.file.Paths;
import java.sql.DriverManager;

import static io.restassured.RestAssured.given;

@Testcontainers
@DBUnit(schema = "public", caseInsensitiveStrategy = Orthography.LOWERCASE)
@ExtendWith(DBUnitExtension.class)
public abstract class IntegrationTestConfiguration {
    static final Network network = Network.newNetwork();
    public static String token;
    protected static String baseUrl;

    protected static ConnectionHolder connectionHolder;

    static MountableFile jar = MountableFile
            .forHostPath(Paths.get("target/votingApp.jar").toAbsolutePath());

    @Container
    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.4-alpine3.20")
            .withNetwork(network)
            .withNetworkAliases("testDatabase")
            .withExposedPorts(5432)
            .withUsername("postgres")
            .withPassword("postgres")
            .withDatabaseName("voting")
            .waitingFor(Wait.defaultWaitStrategy());

    @Container
    static final GenericContainer<?> redis = new GenericContainer<>("redis:7.4-alpine3.20")
            .withNetworkAliases("redis")
            .withNetwork(network)
            .withExposedPorts(6379)
            .waitingFor(Wait.defaultWaitStrategy());

    @Container
    static final GenericContainer<?> mail = new GenericContainer<>("rnwood/smtp4dev:3.6.0")
            .withNetworkAliases("mail")
            .withNetwork(network)
            .withExposedPorts(25)
            .waitingFor(Wait.defaultWaitStrategy());

    @Container
    static final GenericContainer<?> application = new GenericContainer<>("eclipse-temurin:21.0.2_13-jdk")
            .withNetwork(network)
            .withExposedPorts(8080)
            .withLogConsumer(outputFrame -> System.out.print(outputFrame.getUtf8String()))
            .dependsOn(postgres, redis, mail)
            .withEnv("SPRING_MAIL_HOST", "mail")
            .withEnv("SPRING_MAIL_PORT", "25")
            .withEnv("SPRING_DATA_REDIS_HOST", "redis")
            .withEnv("SPRING_DATA_REDIS_PORT", "6379")
            .withEnv("SPRING_DATASOURCE_URL", "jdbc:postgresql://testDatabase:5432/voting")
            .withEnv("SPRING_DATASOURCE_USERNAME", "postgres")
            .withEnv("SPRING_DATASOURCE_PASSWORD", "postgres")
            .withEnv("SPRING_TESTS", "true")
            .withCopyToContainer(jar, "/opt/app.jar")
            .withCommand("sh", "-c", "java -jar /opt/app.jar")
            .waitingFor(Wait.forHttp("/api/v1/actuator/health").forPort(8080).forStatusCode(200));

    @BeforeAll
    public static void setUp() {
        baseUrl = "http://" + application.getHost() + ":" + application.getMappedPort(8080) + "/api/v1";
        connectionHolder = () -> DriverManager.getConnection(
                postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword()
        );
        LoginRequest loginRequest = new LoginRequest("user", "password", "pl");

        if(token != null)
            return;

        Response response = given()
                    .contentType("application/json")
                    .when()
                    .body(loginRequest)
                    .post(baseUrl + "/authenticate")
                    .then()
                    .extract()
                    .response();
        token = response.path("token");
    }

}

