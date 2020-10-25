package com.udacity.pricing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;

/**
 * Creates a Spring Boot Application to run the Pricing Service.
 *
 * References:
 * Spring Boot - Accessing Data with a Restful API: https://spring.io/guides/gs/accessing-data-rest/
 * In28Minutes - Validation for Rest Services: https://www.springboottutorial.com/spring-boot-validation-for-rest-services
 * Baeldung - Creating an executable jar: https://www.baeldung.com/spring-boot-run-maven-vs-executable-jar
 *          - Validating Input to Microservice: https://www.baeldung.com/spring-boot-bean-validation
 * Nick Gibbon - Test Method Order: https://medium.com/pareture/order-test-execution-in-junit5-jupiter-e3d61ab15f26
 * StackOverflow user1809566 - Json Queries: https://stackoverflow.com/questions/47769549/check-jsonpath-for-empty-string
 *               Greg Case - generating random nubmers: https://stackoverflow.com/questions/363681/how-do-i-generate-random-integers-within-a-specific-range-in-java
 * Jayway JsonPath Evaluator: https://jsonpath.herokuapp.com/
 * Geeks for Geeks - Parsing Json: https://www.geeksforgeeks.org/parse-json-java/
 */
@SpringBootApplication
public class PricingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PricingServiceApplication.class, args);
    }

}
