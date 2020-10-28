package com.udacity.pricing;

import com.udacity.pricing.domain.price.Price;
import com.udacity.pricing.domain.price.PriceRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

/**
 * Creates a Spring Boot Application to run the Pricing Service.
 *
 * References:
 * Spring Boot - Accessing Data with a Restful API: https://spring.io/guides/gs/accessing-data-rest/
 *             - API Docs: https://docs.spring.io/spring-framework/docs
 *             - Packaging: https://docs.spring.io/spring-boot/docs/2.3.4.RELEASE/maven-plugin/reference/html/#repackage
 * In28Minutes - Validation for Rest Services: https://www.springboottutorial.com/spring-boot-validation-for-rest-services
 *             - Unit testing Rest Services: https://www.springboottutorial.com/unit-testing-for-spring-boot-rest-services
 * Baeldung - Creating an executable jar: https://www.baeldung.com/spring-boot-run-maven-vs-executable-jar
 *          - Validating Input to Microservice: https://www.baeldung.com/spring-boot-bean-validation
 *          - Parameterized Tests: https://www.baeldung.com/parameterized-tests-junit-5
 * Nick Gibbon - Test Method Order: https://medium.com/pareture/order-test-execution-in-junit5-jupiter-e3d61ab15f26
 * StackOverflow user1809566 - Json Queries: https://stackoverflow.com/questions/47769549/check-jsonpath-for-empty-string
 *               Greg Case - generating random nubmers: https://stackoverflow.com/questions/363681/how-do-i-generate-random-integers-within-a-specific-range-in-java
 *               glytching - Uing JsonPath Filter: https://stackoverflow.com/questions/47769549/check-jsonpath-for-empty-string
 *               muthu - Mock Mvc Test Parameters: https://stackoverflow.com/questions/17972428/mock-mvc-add-request-parameter-to-test
 *               OldCurmudgeon - Using @JsonProperty: https://stackoverflow.com/questions/12583638/when-is-the-jsonproperty-property-used-and-what-is-it-used-for
 *               Nivas - Currency Code Mapping: https://stackoverflow.com/questions/3888991/currency-code-to-currency-symbol-mapping
 *               Roi Snir - Microservice Delete Requests: https://stackoverflow.com/questions/57235450/how-to-make-an-http-delete-request-with-json-body-in-flutter
 *               Luke Peterson - PUT vs PATCH: https://stackoverflow.com/questions/24241893/should-i-use-patch-or-put-in-my-rest-api
 *               crm86 - Packaging a jar with Spring Boot: https://stackoverflow.com/questions/38792031/springboot-making-jar-files-no-auto-configuration-classes-found-in-meta-inf
 * Jayway JsonPath Evaluator: https://jsonpath.herokuapp.com/
 * Geeks for Geeks - Parsing Json: https://www.geeksforgeeks.org/parse-json-java/
 * Assertible.com - JSON path Syntax: JSON path syntax in detail
 * ProgramCreek.com - MockHttpServletRequestBuilder examples: https://www.programcreek.com/java-api-examples/index.php?api=org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
 * TechnicalStack.com - @Pattern Validation Annotation: http://technicalstack.com/form-validation-part-4-using-annotation-pattern-past-max-etc/
 * André Ilhicas dos Santos - Using @JsonProperty and JSONObject: https://ilhicas.com/2019/04/27/Returning-JSON-object-as-response-in-Spring-Boot.html
 * SpringFramework Guru - Jackson Annotations: https://springframework.guru/jackson-annotations-json/
 * Ali Dehghani - Errors Spring Boot Starter (errors-spring-boot-starter): https://github.com/alimate/errors-spring-boot-starter
 * ZetCode.com - Using a MessageSource Tutorial - http://zetcode.com/spring/messagesource/
 * JavaCodeExamples - Java ArrayList for loop - https://www.javacodeexamples.com/java-arraylist-for-loop-for-each-example/906
 *
 */
@SpringBootApplication
public class PricingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PricingServiceApplication.class, args);
    }

    /**
     * Initializes the prices available.
     * Based on the logic for creating entries in the Vehicle
     * API project (VehiclesAPIApplication) and the code that
     * was originally in the Pricing Service which was eliminated
     * with the creation of a Microservice.
     * @param repository where the price information persists.
     * @return the
     */
    @Bean
    CommandLineRunner initDatabase(PriceRepository repository) {
        return args -> {
            for (Price price : PRICES.values()){
                repository.save(price);
            }
        };
    }

    /**
     * Holds {ID: Price} pairings (current implementation allows for 20 vehicles)
     */
    private static final Map<Long, Price> PRICES = LongStream
            .range(1, 20)
            .mapToObj(i -> new Price("USD", randomPrice(), i))
            .collect(Collectors.toMap(Price::getVehicleId, p -> p));

    /**
     * Gets a random price to fill in for a given vehicle ID.
     * @return random price for a vehicle
     */
    private static BigDecimal randomPrice() {
        return new BigDecimal(ThreadLocalRandom.current().nextDouble(1, 5))
                .multiply(new BigDecimal(5000d)).setScale(2, RoundingMode.HALF_UP);
    }
}
