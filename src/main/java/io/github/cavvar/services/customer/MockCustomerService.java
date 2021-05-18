package io.github.cavvar.services.customer;

import io.github.cavvar.models.Address;
import io.github.cavvar.models.Card;
import io.github.cavvar.models.Customer;

import javax.annotation.Priority;
import javax.enterprise.inject.Alternative;
import javax.inject.Singleton;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Alternative
@Priority(1)
@Singleton
public class MockCustomerService extends LiveCustomerService {
    @Override
    public Customer getCustomer(URI customer) {
        final List<Customer> customers = new ArrayList<>();
        final Address exampleAddress = new Address(UUID.randomUUID().toString(), "123", "Example Street", "Cologne", "55432", "Germany");
        final Card exampleCard = new Card(UUID.randomUUID().toString(), "1234-5678-9012-3456", "01/99", "123");
        customers.add(
                new Customer(UUID.randomUUID().toString(), "Max", "Mustermann", "Mustername",
                        Collections.singletonList(exampleAddress),
                        Collections.singletonList(exampleCard)));
        customers.add(
                new Customer(UUID.randomUUID().toString(), "Alberta", "FamilyName", "RandomOnlineUsername",
                        Collections.singletonList(exampleAddress),
                        Collections.singletonList(exampleCard)));
        customers.add(
                new Customer(UUID.randomUUID().toString(), "Evelyn", "Schmidt", "EvelynSchmidt",
                        Collections.singletonList(exampleAddress),
                        Collections.singletonList(exampleCard)));
        customers.add(
                new Customer(UUID.randomUUID().toString(), "Alex", "Schneider", "AlexSchneider",
                        Collections.singletonList(exampleAddress),
                        Collections.singletonList(exampleCard)));
        final int randomIndex = (int) (Math.random() * 3);
        return customers.get(randomIndex);
    }
}
