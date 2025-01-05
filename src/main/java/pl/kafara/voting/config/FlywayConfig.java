package pl.kafara.voting.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.sql.DataSource;

@Configuration
@DependsOn("entityManagerFactory")
public class FlywayConfig {

    @Autowired
    public FlywayConfig(DataSource dataSource, @Value("${flyway.migrate:true}") boolean flywayMigrate) {
        if (flywayMigrate) {
            Flyway.configure()
                    .baselineOnMigrate(true)
                    .dataSource(dataSource)
                    .load()
                    .migrate();
        }
    }
}
