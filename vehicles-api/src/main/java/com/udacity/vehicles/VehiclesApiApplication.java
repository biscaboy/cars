package com.udacity.vehicles;

import com.udacity.vehicles.domain.manufacturer.Manufacturer;
import com.udacity.vehicles.domain.manufacturer.ManufacturerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

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
 *      Multiple contributors - Maven Plugins Not Found - https://stackoverflow.com/questions/20496239/maven-plugins-can-not-be-found-in-intellij
 *      andrea - maven-compiler-plugin not found - https://stackoverflow.com/questions/60120587/maven-compiler-plugin-not-found
 *
 *  Apache Maven Project -
 *      The Compiler - http://maven.apache.org/plugins/maven-compiler-plugin/index.html
 *      Source and Target Settings - http://maven.apache.org/plugins/maven-compiler-plugin/examples/set-compiler-source-and-target.html
 *      Plugin Registry - http://people.apache.org/~ltheussl/maven-stage-site/guides/introduction/introduction-to-plugin-registry.html
 *      Maven Repository - https://mvnrepository.com/
 *      Lokesh Gupto - Maven dependencies for JUnit 5 - https://howtodoinjava.com/junit5/junit5-maven-dependency/
 *
 *  Eureka Service Discovery
 *      Spring Cloud Docs - https://cloud.spring.io/spring-cloud-netflix/multi/multi__service_discovery_eureka_clients.html
 *      The Practical Developer - https://thepracticaldeveloper.com/spring-boot-service-discovery-eureka/
 *      studytonight.com - Service Discovery using Eureka - https://www.studytonight.com/post/service-discovery-using-eureka-in-spring-microservices
 *      Spring Cloud Netflix on GitHub - eureka.client.enabled - https://github.com/spring-cloud/spring-cloud-netflix/issues/1511
 *      Baeldung - Introduction to Spring Cloud - https://www.baeldung.com/spring-cloud-netflix-eureka
 *
 *  HATEOAS
 *      Spring Hateoas - Javadocs - https://docs.spring.io/spring-hateoas/docs/1.1.2.RELEASE/api/
 *                     - Reference - https://docs.spring.io/spring-hateoas/docs/current/reference/html/
 *      Lokesh Gupto - Spring Hateoas Tutorial - https://howtodoinjava.com/spring5/hateoas/spring-hateoas-tutorial/
 *      Brian Hannaway - HATEOAS REST Services with Spring - https://dzone.com/articles/hypermedia-driven-rest-services-with-spring-hateoa
 *
 *  Spring Boot Controller Testing
 *      The Practical Developer - https://thepracticaldeveloper.com/guide-spring-boot-controller-tests/
 *      Spring Boot Sample Tests - https://github.com/spring-projects/spring-framework/blob/master/spring-test/src/test/java/org/springframework/test/web/client/samples/SampleTests.java
 *      JacksonTester JavaDoc - https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/test/json/JacksonTester.html
 *
 *
 *  Other Topics
 *      ModelMapper.org - Javadoc - http://modelmapper.org/javadoc/org/modelmapper
 *      Baeldung - Java Optional Class - https://www.baeldung.com/java-optional
 *
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableEurekaClient
public class VehiclesApiApplication {

    @Autowired
    private DiscoveryClient discoveryClient;

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
     * DONE! : Find the service dynamically from the eureka server.
     *
     * This method initialized the connection to the Pricing API
     * or a local server instance if a remote connection is not
     * availiable.
     *
     * @param connectToEureka if true discover and use remote service API,
     *                        if false use local endpoint.
     * @param serviceName the name of the Eureka service to discover.
     * @param localEndpoint where to communicate for the pricing API.
     * @return created pricing endpoint
     */
    @Bean(name="pricing")
    public WebClient webClientPricing(
            @Value("${pricing.endpoint.use.eureka:false}") boolean connectToEureka,
            @Value("${pricing.service.name:PRICING-SERVICE}") String serviceName,
            @Value("${pricing.endpoint.local:http://localhost:8082}") String localEndpoint) {

        String endpoint = localEndpoint;

        if (connectToEureka) {
            List<ServiceInstance> list = discoveryClient.getInstances(serviceName);
            if (list != null && list.size() > 0) {
                endpoint = list.get(0).getUri().toString();
            }
        }
        return WebClient.create(endpoint);

    }

}
