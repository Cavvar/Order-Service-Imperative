package io.github.cavvar.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Address {
    @Id
    private String id;
    private String number;
    private String street;
    private String city;
    private String postcode;
    private String country;

    public Address(String number, String street, String city, String postcode, String country) {
        this.number = number;
        this.street = street;
        this.city = city;
        this.postcode = postcode;
        this.country = country;
    }
}
