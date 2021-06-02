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
        return orderService.addItemToOrder(orderId, itemId).onItem().transform(wasAdded -> wasAdded ? Response.ok("A new item was added to the order").type(MediaType.TEXT_PLAIN_TYPE) : Response.status(Response.Status.NOT_FOUND).entity("Order or Item was not found").type(MediaType.TEXT_PLAIN_TYPE)).onItem().transform(Response.ResponseBuilder::build);
    }

    @Override
    @Transactional
    public Uni<Response> createOrder(NewOrder newOrder) {
        if (newOrder.address == null || newOrder.customer == null || newOrder.card == null || newOrder.items == null) {
            return Uni.createFrom().item(Response.status(Response.Status.BAD_REQUEST).entity("Invalid Input. The request payload contains invalid data").type(MediaType.TEXT_PLAIN_TYPE).build());
        }
        return orderService.postOrder(newOrder).onFailure().recoverWithUni(unused -> Uni.createFrom().nullItem()).onItem().transform(order -> order != null ? Response.status(Response.Status.OK).entity(order).type(MediaType.APPLICATION_JSON_TYPE) : Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Server Error. Unable to create order").type(MediaType.TEXT_PLAIN_TYPE)).onItem().transform(Response.ResponseBuilder::build);
    }

    @Override
    @Transactional
    public Uni<Response> deleteItemFromOrder(Integer orderId, Integer itemId) {
        return orderService.deleteItemFromOrder(orderId, itemId).onItem().transform(wasDeleted -> wasDeleted ? Response.ok("Item was successfully deleted").type(MediaType.TEXT_PLAIN_TYPE) : Response.status(Response.Status.NOT_FOUND).entity("Order or Item were not found").type(MediaType.TEXT_PLAIN_TYPE)).onItem().transform(Response.ResponseBuilder::build);
    }

    @Override
    @Transactional
    public Uni<Response> deleteOrder(Integer orderId) {
        return orderService.deleteOrder(orderId).onItem().transform(wasDeleted -> wasDeleted ? Response.ok("The order was successfully deleted").type(MediaType.TEXT_PLAIN_TYPE) : Response.status(Response.Status.NOT_FOUND).entity("Order was not found").type(MediaType.TEXT_PLAIN_TYPE)).onItem().transform(Response.ResponseBuilder::build);
    }

    @Override
    public Uni<Response> getAllItemsFromOrder(Integer orderId) {
        return orderService.getAllItemsFromOrder(orderId).onItem().transform(items -> items != null ? Response.ok(items).type(MediaType.APPLICATION_JSON_TYPE) : Response.status(Response.Status.NOT_FOUND).entity("Order was not found").type(MediaType.TEXT_PLAIN_TYPE)).onItem().transform(Response.ResponseBuilder::build);
    }

    @Override
    public Uni<Response> getAllOrders() {
        return orderService.getAllOrders().onItem().transform(orders -> Response.ok(orders).type(MediaType.APPLICATION_JSON_TYPE)).onItem().transform(Response.ResponseBuilder::build);
    }

    @Override
    public Uni<Response> getItemFromOrder(Integer orderId, Integer itemId) {
        return orderService.getItemFromOrder(orderId, itemId).onItem().transform(item -> item != null ? Response.ok(item).type(MediaType.APPLICATION_JSON_TYPE) : Response.status(Response.Status.NOT_FOUND).entity("Order or Item could not be found").type(MediaType.TEXT_PLAIN_TYPE)).onItem().transform(Response.ResponseBuilder::build);
    }

    @Override
    public Uni<Response> getOrder(Integer orderId) {
        return orderService.getOrder(orderId).onItem().transform(order -> order != null ? Response.ok(order).type(MediaType.APPLICATION_JSON_TYPE) : Response.status(Response.Status.NOT_FOUND).entity("Order was not found")).onItem().transform(Response.ResponseBuilder::build);
    }

    @Override
    @Transactional
    public Uni<Response> updateCardOfOrder(Integer orderId, Card card) {
        return orderService.updateCardOfOrder(orderId, card).onItem().transform(wasUpdated -> wasUpdated ? Response.ok("Card Information was successfully updated").type(MediaType.TEXT_PLAIN_TYPE) : Response.status(Response.Status.NOT_FOUND).entity("Order was not found").type(MediaType.TEXT_PLAIN_TYPE)).onItem().transform(Response.ResponseBuilder::build);
    }
}
