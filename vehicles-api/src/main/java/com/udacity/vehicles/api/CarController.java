package com.udacity.vehicles.api;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import com.udacity.vehicles.domain.car.Car;
import com.udacity.vehicles.service.CarService;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Implements a REST-based controller for the Vehicles API.
 */
@RestController
@RequestMapping("/cars")
// Swagger Documentation API Annotation.  Tags
@Api(value="Vehicle Inventory API")
class CarController {

    private final CarService carService;
    private final CarResourceAssembler assembler;

    CarController(CarService carService, CarResourceAssembler assembler) {
        this.carService = carService;
        this.assembler = assembler;
    }

    /**
     * Creates a list to store any vehicles.
     * @return list of vehicles
     */
    @GetMapping
        @ApiOperation(value = "List all cars in the inventory",
                notes = "All cars currently in the system will be listed.  " +
                        "Price and location information are generated randomly." +
                        "These cars do not represent anything in the real world.")

    Resources<Resource<Car>> list() {
        List<Resource<Car>> resources = carService.list().stream().map(assembler::toResource)
                .collect(Collectors.toList());
        return new Resources<>(resources,
                linkTo(methodOn(CarController.class).list()).withSelfRel());
    }

    /**
     * Gets information of a specific car by ID.
     * @param id the id number of the given vehicle
     * @return all information for the requested vehicle
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "Find a car by its vehicle id in the current inventory.",
            notes = "The id is generated with the vehicle during POST request.")
    Resource<Car> get(@ApiParam(value="id of the vehicle to find", required=true)
                        @PathVariable Long id) {
        /**
         * DONE! : Use the `findById` method from the Car Service to get car information.
         * DONE! : Use the `assembler` on that car and return the resulting output.
         *   Update the first line as part of the above implementing.
         *
         */        Car car = carService.findById(id);
        return assembler.toResource(car);
    }

    /**
     * Posts information to create a new vehicle in the system.
     * @param car A new vehicle to add to the system.
     * @return response that the new vehicle was added to the system
     * @throws URISyntaxException if the request contains invalid fields or syntax
     */
    @PostMapping
    @ApiOperation(value = "Save a car to the inventory.",
            notes = "Save a car. ")
    ResponseEntity<?> post(@ApiParam(value="The Car to save.",  required = true)
                            @Valid @RequestBody Car car) throws URISyntaxException {
        /**
         * DONE! : Use the `save` method from the Car Service to save the input car.
         * DONE! : Use the `assembler` on that saved car and return as part of the response.
         *   Update the first line as part of the above implementing.
         */
        Car saved = carService.save(car);
        Resource<Car> resource = assembler.toResource(saved);
        return ResponseEntity.created(new URI(resource.getId().expand().getHref())).body(resource);
    }

    /**
     * Updates the information of a vehicle in the system.
     * @param id The ID number for which to update vehicle information.
     * @param car The updated information about the related vehicle.
     * @return response that the vehicle was updated in the system
     */
    @PutMapping("/{id}")
    @ApiOperation(value = "Update a car by id in the current inventory.",
            notes = "The id is generated with the vehicle during POST request.  Only vehicle details and location are mutible.")

    ResponseEntity<?> put(@ApiParam(value="id of the vehicle to update", required=true)
                            @PathVariable Long id,
                          @ApiParam(value="Car object to save. (Only detail and location fields will be updated.)", required=true)
                            @Valid @RequestBody Car car) {
        /**
         * DONE! : Set the id of the input car object to the `id` input.
         * DONE! : Save the car using the `save` method from the Car service
         * Done! : Use the `assembler` on that updated car and return as part of the response.
         *   Update the first line as part of the above implementing.
         */
        car.setId(id);
        Car saved = carService.save(car);
        Resource<Car> resource = assembler.toResource(saved);
        return ResponseEntity.ok(resource);
    }

    /**
     * Removes a vehicle from the system.
     * @param id The ID number of the vehicle to remove.
     * @return response that the related vehicle is no longer in the system
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete a car by id in the current inventory.",
            notes = "The id is generated with the vehicle during POST request.")

    ResponseEntity<?> delete(@ApiParam(value="id of the vehicle to delete", required=true)
                                @PathVariable Long id) {
        /**
         * DONE! : Use the Car Service to delete the requested vehicle.
         */
        carService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
