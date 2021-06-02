package io.github.cavvar.orderservice.service.card;

import io.github.cavvar.orderservice.model.Card;
import io.github.cavvar.orderservice.utility.OrderServiceInvocationCallback;

import javax.enterprise.context.Dependent;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.net.URI;
import java.util.concurrent.Future;

@Dependent
public class LiveCardService {
    public Future<Card> getCard(URI card) {
        final Client client = ClientBuilder.newClient();
        return client.target(card).request().async().get(new OrderServiceInvocationCallback<>());
    }
}
