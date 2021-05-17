package io.github.cavvar.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "orders")
public class Order {
    @Id
    @GeneratedValue
    private String id;
    @Embedded
    private Customer customer;
    @Embedded
    private Address address;
    @Embedded
    private Card card;
    @ElementCollection
    private List<Item> items;
    private Date date;
    private double total;

    public Order(Customer customer, Address address, Card card, List<Item> items, Date date, double total) {
        this.customer = customer;
        this.address = address;
        this.card = card;
        this.items = items;
        this.date = date;
        this.total = total;
    }
}
