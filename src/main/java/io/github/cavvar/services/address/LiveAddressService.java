package io.github.cavvar.services.address;

import io.github.cavvar.models.Address;
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
public class LiveAddressService {
    public Address getAddress(URI address) throws ExecutionException, InterruptedException, TimeoutException {
        final Client client = ClientBuilder.newClient();
        final Future<Address> futureAddress = client.target(address).request().async().get(new OrderServiceInvocationCallback<>());
        return futureAddress.get(Constants.TIMEOUT_VALUE, TimeUnit.SECONDS);
    }
}
