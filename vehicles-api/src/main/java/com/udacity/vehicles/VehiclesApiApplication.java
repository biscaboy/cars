package com.udacity.vehicles;

import com.udacity.vehicles.domain.manufacturer.Manufacturer;
import com.udacity.vehicles.domain.manufacturer.ManufacturerRepository;
import com.udacity.vehicles.eureka.EurekaEndpoint;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
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
 *      Multiple contributors - Maven Plugins Not Found - https://stackoverflow.com/questions/20496239/maven-plugins-can-not-be-found-in-intellij
 *      andrea - maven-compiler-plugin not found - https://stackoverflow.com/questions/60120587/maven-compiler-plugin-not-found
 *       Marok - Logging Level Properties -https://stackoverflow.com/questions/35353869/how-turn-off-debug-log-messages-in-spring-boot
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
 *  Swagger
 *      Official docs - https://swagger.io/docs
 *                      https://docs.swagger.io/swagger-core/v1.5.X/apidocs/index.html
 *      Core Annotations - https://github.com/swagger-api/swagger-core/wiki/Annotations-1.5.X
 *      Bhargav Bachina  Example Swagger Implementation on a Webservice API - https://github.com/bbachi/java-webservice-example
 *      Stack Overflow - Ron - Customizing https://stackoverflow.com/questions/26742521/sending-dynamic-custom-headers-in-swagger-ui-try-outs
 *      Java Dev Journal - Manish Sharma - https://www.javadevjournal.com/spring/rest/swagger-2-spring-rest-api/
 *      Java Guides - Ramesh Fadatare - https://www.javaguides.net/2018/10/swagger-annotations-for-rest-api-documentation.html
 *                                    - https://www.javaguides.net/2018/10/spring-boot-2-restful-api-documentation-with-swagger2-tutorial.html
 *      Custom Layouts - https://github.com/swagger-api/swagger-ui/blob/master/docs/customization/custom-layout.md
 *      Spring and React.js Tutorial - https://spring.io/guides/tutorials/react-and-spring-data-rest/
 *
 *  Other Topics
 *      ModelMapper.org - Javadoc - http://modelmapper.org/javadoc/org/modelmapper
 *      Baeldung - Java Optional Class - https://www.baeldung.com/java-optional
 *      Terms of Service - Website Policies - https://www.websitepolicies.com/blog/sample-terms-service-template
 *      @GetMapping annotation - Shubham S - https://knowledge.udacity.com/questions/345642
 *
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableEurekaClient
public class VehiclesApiApplication {

    @Autowired
    Environment env;

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
     */
    @Bean(name="pricing")
    public WebClient webClientPricing(@Qualifier("eurekaUrl") String eurekaUrl) {
        return WebClient.create(eurekaUrl);

    }

    /**
     * DONE! : Find the service dynamically from the eureka server.
     *
     * This method provides the endpoint url of the Pricing API
     * on the Eureka server.  It returns an endpoint for a local server
     * instance if a remote connection via Eureka is not available.
     *
     *  @param connectToEureka if true discover and use remote service API,
     *                        if false use local endpoint.
     *  @param serviceName the name of the Eureka service to discover.
     *  @param localEndpoint where to communicate for the pricing API.
     *  @return created pricing endpoint url
     */
    @Bean(name="eurekaUrl")
    public String eurekaUrl (
            @Value("${pricing.endpoint.use.eureka:false}") boolean connectToEureka,
            @Value("${pricing.service.name:PRICING-SERVICE}") String serviceName,
            @Value("${pricing.endpoint.local:http://localhost:8082}") String localEndpoint) {

        EurekaEndpoint endpoint = new EurekaEndpoint(discoveryClient, connectToEureka, serviceName, localEndpoint);
        return endpoint.lookup();
    }


    @Bean(name="vehicleServerUrl")
    public String vehicleServerUrl (@Value("${server.hostname:localhost}") String host,
                                    @Value("${server.port:8080}") String port) {
        return "http://" + host + ":" + port;
    }

}
