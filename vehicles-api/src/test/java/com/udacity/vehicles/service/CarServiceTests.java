package com.udacity.vehicles.service;

import com.udacity.vehicles.domain.Condition;
import com.udacity.vehicles.domain.Location;
import com.udacity.vehicles.domain.car.Car;
import com.udacity.vehicles.domain.car.Details;
import com.udacity.vehicles.domain.manufacturer.Manufacturer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class CarServiceTests {

    private Car chevy;
    private Car audi;

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
    public void testSaveCar() {
        Car saved = carService.save(chevy);
        assertEquals(chevy.getDetails().getBody(), saved.getDetails().getBody());
        assertEquals(chevy.getDetails().getMileage(), saved.getDetails().getMileage());
    }

    @Test
    public void testFindCarById() {
        Car saved = carService.save(audi);
        assertNotNull(saved.getId());

        Car found = carService.findById(saved.getId());
        assertNotNull(found.getLocation().getAddress());
        assertNotNull(found.getLocation().getCity());
        assertNotNull(found.getLocation().getState());
        assertNotNull(found.getLocation().getZip());
        // TODO: send the price to the pricing api
        //assertTrue(found.getPrice() > 0);

    }
}
