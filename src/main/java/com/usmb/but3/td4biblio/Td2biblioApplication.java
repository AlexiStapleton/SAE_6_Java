package com.usmb.but3.td4biblio;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class Td2biblioApplication {

	public static void main(String[] args) {
//		System.out.println("Olalalala retrouvemoi" + new BCryptPasswordEncoder().encode("Admin1234"));
		Dotenv dotenv = Dotenv.configure()
				.ignoreIfMissing()
				.load();
		dotenv.entries().forEach(e ->
				System.setProperty(e.getKey(), e.getValue())
		);
		SpringApplication.run(Td2biblioApplication.class, args);
	}

}
