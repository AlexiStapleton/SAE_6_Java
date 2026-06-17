//package com.usmb.but3.td4biblio.Configs;
//
//import org.flywaydb.core.Flyway;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//
//import javax.sql.DataSource;
//
//@Configuration
//@Profile("!test")
//public class FlywayConfig {
//
//    @Bean(initMethod = "migrate")
//    public Flyway flyway(DataSource dataSource) {
//        return Flyway.configure()
//                .dataSource(dataSource)
//                .schemas("biblio")
//                .locations("classpath:db/migration")
//                .baselineOnMigrate(true)
//                .createSchemas(true)
//                .validateOnMigrate(true)
//                .load();
//    }
//}

package com.usmb.but3.td4biblio.Configs;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;

@Configuration
// On retire le @Profile("!test") pour que cette classe s'exécute AUSSI en test
public class FlywayConfig {

    // Spring va injecter la valeur correspondante au profil actif
    @Value("${spring.flyway.locations}")
    private String locations;

    @Value("${spring.flyway.schemas}")
    private String schema;

    @Bean(initMethod = "migrate")
    public Flyway flyway(DataSource dataSource) {
        return Flyway.configure()
                .dataSource(dataSource)
                .schemas(schema)          // Devra être "biblio" ou "BIBLIO"
                .locations(locations)    // Devra être "classpath:db/migration" ou "classpath:db/migration-test"
                .baselineOnMigrate(true)
                .createSchemas(true)
                .validateOnMigrate(true)
                .load();
    }
}
