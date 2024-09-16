package pl.kafara.voting.integration;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;

import java.nio.file.Paths;

@Testcontainers
public class IntegrationTestConfiguration {
    static final Network network = Network.newNetwork();

    static MountableFile jar = MountableFile
            .forHostPath(Paths.get("target/votingApp.jar").toAbsolutePath());

    @Container
    static final PostgreSQLContainer<?> postgres;

    @Container
    static final GenericContainer<?> application;

    @Container
    static final GenericContainer<?> redis;

    @Container
    static final GenericContainer<?> mail;

    public static String baseUrl;

    static {
        postgres = new PostgreSQLContainer<>("postgres:16.4-alpine3.20")
                .withNetwork(network)
                .withNetworkAliases("testDatabase")
                .withExposedPorts(5432)
                .withUsername("postgres")
                .withPassword("postgres")
                .withDatabaseName("voting")
                .withReuse(true)
                .waitingFor(Wait.defaultWaitStrategy());
        postgres.start();
        redis = new GenericContainer<>("redis:7.4-alpine3.20")
                .withNetworkAliases("redis")
                .withNetwork(network)
                .withExposedPorts(6379)
                .withReuse(true)
                .waitingFor(Wait.defaultWaitStrategy());
        redis.start();
        mail = new GenericContainer<>("rnwood/smtp4dev:3.6.0")
                .withNetworkAliases("mail")
                .withNetwork(network)
                .withExposedPorts(25)
                .withReuse(true)
                .waitingFor(Wait.defaultWaitStrategy());
        mail.start();

        application = new GenericContainer<>("eclipse-temurin:21.0.2_13-jdk")
                .withNetwork(network)
                .withExposedPorts(8080)
                .withLogConsumer(outputFrame -> System.out.print(outputFrame.getUtf8String()))
                .dependsOn(postgres)
                .dependsOn(redis)
                .dependsOn(mail)
                .withEnv("SPRING_MAIL_HOST", "mail")
                .withEnv("SPRING_MAIL_PORT", "25")
                .withEnv("SPRING_DATA_REDIS_HOST", "redis")
                .withEnv("SPRING_DATA_REDIS_PORT", "6379")
                .withEnv("SPRING_DATASOURCE_URL", "jdbc:postgresql://testDatabase:5432/voting")
                .withEnv("SPRING_DATASOURCE_USERNAME", "postgres")
                .withEnv("SPRING_DATASOURCE_PASSWORD", "postgres")
                .withCopyToContainer(jar, "/opt/app.jar")
                .withCommand("sh", "-c", "java -jar /opt/app.jar")
                .withReuse(true)
                .waitingFor(Wait.forHttp("/api/v1/actuator/health").forPort(8080).forStatusCode(200));
        application.start();

        baseUrl = "http://" + application.getHost() + ":" + application.getMappedPort(8080) + "/api/v1";
    }
}
