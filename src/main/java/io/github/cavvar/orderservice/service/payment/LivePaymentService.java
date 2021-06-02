package io.github.cavvar.orderservice.service.payment;

import io.github.cavvar.orderservice.model.PaymentRequest;
import io.github.cavvar.orderservice.model.PaymentResponse;
import io.github.cavvar.orderservice.utility.Constants;

import javax.enterprise.context.Dependent;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import java.util.concurrent.Future;

@Dependent
public class LivePaymentService {
    public Future<PaymentResponse> getPayment(PaymentRequest paymentRequest) {
        final Client client = ClientBuilder.newClient();
        return client.target(Constants.PAYMENT_SERVICE_URL).request().async().post(Entity.json(paymentRequest), PaymentResponse.class);
    }
}
