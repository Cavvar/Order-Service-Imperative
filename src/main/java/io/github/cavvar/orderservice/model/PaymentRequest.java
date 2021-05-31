package io.github.cavvar.orderservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
    private Address address;
    private Card card;
    private Customer customer;
    private double amount;
}
