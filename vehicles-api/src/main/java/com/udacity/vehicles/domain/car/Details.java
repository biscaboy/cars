package com.udacity.vehicles.domain.car;

import com.udacity.vehicles.domain.manufacturer.Manufacturer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Declares the additional detail variables for each Car object,
 * along with related methods for access and setting.
 */
@Embeddable
@ApiModel(parent = Car.class, description = "Represents the details of a Car")
public class Details {

    @NotBlank
    private String body;

    @NotBlank
    private String model;

    @NotNull
    @ManyToOne
    private Manufacturer manufacturer;

    private Integer numberOfDoors;

    private String fuelType;

    private String engine;

    private Integer mileage;

    private Integer modelYear;

    private Integer productionYear;

    private String externalColor;

    @ApiModelProperty( value = "Car body type (e.g. sedan, coupe, SUV, pickup, hatch-back, other")
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @ApiModelProperty(value = "Model name of the car (e.g. Impala, Jetta, Model S, Eldorado, etc)")
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @ApiModelProperty(value = "Producing and designing manufacturer of the car")
    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    @ApiModelProperty(value = "Number of door on the car")
    public Integer getNumberOfDoors() {
        return numberOfDoors;
    }

    public void setNumberOfDoors(Integer numberOfDoors) {
        this.numberOfDoors = numberOfDoors;
    }

    @ApiModelProperty(value = "Fuel type (e.g. Diesel, Gasoline, Electric, Hydrogen, Flinstone Feet, etc)")
    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public String getEngine() {
        return engine;
    }

    @ApiModelProperty(value = "Engine type (e.g. cylinders and liters if gasoline \"2.3 Litre V6\", Battery size if electric, etc) ")
    public void setEngine(String engine) {
        this.engine = engine;
    }

    @ApiModelProperty(value = "Milage of the care determined by reading the odometer")
    public Integer getMileage() {
        return mileage;
    }

    public void setMileage(Integer mileage) {
        this.mileage = mileage;
    }

    @ApiModelProperty(value = "Model year")
    public Integer getModelYear() {
        return modelYear;
    }

    public void setModelYear(Integer modelYear) {
        this.modelYear = modelYear;
    }

    @ApiModelProperty(value = "Production year")
    public Integer getProductionYear() {
        return productionYear;
    }

    public void setProductionYear(Integer productionYear) {
        this.productionYear = productionYear;
    }

    @ApiModelProperty(value = "The car's exterior paint color")
    public String getExternalColor() {
        return externalColor;
    }

    public void setExternalColor(String externalColor) {
        this.externalColor = externalColor;
    }
}
