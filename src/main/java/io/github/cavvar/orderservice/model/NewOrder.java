package io.github.cavvar.orderservice.model;

import org.hibernate.validator.constraints.URL;

import java.net.URI;

public class NewOrder {
    @URL
    public URI customer;
    @URL
    public URI address;
    @URL
    public URI card;
    @URL
    public URI items;
}
