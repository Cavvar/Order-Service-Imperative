package io.github.cavvar.services.address;

import io.github.cavvar.models.Address;

import javax.annotation.Priority;
import javax.enterprise.inject.Alternative;
import javax.inject.Singleton;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Alternative
@Priority(1)
@Singleton
public class MockAddressService extends LiveAddressService {
    @Override
    public Address getAddress(URI address) {
        final List<Address> addresses = new ArrayList<>();
        addresses.add(new Address(5, "123", "ExampleStreet", "Berlin", "12345", "Germany"));
        addresses.add(new Address(6, "1", "Wall Street", "New York", "67234", "United States of America"));
        addresses.add(new Address(7, "244", "Leopoldstraße", "Munich", "80678", "Germany"));
        addresses.add(new Address(8, "67", "Via Roma", "Rome", "33448", "Italy"));
        final int randomIndex = (int) (Math.random() * 3);
        return addresses.get(randomIndex);
    }
}
