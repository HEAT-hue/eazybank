package com.eazybytes.cards;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@OpenAPIDefinition(
        info = @Info(
                title = "Cards microservice REST API documentation",
                description = "Eazybank Cards microservice documentation",
                version = "v1",
                contact = @Contact(
                        name = "Emmanuel",
                        email = "onyejemeemmanuel65@gmail.com"
                ),
                license = @License(
                        name = "Apache 2.0"
                )
        )
)
@SpringBootApplication
@EnableJpaRepositories("com.eazybytes.cards.repository")     // Spring data J{A config
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
public class CardsServicesApplication {

    public static void main(String[] args) {
        SpringApplication.run(CardsServicesApplication.class, args);
    }

}
