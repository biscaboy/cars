package com.udacity.vehicles;

import com.udacity.vehicles.domain.manufacturer.Manufacturer;
import com.udacity.vehicles.domain.manufacturer.ManufacturerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Launches a Spring Boot application for the Vehicles API,
 * initializes the car manufacturers in the database,
 * and launches web clients to communicate with maps and pricing.
 *
 * Resources:
 *
 *  Stack Overflow
 *      Madhusudana Reffy Sunnapu - Using @Qualifier Annotation - https://stackoverflow.com/questions/36183624/how-to-autowire-by-name-in-spring-with-annotations
 *      Nitin - Hateoas with Spring Boot 2.2.0+ - https://stackoverflow.com/questions/25352764/hateoas-methods-not-found#25368187
 *      zhuhang.jasper - @Valid vs @Validatated - https://stackoverflow.com/questions/36173332/difference-between-valid-and-validated-in-spring
 *
 *  ModelMapper.org - Javadoc - http://modelmapper.org/javadoc/org/modelmapper
 *  Lokesh Gupto - Maven dependencies for JUnit 5 - https://howtodoinjava.com/junit5/junit5-maven-dependency/
 *                 Spring Hateoas Tutorial - https://howtodoinjava.com/spring5/hateoas/spring-hateoas-tutorial/
 *  Spring Hateoas - Javadocs - https://docs.spring.io/spring-hateoas/docs/1.1.2.RELEASE/api/
 *                 - Reference - https://docs.spring.io/spring-hateoas/docs/current/reference/html/
 *  Maven Repository - https://mvnrepository.com
 *  Baeldung - Java Optional Class - https://www.baeldung.com/java-optional
 *  Brian Hannaway - HATEOAS REST Services with Spring - https://dzone.com/articles/hypermedia-driven-rest-services-with-spring-hateoa
 *
 */
@SpringBootApplication
@EnableJpaAuditing
public class VehiclesApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(VehiclesApiApplication.class, args);
    }

    /**
     * Initializes the car manufacturers available to the Vehicle API.
     * @param repository where the manufacturer information persists.
     * @return the car manufacturers to add to the related repository
     */
    @Bean
    CommandLineRunner initDatabase(ManufacturerRepository repository) {
        return args -> {
            repository.save(new Manufacturer(100, "Audi"));
            repository.save(new Manufacturer(101, "Chevrolet"));
            repository.save(new Manufacturer(102, "Ford"));
            repository.save(new Manufacturer(103, "BMW"));
            repository.save(new Manufacturer(104, "Dodge"));
        };
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    /**
     * Web Client for the maps (location) API
     * @param endpoint where to communicate for the maps API
     * @return created maps endpoint
     */
    @Bean(name="maps")
    public WebClient webClientMaps(@Value("${maps.endpoint}") String endpoint) {
        return WebClient.create(endpoint);
    }

    /**
     * Web Client for the pricing API
     * TODO: Find the service dynamically from the eureka server.
     * @param endpoint where to communicate for the pricing API
     * @return created pricing endpoint
     */
    @Bean(name="pricing")
    public WebClient webClientPricing(@Value("${pricing.endpoint}") String endpoint) {
        return WebClient.create(endpoint);
    }

}
