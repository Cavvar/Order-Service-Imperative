package io.github.cavvar.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Customer {
    private int customerId;
    private String firstName;
    private String lastName;
    private String userName;
    @ElementCollection
    private List<Address> addresses;
    @ElementCollection
    private List<Card> cards;
}
