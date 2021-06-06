package io.github.cavvar.orderservice.controller;

import io.github.cavvar.orderservice.model.Card;
import io.github.cavvar.orderservice.model.NewOrder;
import io.github.cavvar.orderservice.service.OrderService;
import io.smallrye.mutiny.Uni;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class OrdersApiImpl implements OrdersApi {

    @Inject
    OrderService orderService;

    @Override
    @Transactional
    public Uni<Response> addItemToOrder(Integer orderId, Integer itemId) {
        return orderService.addItemToOrder(orderId, itemId)
                .onItem().transform(item -> Response.ok(item).type(MediaType.APPLICATION_JSON_TYPE).build())
                .onFailure().recoverWithItem(Response.status(Response.Status.NOT_FOUND).entity("Order or Item was not found").type(MediaType.TEXT_PLAIN_TYPE).build());
    }

    @Override
    @Transactional
    public Uni<Response> createOrder(NewOrder newOrder) {
        if (newOrder.address == null || newOrder.customer == null || newOrder.card == null || newOrder.items == null) {
            return Uni.createFrom().item(Response.status(Response.Status.BAD_REQUEST).entity("Invalid Input. The request payload contains invalid data").type(MediaType.TEXT_PLAIN_TYPE).build());
        }
        return orderService.postOrder(newOrder)
                .onItem().transform(order -> Response.ok(order).type(MediaType.APPLICATION_JSON_TYPE).build())
                .onFailure().recoverWithItem(error -> Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Server Error. Unable to create order").type(MediaType.TEXT_PLAIN_TYPE).build());
    }

    @Override
    @Transactional
    public Uni<Response> deleteItemFromOrder(Integer orderId, Integer itemId) {
        return orderService.deleteItemFromOrder(orderId, itemId)
                .onItem().transform(unused -> Response.noContent().build())
                .onFailure().recoverWithItem(Response.status(Response.Status.NOT_FOUND).entity("Order or Item were not found").type(MediaType.TEXT_PLAIN_TYPE).build());
    }

    @Override
    @Transactional
    public Uni<Response> deleteOrder(Integer orderId) {
        return orderService.deleteOrder(orderId)
                .onItem().transform(unused -> Response.noContent().build())
                .onFailure().recoverWithItem(Response.status(Response.Status.NOT_FOUND).entity("Order was not found").type(MediaType.TEXT_PLAIN_TYPE).build());
    }

    @Override
    public Uni<Response> getAllItemsFromOrder(Integer orderId) {
        return orderService.getAllItemsFromOrder(orderId)
                .onItem().transform(items -> Response.ok(items).type(MediaType.APPLICATION_JSON_TYPE).build())
                .onFailure().recoverWithItem(Response.status(Response.Status.NOT_FOUND).entity("Order was not found").type(MediaType.TEXT_PLAIN_TYPE).build());
    }

    @Override
    public Uni<Response> getAllOrders() {
        return orderService.getAllOrders().onItem().transform(orders -> Response.ok(orders).type(MediaType.APPLICATION_JSON_TYPE).build());
    }

    @Override
    public Uni<Response> getItemFromOrder(Integer orderId, Integer itemId) {
        return orderService.getItemFromOrder(orderId, itemId)
                .onItem().transform(item -> Response.ok(item).type(MediaType.APPLICATION_JSON_TYPE).build())
                .onFailure().recoverWithItem(Response.status(Response.Status.NOT_FOUND).entity("Order or Item could not be found").type(MediaType.TEXT_PLAIN_TYPE).build());
    }

    @Override
    public Uni<Response> getOrder(Integer orderId) {
        return orderService.getOrder(orderId)
                .onItem().ifNotNull().transform(order -> Response.ok(order).type(MediaType.APPLICATION_JSON_TYPE).build())
                .onItem().ifNull().continueWith(Response.status(Response.Status.NOT_FOUND).entity("Order was not found").build());
    }

    @Override
    @Transactional
    public Uni<Response> updateCardOfOrder(Integer orderId, Card card) {
        return orderService.updateCardOfOrder(orderId, card)
                .onItem().transform(newCard -> Response.ok(newCard).type(MediaType.APPLICATION_JSON_TYPE).build())
                .onFailure().recoverWithItem(Response.status(Response.Status.NOT_FOUND).entity("Order was not found").type(MediaType.TEXT_PLAIN_TYPE).build());
    }
}
