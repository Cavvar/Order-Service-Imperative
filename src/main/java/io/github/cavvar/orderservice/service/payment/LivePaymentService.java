package io.github.cavvar.orderservice.service.payment;

import io.github.cavvar.orderservice.model.PaymentRequest;
import io.github.cavvar.orderservice.model.PaymentResponse;
import io.github.cavvar.orderservice.utility.Constants;

import javax.enterprise.context.Dependent;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Dependent
public class LivePaymentService {
    public PaymentResponse getPayment(PaymentRequest paymentRequest) throws ExecutionException, InterruptedException, TimeoutException {
        final Client client = ClientBuilder.newClient();
        final Future<PaymentResponse> futurePayment = client.target(Constants.PAYMENT_SERVICE_URL).request().async().post(Entity.json(paymentRequest), PaymentResponse.class);
        return futurePayment.get(Constants.TIMEOUT_VALUE, TimeUnit.SECONDS);
    }
}
