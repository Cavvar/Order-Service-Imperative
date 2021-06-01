package io.github.cavvar.orderservice.utility;

import javax.ws.rs.client.InvocationCallback;

public class OrderServiceInvocationCallback<T> implements InvocationCallback<T> {
    @Override
    public void completed(T t) {

    }

    @Override
    public void failed(Throwable throwable) {

    }
}