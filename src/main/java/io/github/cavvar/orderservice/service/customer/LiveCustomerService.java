package io.github.cavvar.orderservice.service.customer;

import io.github.cavvar.orderservice.model.Customer;
import io.github.cavvar.orderservice.utility.OrderServiceInvocationCallback;

import javax.enterprise.context.Dependent;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.net.URI;
import java.util.concurrent.Future;

@Dependent
public class LiveCustomerService {
    public Future<Customer> getCustomer(URI customer) {
        final Client client = ClientBuilder.newClient();
        return client.target(customer).request().async().get(new OrderServiceInvocationCallback<>());
    }
}
