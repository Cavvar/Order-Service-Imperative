package io.github.cavvar.orderservice.controller;

import io.github.cavvar.orderservice.api.OrdersApi;
import io.github.cavvar.orderservice.model.Card;
import io.github.cavvar.orderservice.model.Item;
import io.github.cavvar.orderservice.model.NewOrder;
import io.github.cavvar.orderservice.model.Order;
import io.github.cavvar.orderservice.service.OrderService;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;
import java.util.List;

public class OrdersApiImpl implements OrdersApi {

    @Inject
    OrderService orderService;

    @Override
    @Transactional
    public Item addItemToOrder(Integer orderId, Integer itemId) {
        return orderService.addItemToOrder(orderId, itemId);
    }

    @Override
    @Transactional
    public Order createOrder(NewOrder newOrder) {
        if (newOrder.address == null || newOrder.customer == null || newOrder.card == null || newOrder.items == null) {
            throw new BadRequestException();
        }
        return orderService.postOrder(newOrder);
    }

    @Override
    @Transactional
    public void deleteItemFromOrder(Integer orderId, Integer itemId) {
        orderService.deleteItemFromOrder(orderId, itemId);
    }

    @Override
    @Transactional
    public void deleteOrder(Integer orderId) {
        orderService.deleteOrder(orderId);
    }

    @Override
    public List<Item> getAllItemsFromOrder(Integer orderId) {
        return orderService.getAllItemsFromOrder(orderId);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @Override
    public Item getItemFromOrder(Integer orderId, Integer itemId) {
        return orderService.getItemFromOrder(orderId, itemId);
    }

    @Override
    public Order getOrder(Integer orderId) {
        final Order retrievedOrder = orderService.getOrder(orderId);
        if (retrievedOrder == null) {
            throw new NullPointerException();
        }
        return retrievedOrder;
    }

    @Override
    @Transactional
    public Card updateCardOfOrder(Integer orderId, Card card) {
        return orderService.updateCardOfOrder(orderId, card);
    }
}
