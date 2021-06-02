package io.github.cavvar.orderservice.service.item;

import io.github.cavvar.orderservice.model.Item;
import io.github.cavvar.orderservice.utility.OrderServiceInvocationCallback;

import javax.enterprise.context.Dependent;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.net.URI;
import java.util.List;
import java.util.concurrent.Future;

@Dependent
public class LiveItemService {
    public Future<List<Item>> getItems(URI items) {
        final Client client = ClientBuilder.newClient();
        return client.target(items).request().async().get(new OrderServiceInvocationCallback<>());
    }
}
