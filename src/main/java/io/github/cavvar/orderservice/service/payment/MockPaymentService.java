package io.github.cavvar.orderservice.service.payment;

import io.github.cavvar.orderservice.model.PaymentRequest;
import io.github.cavvar.orderservice.model.PaymentResponse;

import javax.annotation.Priority;
import javax.enterprise.inject.Alternative;
import javax.inject.Singleton;

@Alternative
@Priority(1)
@Singleton
public class MockPaymentService extends LivePaymentService {
    @Override
    public PaymentResponse getPayment(PaymentRequest paymentRequest) {
        return new PaymentResponse(true, "Payment was authorised!");
    }
}
