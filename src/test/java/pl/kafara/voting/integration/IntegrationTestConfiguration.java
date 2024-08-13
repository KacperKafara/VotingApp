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

    public static String baseUrl;

    static {
        postgres = new PostgreSQLContainer<>("postgres:16.4-alpine3.20")
                .withNetwork(network)
                .withNetworkAliases("testDatabase")
                .withExposedPorts(5432)
                .withUsername("postgres")
                .withPassword("postgres")
                .withDatabaseName("voting")
                .waitingFor(Wait.defaultWaitStrategy());
        postgres.start();

        application = new GenericContainer<>("eclipse-temurin:21.0.2_13-jdk")
                .withNetwork(network)
                .withExposedPorts(8080)
                .withLogConsumer(outputFrame -> System.out.print(outputFrame.getUtf8String()))
                .dependsOn(postgres)
                .withEnv("SPRING_DATASOURCE_URL", "jdbc:postgresql://testDatabase:5432/voting")
                .withEnv("SPRING_DATASOURCE_USERNAME", "postgres")
                .withEnv("SPRING_DATASOURCE_PASSWORD", "postgres")
                .withCopyToContainer(jar, "/opt/app.jar")
                .withCommand("sh", "-c", "java -jar /opt/app.jar")
                .waitingFor(Wait.forHttp("/api/v1/actuator/health").forPort(8080).forStatusCode(200));
        application.start();

        baseUrl = "http://" + application.getHost() + ":" + application.getMappedPort(8080) + "/api/v1";
    }
}
