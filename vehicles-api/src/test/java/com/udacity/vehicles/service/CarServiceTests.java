package com.udacity.vehicles.service;

import java.time.LocalDateTime;
import com.udacity.vehicles.domain.Condition;
import com.udacity.vehicles.domain.Location;
import com.udacity.vehicles.domain.car.Car;
import com.udacity.vehicles.domain.car.Details;
import com.udacity.vehicles.domain.manufacturer.Manufacturer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.env.Environment;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CarServiceTests {

    private Car chevy;
    private Car audi;

    @Autowired
    Environment env;

    @Autowired
    @Qualifier("eurekaUrl")
    String eurekaUrl;

    @Autowired
    CarService carService;

    @BeforeEach
    public void beforeEach() {
        Details details = new Details();
        details.setBody("sedan");
        details.setNumberOfDoors(4);
        details.setFuelType("Gasoline");
        details.setEngine("3.6L V6");
        details.setMileage(100);
        details.setModelYear(2020);
        details.setProductionYear(2020);
        details.setExternalColor("white");

        chevy = new Car();
        chevy.setCondition(Condition.NEW);
        chevy.setCreatedAt(LocalDateTime.now());
        chevy.setLocation(new Location(0.0, 0.0));
        chevy.setModifiedAt(LocalDateTime.now());
        chevy.setPrice("25000");
        details.setModel("Impala");
        details.setManufacturer(new Manufacturer(101, "Chevrolet"));
        chevy.setDetails(details);

        audi = new Car();
        audi.setCondition(Condition.NEW);
        audi.setCreatedAt(LocalDateTime.now());
        audi.setLocation(new Location(0.0, 0.0));
        audi.setModifiedAt(LocalDateTime.now());
        audi.setPrice("45000");
        details.setModel("A4");
        details.setManufacturer(new Manufacturer(100, "Audi"));
        audi.setDetails(details);

    }

    @Test
    @DisplayName("Save a car")
    public void testSaveCar() {
        Car saved = carService.save(chevy);
        assertEquals(chevy.getDetails().getBody(), saved.getDetails().getBody());
        assertEquals(chevy.getDetails().getMileage(), saved.getDetails().getMileage());
    }

    /**
     * Complete a full request and check the results with pricing
     * and location information as well.
     *
     * Since this is a backend service integration test, to ensure the Service
     * works even if the backend API's are down, check to see if the
     * api services are available and test the modified behavior.
     */
    @Test
    @DisplayName("Find a car with an id")
    public void testFindCarById() {
        boolean mapsAvailable = ServiceUtil.pingURL(env.getProperty("maps.endpoint"), 1000);
        boolean pricingAvailable = ServiceUtil.pingURL(eurekaUrl, 1000);

        Car saved = carService.save(audi);
        assertNotNull(saved.getId());

        Car found = carService.findById(saved.getId());
        if (mapsAvailable) {
            assertNotNull(found.getLocation().getAddress());
            assertNotNull(found.getLocation().getCity());
            assertNotNull(found.getLocation().getState());
            assertNotNull(found.getLocation().getZip());
        } else {
            assertNotNull(found.getLocation());
        }

        if (pricingAvailable) {
            assertNotEquals("(consult price)", found.getPrice());
            String[] split = found.getPrice().split(" ");
            assertEquals("USD", split[0]);
            assertTrue(Double.valueOf(split[1]) > 0.0);
        } else {
            assertEquals("(consult price)", found.getPrice());
        }
    }

    @Test
    @DisplayName("Delete a car")
    public void testDeleteCar() {
        int count = carService.list().size();
        Car saved = carService.save(chevy);
        int insertCount = carService.list().size();
        carService.delete(saved.getId());
        int deleteCount = carService.list().size();
        assertAll ("Delete a Car",
                () -> assertEquals(count + 1, insertCount, "The car was never saved"),
                () -> assertFalse(count > deleteCount, "More then one car was deleted." ),
                () -> assertEquals(count, deleteCount, "The car was not deleted.")
        );
    }

    @Test
    @DisplayName("Find a car with a bad id")
    public void testFindCarFail() {
        assertThrows(CarNotFoundException.class, () -> { carService.findById(0L); });
    }

    @Test
    @DisplayName("Update a car")
    public void testUpdateCar() {
        Car saved = carService.save(chevy);
        assertEquals(chevy.getDetails().getBody(), saved.getDetails().getBody());
        assertEquals(chevy.getDetails().getMileage(), saved.getDetails().getMileage());

        saved.setCondition(Condition.USED);
        saved.getDetails().setMileage(Integer.valueOf(111111));

        Car updated = carService.save(saved);
        assertEquals(saved.getCondition(), updated.getCondition());
        assertEquals(saved.getDetails().getMileage(), updated.getDetails().getMileage());

    }
}
