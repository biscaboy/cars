package com.udacity.vehicles.domain;

import com.udacity.vehicles.domain.car.Car;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Available values for condition of a given car.
 */
@ApiModel(parent = Car.class, description = "Enumeration of vehicle condition types")
public enum Condition {

    @ApiModelProperty(value = "Denotes a car that has had previous owners")
    USED,
    @ApiModelProperty(value = "Denotes a car that never been sold")
    NEW;
}
