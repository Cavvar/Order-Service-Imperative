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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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

    public Response getAllOrders() {
        final List<Order> allOrders = entityManager.createQuery("SELECT o FROM orders o", Order.class).getResultList();
        return Response.status(Response.Status.OK).entity(allOrders).type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    public Response postOrder(NewOrder newOrder) {
        try {
            // Validate new order input
            if (newOrder.address == null || newOrder.customer == null || newOrder.card == null || newOrder.items == null) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Invalid Input").type(MediaType.TEXT_PLAIN_TYPE).build();
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
                return Response.status(Response.Status.NOT_ACCEPTABLE).entity("Payment was not authorised!").type(MediaType.TEXT_PLAIN_TYPE).build();
            }
            final Order newCustomerOrder = new Order(
                    customer,
                    address,
                    card,
                    items,
                    Calendar.getInstance().getTime(),
                    totalSum);
            entityManager.persist(newCustomerOrder);
            return Response.status(Response.Status.OK).entity(newCustomerOrder).type(MediaType.APPLICATION_JSON_TYPE).build();
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            throw new IllegalStateException(String.format("Unable to create order. %s", ex.getMessage()));
        }
    }

    public Response getOrder(int orderId) {
        final Order retrievedOrder = retrieveOrder(orderId);
        if (Objects.nonNull(retrievedOrder)) {
            return Response.status(Response.Status.OK).entity(retrievedOrder).type(MediaType.APPLICATION_JSON_TYPE).build();
        }
        return Response.status(Response.Status.NOT_FOUND).entity("Order was not found").type(MediaType.TEXT_PLAIN_TYPE).build();
    }

    public Response deleteOrder(int orderId) {
        final Order orderToBeDeleted = retrieveOrder(orderId);
        if (Objects.nonNull(orderToBeDeleted)) {
            entityManager.remove(orderToBeDeleted);
            return Response.status(Response.Status.OK).entity("The order was successfully deleted").type(MediaType.TEXT_PLAIN_TYPE).build();
        }
        return Response.status(Response.Status.NOT_FOUND).entity("Order was not found").type(MediaType.TEXT_PLAIN_TYPE).build();
    }

    public Response getAllItemsFromOrder(int orderId) {
        final Order retrievedOrder = retrieveOrder(orderId);
        if (Objects.nonNull(retrievedOrder)) {
            return Response.status(Response.Status.OK).entity(retrievedOrder.getItems()).type(MediaType.APPLICATION_JSON_TYPE).build();
        }
        return Response.status(Response.Status.NOT_FOUND).entity("Order was not found").type(MediaType.TEXT_PLAIN_TYPE).build();
    }

    public Response addItemToOrder(int orderId, Item itemToAdd) {
        final Order retrievedOrder = retrieveOrder(orderId);
        if (Objects.nonNull(retrievedOrder)) {
            retrievedOrder.getItems().add(itemToAdd);
            return Response.status(Response.Status.OK).entity("A new item was added to the order").type(MediaType.TEXT_PLAIN_TYPE).build();
        }
        return Response.status(Response.Status.NOT_FOUND).entity("Order was not found").type(MediaType.TEXT_PLAIN_TYPE).build();
    }

    public Response getItemFromOrder(int orderId, int itemId) {
        final Order retrievedOrder = retrieveOrder(orderId);
        if (Objects.nonNull(retrievedOrder)) {
            final Optional<Item> retrievedItem = retrievedOrder.getItems().stream().filter(item -> item.getItemId() == itemId).findFirst();
            if (retrievedItem.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND).entity("Item could not be found").type(MediaType.TEXT_PLAIN_TYPE).build();
            }
            return Response.status(Response.Status.OK).entity(retrievedItem.get()).type(MediaType.APPLICATION_JSON_TYPE).build();
        }
        return Response.status(Response.Status.NOT_FOUND).entity("Order could not be found").type(MediaType.TEXT_PLAIN_TYPE).build();
    }

    public Response deleteItemFromOrder(int orderId, int itemId) {
        final Order retrievedOrder = retrieveOrder(orderId);
        if (Objects.nonNull(retrievedOrder)) {
            final List<Item> newItems = retrievedOrder.getItems().stream().filter(item -> !(item.getItemId() == itemId)).collect(Collectors.toList());
            retrievedOrder.getItems().clear();
            retrievedOrder.getItems().addAll(newItems);
            entityManager.flush();
            return Response.status(Response.Status.OK).entity("Item was successfully deleted").type(MediaType.TEXT_PLAIN_TYPE).build();
        }
        return Response.status(Response.Status.NOT_FOUND).entity("Order or Item were not found").type(MediaType.TEXT_PLAIN_TYPE).build();
    }

    private Order retrieveOrder(int orderId) {
        return entityManager.find(Order.class, orderId);
    }
}
