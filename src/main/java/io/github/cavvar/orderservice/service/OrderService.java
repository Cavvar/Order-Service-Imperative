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
import io.github.cavvar.orderservice.utility.PaymentNotAuthorisedException;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

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

    public Uni<List<Order>> getAllOrders() {
        return Uni.createFrom().item(entityManager.createQuery("SELECT o FROM orders o", Order.class).getResultList());
    }

    public Uni<Order> postOrder(NewOrder newOrder) {
        final Uni<Address> addressUni = Uni.createFrom().future(addressService.getAddress(newOrder.address));
        final Uni<Customer> customerUni = Uni.createFrom().future(customerService.getCustomer(newOrder.customer));
        final Uni<Card> cardUni = Uni.createFrom().future(cardService.getCard(newOrder.card));
        final Uni<List<Item>> itemsUni = Uni.createFrom().future(itemService.getItems(newOrder.items));
        return Uni.combine().all().unis(addressUni, customerUni, cardUni, itemsUni).asTuple().onItem().transform(combinedObjects -> new Order(combinedObjects.getItem2(), combinedObjects.getItem1(), combinedObjects.getItem3(), combinedObjects.getItem4(), Calendar.getInstance().getTime(), calculateTotal(combinedObjects.getItem4()))).flatMap(order -> {
            // Check if payment was authorised
            final PaymentRequest paymentRequest = new PaymentRequest(order.getAddress(), order.getCard(), order.getCustomer(), order.getTotal());
            final Uni<PaymentResponse> paymentResponseUni = Uni.createFrom().future(paymentService.getPayment(paymentRequest));
            return paymentResponseUni.flatMap(paymentResponse -> {
                if (!paymentResponse.isAuthorised()) {
                    return Uni.createFrom().failure(new PaymentNotAuthorisedException());
                }
                return Uni.createFrom().item(order);
            });
        }).onItem().invoke(order -> entityManager.persist(order));
    }

    public Uni<Order> getOrder(int orderId) {
        return retrieveOrder(orderId);
    }

    public Uni<Boolean> updateCardOfOrder(int orderId, Card newCard) {
        return retrieveOrder(orderId).map(order -> {
            order.setCard(newCard);
            entityManager.flush();
            return true;
        }).onFailure().recoverWithItem(false);
    }

    public Uni<Boolean> deleteOrder(int orderId) {
        return retrieveOrder(orderId).map(order -> {
            entityManager.remove(order);
            return true;
        }).onFailure().recoverWithItem(false);
    }

    public Uni<List<Item>> getAllItemsFromOrder(int orderId) {
        return retrieveOrder(orderId).map(Order::getItems).onFailure().recoverWithNull();
    }

    public Uni<Boolean> addItemToOrder(int orderId, int itemId) {
        return Uni.combine().all().unis(retrieveOrder(orderId), Uni.createFrom().item(entityManager.find(Item.class, itemId))).asTuple().map(combinedObjects -> {
            final Optional<Item> itemFromList = combinedObjects.getItem1().getItems().stream().filter(item -> item.getId() == itemId).findFirst();
            if (itemFromList.isPresent()) {
                final Item itemFromOptional = itemFromList.get();
                itemFromOptional.setQuantity(itemFromOptional.getQuantity() + combinedObjects.getItem2().getQuantity());
            } else {
                combinedObjects.getItem1().getItems().add(combinedObjects.getItem2());
            }
            combinedObjects.getItem1().setTotal(calculateTotal(combinedObjects.getItem1().getItems()));
            return true;
        }).onFailure().recoverWithItem(false);
    }

    public Uni<Item> getItemFromOrder(int orderId, int itemId) {
        return retrieveOrder(orderId).map(order -> getItemFromList(order.getItems(), itemId)).onFailure().recoverWithNull();
    }

    public Uni<Boolean> deleteItemFromOrder(int orderId, int itemId) {
        return retrieveOrder(orderId).map(order -> {
            final Item itemToBeDeleted = getItemFromList(order.getItems(), itemId);
            order.getItems().remove(itemToBeDeleted);
            order.setTotal(calculateTotal(order.getItems()));
            entityManager.flush();
            return true;
        }).onFailure().recoverWithItem(false);
    }

    private Uni<Order> retrieveOrder(int orderId) {
        return Uni.createFrom().item(entityManager.find(Order.class, orderId));
    }

    private double calculateTotal(List<Item> items) {
        return items.stream().mapToDouble(item -> item.getQuantity() * item.getUnitPrice()).sum();
    }

    private Item getItemFromList(List<Item> items, int itemId) {
        return items.stream().filter(item -> item.getId() == itemId).findFirst().get();
    }
}
