package io.github.cavvar.orderservice.service.item;

import io.github.cavvar.orderservice.model.Item;
import io.github.cavvar.orderservice.utility.Constants;
import io.github.cavvar.orderservice.utility.OrderServiceInvocationCallback;

import javax.enterprise.context.Dependent;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.net.URI;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Dependent
public class LiveItemService {
    public List<Item> getItems(URI items) throws ExecutionException, InterruptedException, TimeoutException {
        final Client client = ClientBuilder.newClient();
        final Future<List<Item>> futureItems = client.target(items).request().async().get(new OrderServiceInvocationCallback<>());
        return futureItems.get(Constants.TIMEOUT_VALUE, TimeUnit.SECONDS);
    }
}
