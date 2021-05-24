package io.github.cavvar.services.customer;

import io.github.cavvar.models.Customer;

import javax.annotation.Priority;
import javax.enterprise.inject.Alternative;
import javax.inject.Singleton;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Alternative
@Priority(1)
@Singleton
public class MockCustomerService extends LiveCustomerService {
    @Override
    public Customer getCustomer(URI customer) {
        final List<Customer> customers = new ArrayList<>();
        customers.add(
                new Customer(1, "Max", "Mustermann", "Mustername"));
        customers.add(
                new Customer(2, "Alberta", "FamilyName", "RandomOnlineUsername"));
        customers.add(
                new Customer(3, "Evelyn", "Schmidt", "EvelynSchmidt"));
        customers.add(
                new Customer(4, "Alex", "Schneider", "AlexSchneider"));
        final int randomIndex = (int) (Math.random() * 3);
        return customers.get(randomIndex);
    }
}
