package com.usmb.but3.td4biblio.Configs;

import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
@Profile("!test")
public class FlywayConfig {

    @Bean(initMethod = "migrate")
    public Flyway flyway(DataSource dataSource) {
        return Flyway.configure()
                .dataSource(dataSource)
                .schemas("biblio")
                .locations("classpath:db/migration")
                .baselineOnMigrate(true)
                .createSchemas(true)
                .validateOnMigrate(true)
                .load();
    }
}
