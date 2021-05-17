package io.github.cavvar.services;

import io.github.cavvar.models.PaymentRequest;
import io.github.cavvar.models.PaymentResponse;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

@RegisterRestClient(configKey = "payment-service-api")
public interface PaymentService {
    @POST
    @Path("/paymentAuth")
    PaymentResponse getPaymentValidation(PaymentRequest paymentRequest);
}
