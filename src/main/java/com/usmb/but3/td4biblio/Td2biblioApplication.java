package com.usmb.but3.td4biblio;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Td2biblioApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure().load();
		dotenv.entries().forEach(e ->
				System.setProperty(e.getKey(), e.getValue())
		);
		System.out.println("DB_USERNAME: " + System.getProperty("DB_USERNAME"));
		System.out.println("POSTGRES_USER: " + System.getProperty("POSTGRES_USER"));
		SpringApplication.run(Td2biblioApplication.class, args);
	}

}
