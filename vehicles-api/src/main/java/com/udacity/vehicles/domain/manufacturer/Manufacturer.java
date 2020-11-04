package com.udacity.vehicles.domain.manufacturer;

import com.udacity.vehicles.domain.car.Car;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Declares class to hold car manufacturer information.
 */
@Entity
@ApiModel(parent = Car.class, description = "Represents the manufacturing brand of the Car")
public class Manufacturer {

    @Id
    private Integer code;
    private String name;

    public Manufacturer() { }

    public Manufacturer(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    @ApiModelProperty(value = "The manufacturing company's brand name (e.g. Cadillac, Ford, Volkswagen, Suburu, etc)")
    public String getName() {
        return name;
    }
}
