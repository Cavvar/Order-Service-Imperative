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

@Alternative
@Priority(1)
@Singleton
public class MockCustomerService extends LiveCustomerService {
    @Override
    public Customer getCustomer(URI customer) {
        final List<Customer> customers = new ArrayList<>();
        final Address exampleAddress = new Address(5, "123", "Example Street", "Cologne", "55432", "Germany");
        final Card exampleCard = new Card(5, "1234-5678-9012-3456", "01/99", "123");
        customers.add(
                new Customer(1, "Max", "Mustermann", "Mustername",
                        Collections.singletonList(exampleAddress),
                        Collections.singletonList(exampleCard)));
        customers.add(
                new Customer(2, "Alberta", "FamilyName", "RandomOnlineUsername",
                        Collections.singletonList(exampleAddress),
                        Collections.singletonList(exampleCard)));
        customers.add(
                new Customer(3, "Evelyn", "Schmidt", "EvelynSchmidt",
                        Collections.singletonList(exampleAddress),
                        Collections.singletonList(exampleCard)));
        customers.add(
                new Customer(4, "Alex", "Schneider", "AlexSchneider",
                        Collections.singletonList(exampleAddress),
                        Collections.singletonList(exampleCard)));
        final int randomIndex = (int) (Math.random() * 3);
        return customers.get(randomIndex);
    }
}
