package io.github.cavvar.orderservice.utility.exceptions;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.NoSuchElementException;

@Provider
public class OrderServiceExceptionMapper implements ExceptionMapper<Throwable> {
    @Override
    public Response toResponse(Throwable throwable) {
        final Response.ResponseBuilder responseBuilder = Response.status(Response.Status.NOT_FOUND).type(MediaType.TEXT_PLAIN_TYPE);

        if (throwable instanceof NullPointerException) {
            responseBuilder.entity(String.format("Order or Item was not found. %s", throwable.getMessage()));
        } else if (throwable instanceof NoSuchElementException) {
            responseBuilder.entity(String.format("Item was not found. %s", throwable.getMessage()));
        } else if (throwable instanceof IllegalArgumentException) {
            responseBuilder.entity(String.format("Order was not found. %s", throwable.getMessage()));
        } else if (throwable instanceof IllegalStateException) {
            responseBuilder.status(Response.Status.INTERNAL_SERVER_ERROR).entity(throwable.getMessage());
        } else if (throwable instanceof BadRequestException) {
            responseBuilder.status(Response.Status.BAD_REQUEST).entity(String.format("Invalid Input. %s", throwable.getMessage()));
        }


        return responseBuilder.build();
    }
}
