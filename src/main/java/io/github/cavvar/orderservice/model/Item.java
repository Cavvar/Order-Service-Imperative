package io.github.cavvar.orderservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "items")
public class Item {
    @Id
    @Column(name = "item_id")
    private int id;
    private String name;
    private int quantity;
    private double unitPrice;
}
