package io.github.cavvar.services.customer;

import io.github.cavvar.models.Customer;
import io.github.cavvar.utility.Constants;
import io.github.cavvar.utility.OrderServiceInvocationCallback;

import javax.enterprise.context.Dependent;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.net.URI;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Dependent
public class LiveCustomerService {
    public Customer getCustomer(URI customer) throws ExecutionException, InterruptedException, TimeoutException {
        final Client client = ClientBuilder.newClient();
        final Future<Customer> futureCustomer = client.target(customer).request().async().get(new OrderServiceInvocationCallback<>());
        return futureCustomer.get(Constants.timeoutValue, TimeUnit.SECONDS);
    }
}
