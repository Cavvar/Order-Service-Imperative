package io.github.cavvar.orderservice.service.address;

import io.github.cavvar.orderservice.model.Address;

import javax.annotation.Priority;
import javax.enterprise.inject.Alternative;
import javax.inject.Singleton;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Alternative
@Priority(1)
@Singleton
public class MockAddressService extends LiveAddressService {
    @Override
    public Future<Address> getAddress(URI address) {
        final List<Address> addresses = new ArrayList<>();
        addresses.add(new Address(1, "123", "ExampleStreet", "Berlin", "12345", "Germany"));
        addresses.add(new Address(2, "1", "Wall Street", "New York", "67234", "United States of America"));
        addresses.add(new Address(3, "244", "Leopoldstraße", "Munich", "80678", "Germany"));
        addresses.add(new Address(4, "67", "Via Roma", "Rome", "33448", "Italy"));
        final int randomIndex = (int) (Math.random() * 3);
        return CompletableFuture.completedFuture(addresses.get(randomIndex));
    }
}
