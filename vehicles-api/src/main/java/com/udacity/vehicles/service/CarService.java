package com.udacity.vehicles.service;

import com.udacity.vehicles.client.maps.MapsClient;
import com.udacity.vehicles.client.prices.PriceClient;
import com.udacity.vehicles.domain.car.Car;
import com.udacity.vehicles.domain.car.CarRepository;

import java.util.List;

import org.springframework.stereotype.Service;

/**
 * Implements the car service create, read, update or delete
 * information about vehicles, as well as gather related
 * location and price data when desired.
 */
@Service
public class CarService {

    private PriceClient pricingClient;

    private MapsClient mapsClient;

    private final CarRepository repository;

    public CarService(CarRepository repository, PriceClient priceClient, MapsClient mapsClient) {
        /**
         * DONE! : Add the Maps and Pricing Web Clients you create
         *   in `VehiclesApiApplication` as arguments and set them here.
         */
        this.repository = repository;
        this.pricingClient = priceClient;
        this.mapsClient = mapsClient;
    }

    /**
     * Gathers a list of all vehicles
     * @return a list of all vehicles in the CarRepository
     */
    public List<Car> list() {

        List<Car> cars = repository.findAll();

        cars.forEach(c -> {
            c.setLocation(mapsClient.getAddress(c.getLocation()));
            c.setPrice(pricingClient.getPrice(c.getId()));
        });

        return cars;
    }

    /**
     * Gets car information by ID (or throws exception if non-existent)
     * @param id the ID number of the car to gather information on
     * @return the requested car's information, including location and price
     */
    public Car findById(Long id) throws CarNotFoundException {
        /**
         * DONE! : Find the car by ID from the `repository` if it exists.
         *   If it does not exist, throw a CarNotFoundException
         *   Remove the below code as part of your implementation.
         */
        Car car = repository.findById(id).orElseThrow(CarNotFoundException::new);

        /**
         * DONE! : Use the Pricing Web client you create in `VehiclesApiApplication`
         *   to get the price based on the ``id input'
         * DONE! : Set the price of the car
         * Note: The car class file uses @transient, meaning you will need to call
         *   the pricing service each time to get the price.
         */
        car.setPrice(pricingClient.getPrice(car.getId()));

        /**
         * DONE! - Use the Maps Web client you create in `VehiclesApiApplication`
         *   to get the address for the vehicle. You should access the location
         *   from the car object and feed it to the Maps service.
         * DONE! -  Set the location of the vehicle, including the address information
         * Note: The Location class file also uses @transient for the address,
         * meaning the Maps service needs to be called each time for the address.
         */
        car.setLocation(mapsClient.getAddress(car.getLocation()));

        return car;
    }

    /**
     * Either creates or updates a vehicle, based on prior existence of car
     * @param car A car object, which can be either new or existing
     * @return the new/updated car is stored in the repository
     * @throws CarNotFoundException if the car id is provided by does not exist in the data store
     */
    public Car save(Car car) throws CarNotFoundException {

        if (car.getId() != null) {
            return repository.findById(car.getId())
                    .map(carToBeUpdated -> {
                        carToBeUpdated.setDetails(car.getDetails());
                        carToBeUpdated.setLocation(car.getLocation());
                        return repository.save(carToBeUpdated);
                    }).orElseThrow(CarNotFoundException::new);
        }

        Car saved = repository.save(car);
        saved.setPrice(pricingClient.getPrice(saved.getId()));
        saved.setLocation(mapsClient.getAddress(car.getLocation()));
        return saved;
    }

    /**
     * Deletes a given car by ID
     * @param id the ID number of the car to delete
     */
    public void delete(Long id) {
        /**
         * DONE! : Find the car by ID from the `repository` if it exists.
         *   If it does not exist, throw a CarNotFoundException
         */
        Car car = repository.findById(id).orElseThrow(CarNotFoundException::new);

        /**
         * DONE! : Delete the car from the repository.
         */
        repository.deleteById(car.getId());
    }
}
