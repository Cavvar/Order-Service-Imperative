package io.github.cavvar.orderservice.service.card;

import io.github.cavvar.orderservice.model.Card;
import io.github.cavvar.orderservice.utility.Constants;
import io.github.cavvar.orderservice.utility.OrderServiceInvocationCallback;

import javax.enterprise.context.Dependent;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.net.URI;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Dependent
public class LiveCardService {
    public Card getCard(URI card) throws ExecutionException, InterruptedException, TimeoutException {
        final Client client = ClientBuilder.newClient();
        final Future<Card> futureCard = client.target(card).request().async().get(new OrderServiceInvocationCallback<>());
        return futureCard.get(Constants.TIMEOUT_VALUE, TimeUnit.SECONDS);
    }
}
