package io.github.cavvar.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "items")
public class Item {
    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private int id;
    private String name;
    private int quantity;
    private double unitPrice;
}
