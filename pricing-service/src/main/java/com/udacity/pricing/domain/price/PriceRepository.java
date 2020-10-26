package com.udacity.pricing.domain.price;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface PriceRepository extends CrudRepository<Price, Long> {

    Price findByVehicleId(@Param("vehicle_id") Long vehicleId);
}
