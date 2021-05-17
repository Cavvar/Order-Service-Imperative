package io.github.cavvar.services;

import io.github.cavvar.models.Address;
import io.github.cavvar.models.Card;
import io.github.cavvar.models.Customer;
import io.github.cavvar.models.Item;
import io.github.cavvar.models.NewOrder;
import io.github.cavvar.models.Order;
import io.github.cavvar.models.PaymentRequest;
import io.github.cavvar.models.PaymentResponse;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.InvocationCallback;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

@ApplicationScoped
public class OrderService {

    private static final int timeoutInSeconds = 5;

    @Inject
    EntityManager entityManager;

    @Inject
    @RestClient
    PaymentService paymentService;

    public List<Order> getAllOrders() {
        return entityManager.createQuery("SELECT o FROM orders o", Order.class).getResultList();
    }

    public Order postOrder(NewOrder newOrder) {
        try {
            // Validate new order input
            if (newOrder.address == null || newOrder.customer == null || newOrder.card == null || newOrder.items == null) {
                throw new WebApplicationException("Invalid Input", 400);
            }
            // Retrieve newOrder data through hypermedia links
            final Client client = ClientBuilder.newClient();
            final Future<Address> address = client.target(newOrder.address).request().async().get(new OrderServiceInvocationCallback<>());
            final Future<Customer> customer = client.target(newOrder.customer).request().async().get(new OrderServiceInvocationCallback<>());
            final Future<Card> card = client.target(newOrder.card).request().async().get(new OrderServiceInvocationCallback<>());
            final Future<List<Item>> items = client.target(newOrder.items).request().async().get(new OrderServiceInvocationCallback<>());
            // Calculate total sum to be paid
            final double totalSum = items.get(timeoutInSeconds, TimeUnit.SECONDS)
                    .stream()
                    .mapToDouble(item -> item.getQuantity() * item.getUnitPrice())
                    .sum();
            // Call to Payment Service
            final PaymentRequest paymentRequest = new PaymentRequest(
                    address.get(timeoutInSeconds, TimeUnit.SECONDS),
                    card.get(timeoutInSeconds, TimeUnit.SECONDS),
                    customer.get(timeoutInSeconds, TimeUnit.SECONDS),
                    totalSum);
            final PaymentResponse paymentResponse = paymentService.getPaymentValidation(paymentRequest);
            if (!paymentResponse.isAuthorised()) {
                throw new WebApplicationException("Payment was not authorised!", 406);
            }
            final Order newCustomerOrder = new Order(
                    customer.get(timeoutInSeconds, TimeUnit.SECONDS),
                    address.get(timeoutInSeconds, TimeUnit.SECONDS),
                    card.get(timeoutInSeconds, TimeUnit.SECONDS),
                    items.get(timeoutInSeconds, TimeUnit.SECONDS),
                    Calendar.getInstance().getTime(),
                    totalSum);
            entityManager.persist(newCustomerOrder);
            return newCustomerOrder;
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            throw new IllegalStateException(String.format("Unable to create order. %s", ex.getMessage()));
        }
    }

    public Order getOrder(String orderId) {
        return retrieveOrder(orderId);
    }

    public void deleteOrder(String orderId) {
        final Order orderToBeDeleted = retrieveOrder(orderId);
        entityManager.remove(orderToBeDeleted);
    }

    public List<Item> getAllItemsFromOrder(String orderId) {
        return retrieveOrder(orderId).getItems();
    }

    public void addItemToOrder(String orderId, Item itemToAdd) {
        retrieveOrder(orderId).getItems().add(itemToAdd);
    }

    public Item getItemFromOrder(String orderId, String itemId) {
        final Optional<Item> retrievedItem = retrieveOrder(orderId).getItems().stream().filter(item -> item.getId().equals(itemId)).findFirst();
        if (retrievedItem.isEmpty()) {
            throw new WebApplicationException("Item was not found", 404);
        }
        return retrievedItem.get();
    }

    public void deleteItemFromOrder(String orderId, String itemId) {
        final Order retrievedOrder = retrieveOrder(orderId);
        final List<Item> newItems = retrievedOrder.getItems().stream().filter(item -> !item.getId().equals(itemId)).collect(Collectors.toList());
        retrievedOrder.getItems().clear();
        retrievedOrder.getItems().addAll(newItems);
        entityManager.flush();
    }

    private Order retrieveOrder(String orderId) {
        final Order retrievedOrder = entityManager.find(Order.class, orderId);
        if (Objects.isNull(retrievedOrder)) {
            throw new WebApplicationException("Order was not found", 404);
        }
        return retrievedOrder;
    }

    private static class OrderServiceInvocationCallback<T> implements InvocationCallback<T> {
        @Override
        public void completed(T t) {

        }

        @Override
        public void failed(Throwable throwable) {

        }
    }
}
