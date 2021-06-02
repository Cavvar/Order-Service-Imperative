package io.github.cavvar.orderservice.service.address;

import io.github.cavvar.orderservice.model.Address;
import io.github.cavvar.orderservice.utility.OrderServiceInvocationCallback;

import javax.enterprise.context.Dependent;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.net.URI;
import java.util.concurrent.Future;

@Dependent
public class LiveAddressService {
    public Future<Address> getAddress(URI address) {
        final Client client = ClientBuilder.newClient();
        return client.target(address).request().async().get(new OrderServiceInvocationCallback<>());
    }
}