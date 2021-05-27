package io.github.cavvar.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "cards")
public class Card {
    @Id
    @Column(name = "card_id")
    private int id;
    private String longNum;
    private String expires;
    private String ccv;
}
