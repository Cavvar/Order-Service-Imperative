package io.github.cavvar.resources;

import io.github.cavvar.models.Item;
import io.github.cavvar.models.NewOrder;
import io.github.cavvar.services.OrderService;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/orders")
public class OrderResource {

    @Inject
    OrderService orderService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllOrders() {
        return orderService.getAllOrders();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createOrder(NewOrder newOrder) {
        return orderService.postOrder(newOrder);
    }

    @GET
    @Path("{orderId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOrder(@PathParam("orderId") String orderId) {
        return orderService.getOrder(orderId);
    }

    @DELETE
    @Path("{orderId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response deleteOrder(@PathParam("orderId") String orderId) {
        return orderService.deleteOrder(orderId);
    }

    @GET
    @Path("{orderId}/items")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllItemsFromOrder(@PathParam("orderId") String orderId) {
        return orderService.getAllItemsFromOrder(orderId);
    }

    @POST
    @Path("{orderId}/items")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response addItemToOrder(@PathParam("orderId") String orderId, Item itemToAdd) {
        return orderService.addItemToOrder(orderId, itemToAdd);
    }

    @GET
    @Path("{orderId}/items/{itemId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getItemFromOrder(@PathParam("orderId") String orderId, @PathParam("itemId") String itemId) {
        return orderService.getItemFromOrder(orderId, itemId);
    }

    @DELETE
    @Path("{orderId}/items/{itemId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response deleteItemFromOrder(@PathParam("orderId") String orderId, @PathParam("itemId") String itemId) {
        return orderService.deleteItemFromOrder(orderId, itemId);
    }
}