package io.github.cavvar.orderservice.controller;

import io.github.cavvar.orderservice.api.OrdersApi;
import io.github.cavvar.orderservice.model.Card;
import io.github.cavvar.orderservice.model.Item;
import io.github.cavvar.orderservice.model.NewOrder;
import io.github.cavvar.orderservice.model.Order;
import io.github.cavvar.orderservice.service.OrderService;
import io.smallrye.common.annotation.Blocking;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

public class OrdersApiImpl implements OrdersApi {

    @Inject
    OrderService orderService;

    @Override
    @Transactional
    @Blocking
    public Response addItemToOrder(Integer orderId, Integer itemId) {
        try {
            orderService.addItemToOrder(orderId, itemId);
        } catch (NullPointerException ex) {
            return Response.status(Response.Status.NOT_FOUND).entity("Order or Item was not found").type(MediaType.TEXT_PLAIN_TYPE).build();
        }
        return Response.status(Response.Status.OK).entity("A new item was added to the order").type(MediaType.TEXT_PLAIN_TYPE).build();
    }

    @Override
    @Transactional
    @Blocking
    public Response createOrder(NewOrder newOrder) {
        if (newOrder.address == null || newOrder.customer == null || newOrder.card == null || newOrder.items == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid Input").type(MediaType.TEXT_PLAIN_TYPE).build();
        }
        try {
            final Order newCustomerOrder = orderService.postOrder(newOrder);
            return Response.status(Response.Status.OK).entity(newCustomerOrder).type(MediaType.APPLICATION_JSON_TYPE).build();
        } catch (IllegalStateException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).type(MediaType.TEXT_PLAIN_TYPE).build();
        }
    }

    @Override
    @Transactional
    @Blocking
    public Response deleteItemFromOrder(Integer orderId, Integer itemId) {
        try {
            orderService.deleteItemFromOrder(orderId, itemId);
        } catch (NullPointerException | NoSuchElementException ex) {
            return Response.status(Response.Status.NOT_FOUND).entity("Order or Item were not found").type(MediaType.TEXT_PLAIN_TYPE).build();
        }
        return Response.status(Response.Status.OK).entity("Item was successfully deleted").type(MediaType.TEXT_PLAIN_TYPE).build();
    }

    @Override
    @Transactional
    @Blocking
    public Response deleteOrder(Integer orderId) {
        try {
            orderService.deleteOrder(orderId);
        } catch (IllegalArgumentException | NullPointerException ex) {
            return Response.status(Response.Status.NOT_FOUND).entity("Order was not found").type(MediaType.TEXT_PLAIN_TYPE).build();
        }
        return Response.status(Response.Status.OK).entity("The order was successfully deleted").type(MediaType.TEXT_PLAIN_TYPE).build();
    }

    @Override
    @Blocking
    public Response getAllItemsFromOrder(Integer orderId) {
        try {
            final List<Item> allItemsFromOrder = orderService.getAllItemsFromOrder(orderId);
            return Response.status(Response.Status.OK).entity(allItemsFromOrder).type(MediaType.APPLICATION_JSON_TYPE).build();
        } catch (NullPointerException ex) {
            return Response.status(Response.Status.NOT_FOUND).entity("Order was not found").type(MediaType.TEXT_PLAIN_TYPE).build();
        }
    }

    @Override
    @Blocking
    public Response getAllOrders() {
        final List<Order> allOrders = orderService.getAllOrders();
        return Response.status(Response.Status.OK).entity(allOrders).type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    @Override
    @Blocking
    public Response getItemFromOrder(Integer orderId, Integer itemId) {
        try {
            final Item itemFromOrder = orderService.getItemFromOrder(orderId, itemId);
            return Response.status(Response.Status.OK).entity(itemFromOrder).type(MediaType.APPLICATION_JSON_TYPE).build();
        } catch (NullPointerException | NoSuchElementException ex) {
            return Response.status(Response.Status.NOT_FOUND).entity("Order or Item could not be found").type(MediaType.TEXT_PLAIN_TYPE).build();
        }
    }

    @Override
    @Blocking
    public Response getOrder(Integer orderId) {
        final Order retrievedOrder = orderService.getOrder(orderId);
        if (Objects.isNull(retrievedOrder)) {
            return Response.status(Response.Status.NOT_FOUND).entity("Order was not found").type(MediaType.TEXT_PLAIN_TYPE).build();
        }
        return Response.status(Response.Status.OK).entity(retrievedOrder).type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    @Override
    @Transactional
    @Blocking
    public Response updateCardOfOrder(Integer orderId, Card card) {
        try {
            orderService.updateCardOfOrder(orderId, card);
        } catch (NullPointerException ex) {
            return Response.status(Response.Status.NOT_FOUND).entity("Order was not found").type(MediaType.TEXT_PLAIN_TYPE).build();
        }
        return Response.status(Response.Status.OK).entity("Card Information was successfully updated").type(MediaType.TEXT_PLAIN_TYPE).build();
    }
}
