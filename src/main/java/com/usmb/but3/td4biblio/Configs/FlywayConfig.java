package com.usmb.but3.td4biblio.Configs;

import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
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
