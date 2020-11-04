package com.udacity.vehicles.domain.car;

import com.udacity.vehicles.domain.Condition;
import com.udacity.vehicles.domain.Location;
import java.time.LocalDateTime;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Declares the Car class, related variables and methods.
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@ApiModel(description = "Represents a car for sale in inventory")
public class Car {

    @Id
    @GeneratedValue
    private Long id;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Condition condition;

    @Valid
    @Embedded
    private Details details = new Details();

    @Valid
    @Embedded
    private Location location = new Location(0d, 0d);

    @Transient
    private String price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ApiModelProperty(value = "Date and time of creation")
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @ApiModelProperty(value = "Date and time of latest update")
    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(LocalDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    @ApiModelProperty(value = "Condition", allowableValues = "NEW | USED")
    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    @ApiModelProperty(value = "Details for this car (See DETAILS Model)")
    public Details getDetails() {
        return details;
    }

    public void setDetails(Details details) {
        this.details = details;
    }

    @ApiModelProperty(value = "Address location for this car - Randomly generated (See LOCATTION Model)")
    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @ApiModelProperty(value = "Price (and listed currency) of this car - Randomly generated")
    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
