package pl.kafara.voting.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.sql.DataSource;

@Configuration
@DependsOn("entityManagerFactory")
@ConditionalOnProperty(name = "spring.tests", havingValue = "false", matchIfMissing = true)
public class FlywayConfig {

    @Autowired
    public FlywayConfig(DataSource dataSource) {
        Flyway.configure()
                .baselineOnMigrate(true)
                .dataSource(dataSource)
                .load()
                .migrate();
    }
}
