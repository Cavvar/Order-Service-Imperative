package io.github.cavvar.services;

import io.github.cavvar.models.Address;
import io.github.cavvar.models.Card;
import io.github.cavvar.models.Customer;
import io.github.cavvar.models.Item;
import io.github.cavvar.models.NewOrder;
import io.github.cavvar.models.Order;
import io.github.cavvar.models.PaymentRequest;
import io.github.cavvar.models.PaymentResponse;
import io.github.cavvar.services.address.LiveAddressService;
import io.github.cavvar.services.card.LiveCardService;
import io.github.cavvar.services.customer.LiveCustomerService;
import io.github.cavvar.services.item.LiveItemService;
import io.github.cavvar.services.payment.LivePaymentService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.WebApplicationException;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

@ApplicationScoped
public class OrderService {

    @Inject
    EntityManager entityManager;

    @Inject
    LiveAddressService addressService;

    @Inject
    LiveCardService cardService;

    @Inject
    LiveCustomerService customerService;

    @Inject
    LiveItemService itemService;

    @Inject
    LivePaymentService paymentService;

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
            final Address address = addressService.getAddress(newOrder.address);
            final Customer customer = customerService.getCustomer(newOrder.customer);
            final Card card = cardService.getCard(newOrder.card);
            final List<Item> items = itemService.getItems(newOrder.items);
            // Calculate total sum to be paid
            final double totalSum = items
                    .stream()
                    .mapToDouble(item -> item.getQuantity() * item.getUnitPrice())
                    .sum();
            // Call to Payment Service
            final PaymentRequest paymentRequest = new PaymentRequest(
                    address,
                    card,
                    customer,
                    totalSum);
            final PaymentResponse paymentResponse = paymentService.getPayment(paymentRequest);
            if (!paymentResponse.isAuthorised()) {
                throw new WebApplicationException("Payment was not authorised!", 406);
            }
            final Order newCustomerOrder = new Order(
                    UUID.randomUUID().toString(),
                    customer,
                    address,
                    card,
                    items,
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
        final Optional<Item> retrievedItem = retrieveOrder(orderId).getItems().stream().filter(item -> item.getItemId().equals(itemId)).findFirst();
        if (retrievedItem.isEmpty()) {
            throw new WebApplicationException("Item was not found", 404);
        }
        return retrievedItem.get();
    }

    public void deleteItemFromOrder(String orderId, String itemId) {
        final Order retrievedOrder = retrieveOrder(orderId);
        final List<Item> newItems = retrievedOrder.getItems().stream().filter(item -> !item.getItemId().equals(itemId)).collect(Collectors.toList());
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
}
