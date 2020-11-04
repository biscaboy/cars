package com.udacity.vehicles.domain;

import com.udacity.vehicles.domain.car.Car;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Embeddable;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

/**
 * Stores information about a given location.
 * Latitude and longitude must be provided, while other
 * location information must be gathered each time from
 * the maps API.
 */
@Embeddable
@ApiModel(parent = Car.class, description = "Address, longitude and latitude of the vehicle - (system generated)")
public class Location {

    @NotNull
    private Double lat;

    @NotNull
    private Double lon;

    @Transient
    private String address;

    @Transient
    private String city;

    @Transient
    private String state;

    @Transient
    private String zip;

    public Location() {
    }

    public Location(Double lat, Double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLon() {
        return lon;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }
}
