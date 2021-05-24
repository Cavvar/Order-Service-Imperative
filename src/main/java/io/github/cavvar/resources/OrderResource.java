package io.github.cavvar.resources;

import io.github.cavvar.models.Card;
import io.github.cavvar.models.Item;
import io.github.cavvar.models.NewOrder;
import io.github.cavvar.models.Order;
import io.github.cavvar.services.OrderService;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Objects;

@Path("/orders")
public class OrderResource {

    @Inject
    OrderService orderService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllOrders() {
        final List<Order> allOrders = orderService.getAllOrders();
        return Response.status(Response.Status.OK).entity(allOrders).type(MediaType.APPLICATION_JSON_TYPE).build();
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
    public Response getOrder(@PathParam("orderId") int orderId) {
        final Order retrievedOrder = orderService.getOrder(orderId);
        if (Objects.isNull(retrievedOrder)) {
            return Response.status(Response.Status.NOT_FOUND).entity("Order was not found").type(MediaType.TEXT_PLAIN_TYPE).build();
        }
        return Response.status(Response.Status.OK).entity(retrievedOrder).type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    @PUT
    @Path("{orderId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response updateCardOfOrder(@PathParam("orderId") int orderId, Card newCard) {
        return orderService.updateCardOfOrder(orderId, newCard);
    }

    @DELETE
    @Path("{orderId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response deleteOrder(@PathParam("orderId") int orderId) {
        return orderService.deleteOrder(orderId);
    }

    @GET
    @Path("{orderId}/items")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllItemsFromOrder(@PathParam("orderId") int orderId) {
        return orderService.getAllItemsFromOrder(orderId);
    }

    @POST
    @Path("{orderId}/items")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response addItemToOrder(@PathParam("orderId") int orderId, Item itemToAdd) {
        return orderService.addItemToOrder(orderId, itemToAdd);
    }

    @GET
    @Path("{orderId}/items/{itemId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getItemFromOrder(@PathParam("orderId") int orderId, @PathParam("itemId") int itemId) {
        return orderService.getItemFromOrder(orderId, itemId);
    }

    @DELETE
    @Path("{orderId}/items/{itemId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response deleteItemFromOrder(@PathParam("orderId") int orderId, @PathParam("itemId") int itemId) {
        return orderService.deleteItemFromOrder(orderId, itemId);
    }
}