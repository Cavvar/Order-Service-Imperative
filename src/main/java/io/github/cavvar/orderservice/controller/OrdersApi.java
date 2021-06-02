package io.github.cavvar.orderservice.controller;

import io.github.cavvar.orderservice.model.Card;
import io.github.cavvar.orderservice.model.Item;
import io.github.cavvar.orderservice.model.NewOrder;
import io.github.cavvar.orderservice.model.Order;
import io.smallrye.mutiny.Uni;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/orders")
@Api(value = "the orders API")
public interface OrdersApi {

    @POST
    @Path("/{orderId}/items/{itemId}")
    @ApiOperation(value = "Add an item to an order", notes = "", tags = {"orders"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK. A new item was added to the order"), @ApiResponse(code = 404, message = "Not found. Order or Item was not found")})
    Uni<Response> addItemToOrder(@PathParam("orderId") @ApiParam("Order ID") Integer orderId, @PathParam("itemId") @ApiParam("Item ID") Integer itemId);

    @POST
    @Consumes({"application/json"})
    @Produces({"application/json"})
    @ApiOperation(value = "Create a new order", notes = "", tags = {"orders"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK. Order was successfully created", response = Order.class), @ApiResponse(code = 400, message = "Invalid Input. The request payload contains invalid data"), @ApiResponse(code = 500, message = "Internal Server Error. Unable to create order")})
    Uni<Response> createOrder(NewOrder newOrder);

    @DELETE
    @Path("/{orderId}/items/{itemId}")
    @ApiOperation(value = "Delete an item from an order", notes = "", tags = {"orders"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK. Item was successfully deleted"), @ApiResponse(code = 404, message = "Not found. Order or Item were not found")})
    Uni<Response> deleteItemFromOrder(@PathParam("orderId") @ApiParam("Order ID") Integer orderId, @PathParam("itemId") @ApiParam("Item ID") Integer itemId);

    @DELETE
    @Path("/{orderId}")
    @ApiOperation(value = "Delete an order", notes = "", tags = {"orders"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK. The Order was successfully deleted"), @ApiResponse(code = 404, message = "Order was not found")})
    Uni<Response> deleteOrder(@PathParam("orderId") @ApiParam("Order ID") Integer orderId);

    @GET
    @Path("/{orderId}/items")
    @Produces({"application/json"})
    @ApiOperation(value = "Get all items of an order", notes = "", tags = {"orders"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK. Collection of items from an order", response = Item.class, responseContainer = "List"), @ApiResponse(code = 404, message = "Not found. Order was not found", responseContainer = "List")})
    Uni<Response> getAllItemsFromOrder(@PathParam("orderId") @ApiParam("Order ID") Integer orderId);

    @GET
    @Produces({"application/json"})
    @ApiOperation(value = "Get all available orders", notes = "", tags = {"orders"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK. Collection of orders", response = Order.class, responseContainer = "List")})
    Uni<Response> getAllOrders();

    @GET
    @Path("/{orderId}/items/{itemId}")
    @Produces({"application/json"})
    @ApiOperation(value = "Get the item from an order", notes = "", tags = {"orders"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK. Retrieved item was returned", response = Item.class), @ApiResponse(code = 404, message = "Not found. Order or Item were not found")})
    Uni<Response> getItemFromOrder(@PathParam("orderId") @ApiParam("Order ID") Integer orderId, @PathParam("itemId") @ApiParam("Item ID") Integer itemId);

    @GET
    @Path("/{orderId}")
    @Produces({"application/json"})
    @ApiOperation(value = "Get an order by order id", notes = "", tags = {"orders"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK. Order Details were successfully returned", response = Order.class), @ApiResponse(code = 404, message = "Not found. Order was not found")})
    Uni<Response> getOrder(@PathParam("orderId") @ApiParam("Order ID") Integer orderId);

    @PUT
    @Path("/{orderId}")
    @Consumes({"application/json"})
    @ApiOperation(value = "Update Card of an Order", notes = "", tags = {"orders"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK. Card Information was successfully updated"), @ApiResponse(code = 404, message = "Not found. Order was not found", response = Void.class)})
    Uni<Response> updateCardOfOrder(@PathParam("orderId") @ApiParam("Order ID") Integer orderId, Card card);
}

