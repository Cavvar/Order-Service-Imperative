package io.github.cavvar.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.Id;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable//https://memorynotfound.com/hibernate-embeddable-embedded-annotation-example/
public class Customer {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String userName;
    @ElementCollection
    private List<Address> addresses;
    @ElementCollection
    private List<Card> cards;

    public Customer(String firstName, String lastName, String userName, List<Address> addresses, List<Card> cards) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.addresses = addresses;
        this.cards = cards;
    }
}
