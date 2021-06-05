package io.github.cavvar.orderservice.service;

import io.github.cavvar.orderservice.model.Address;
import io.github.cavvar.orderservice.model.Card;
import io.github.cavvar.orderservice.model.Customer;
import io.github.cavvar.orderservice.model.Item;
import io.github.cavvar.orderservice.model.NewOrder;
import io.github.cavvar.orderservice.model.Order;
import io.github.cavvar.orderservice.model.PaymentRequest;
import io.github.cavvar.orderservice.model.PaymentResponse;
import io.github.cavvar.orderservice.service.address.LiveAddressService;
import io.github.cavvar.orderservice.service.card.LiveCardService;
import io.github.cavvar.orderservice.service.customer.LiveCustomerService;
import io.github.cavvar.orderservice.service.item.LiveItemService;
import io.github.cavvar.orderservice.service.payment.LivePaymentService;
import io.github.cavvar.orderservice.utility.exceptions.PaymentNotAuthorisedException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

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
            // Retrieve newOrder data through hypermedia links
            final Address address = addressService.getAddress(newOrder.address);
            final Customer customer = customerService.getCustomer(newOrder.customer);
            final Card card = cardService.getCard(newOrder.card);
            final List<Item> items = itemService.getItems(newOrder.items);
            // Calculate total sum to be paid
            final double totalSum = calculateTotal(items);
            // Call to Payment Service
            final PaymentRequest paymentRequest = new PaymentRequest(address, card, customer, totalSum);
            final PaymentResponse paymentResponse = paymentService.getPayment(paymentRequest);
            if (!paymentResponse.isAuthorised()) {
                throw new PaymentNotAuthorisedException();
            }
            final Order newCustomerOrder = new Order(customer, address, card, items, Calendar.getInstance().getTime(), totalSum);
            entityManager.persist(newCustomerOrder);
            return newCustomerOrder;
        } catch (InterruptedException | ExecutionException | TimeoutException | PaymentNotAuthorisedException ex) {
            throw new IllegalStateException(String.format("Unable to create order. %s", ex.getMessage()));
        }
    }

    public Order getOrder(int orderId) {
        return retrieveOrder(orderId);
    }

    public void updateCardOfOrder(int orderId, Card newCard) {
        final Order retrievedOrder = retrieveOrder(orderId);
        retrievedOrder.setCard(newCard);
        entityManager.flush();
    }

    public void deleteOrder(int orderId) {
        final Order orderToBeDeleted = retrieveOrder(orderId);
        entityManager.remove(orderToBeDeleted);
    }

    public List<Item> getAllItemsFromOrder(int orderId) {
        final Order retrievedOrder = retrieveOrder(orderId);
        return retrievedOrder.getItems();
    }

    public void addItemToOrder(int orderId, int itemId) {
        final Order retrievedOrder = retrieveOrder(orderId);
        final Item retrievedItemFromDB = entityManager.find(Item.class, itemId);
        final Optional<Item> optionalItem = retrievedOrder.getItems().stream().filter(item -> item.getId() == itemId).findFirst();
        if (optionalItem.isPresent()) {
            final Item existingItem = optionalItem.get();
            existingItem.setQuantity(existingItem.getQuantity() + retrievedItemFromDB.getQuantity());
        } else {
            retrievedOrder.getItems().add(retrievedItemFromDB);
        }
        retrievedOrder.setTotal(calculateTotal(retrievedOrder.getItems()));
        entityManager.flush();
    }

    public Item getItemFromOrder(int orderId, int itemId) {
        final Order retrievedOrder = retrieveOrder(orderId);
        return retrievedOrder.getItems().stream().filter(item -> item.getId() == itemId).findFirst().get();
    }

    public void deleteItemFromOrder(int orderId, int itemId) {
        final Order retrievedOrder = retrieveOrder(orderId);
        final Optional<Item> itemToBeDeleted = retrievedOrder.getItems().stream().filter(item -> item.getId() == itemId).findFirst();
        retrievedOrder.getItems().remove(itemToBeDeleted.get());
        retrievedOrder.setTotal(calculateTotal(retrievedOrder.getItems()));
        entityManager.flush();
    }

    private Order retrieveOrder(int orderId) {
        return entityManager.find(Order.class, orderId);
    }

    private double calculateTotal(List<Item> items) {
        return items.stream().mapToDouble(item -> item.getQuantity() * item.getUnitPrice()).sum();
    }
}
