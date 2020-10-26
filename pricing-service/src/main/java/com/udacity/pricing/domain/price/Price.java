package com.udacity.pricing.domain.price;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the price of a given vehicle, including currency.
 */
@Entity
public class Price {

    private static final String CODES = "AED|AFN|ALL|AMD|ANG|AOA|ARS|AUD|AWG|AZN|BAM|BBD|BDT|BGN|BHD|BIF|BMD|BND|BOB|BOV|BRL|BSD|BTN|BWP|BYN|BZD|CAD|CDF|CHE|CHF|CHW|CLF|CLP|CNY|COP|COU|CRC|CUC|CUP|CVE|CZK|DJF|DKK|DOP|DZD|EGP|ERN|ETB|EUR|FJD|FKP|GBP|GEL|GHS|GIP|GMD|GNF|GTQ|GYD|HKD|HNL|HRK|HTG|HUF|IDR|ILS|INR|IQD|IRR|ISK|JMD|JOD|JPY|KES|KGS|KHR|KMF|KPW|KRW|KWD|KYD|KZT|LAK|LBP|LKR|LRD|LSL|LYD|MAD|MDL|MGA|MKD|MMK|MNT|MOP|MRU|MUR|MVR|MWK|MXN|MXV|MYR|MZN|NAD|NGN|NIO|NOK|NPR|NZD|OMR|PAB|PEN|PGK|PHP|PKR|PLN|PYG|QAR|RON|RSD|RUB|RWF|SAR|SBD|SCR|SDG|SEK|SGD|SHP|SLL|SOS|SRD|SSP|STN|SVC|SYP|SZL|THB|TJS|TMT|TND|TOP|TRY|TTD|TWD|TZS|UAH|UGX|USD|USN|UYI|UYU|UYW|UZS|VES|VND|VUV|WST|XAF|XAG|XAU";

    // Validate for global currency codes (https://en.wikipedia.org/wiki/ISO_4217)
    @Pattern(message = "currency.code.invalid",
             regexp = CODES)
    @NotBlank(message = "currency.code.required")
    private String currency;

    @NotNull(message = "price.required")
    private BigDecimal price;

    @NotNull(message = "vehicle_id.required")
    @Column(unique=true)
    @JsonProperty("vehicle_id")
    private Long vehicleId;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public Price() {
    }

    public Price(String currency, BigDecimal price, Long vehicleId, Long id) {
        this.currency = currency;
        this.price = price;
        this.vehicleId = vehicleId;
        this.id = id;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Added explicit get method for price id to expose the id to the API.
     * Without this addition the id is not included.
     * We don't expose the set method protect the id from mutation.
     * @return
     */
    @JsonProperty("price_id")
    public Long getPriceId() {
        return id;
    }

}
