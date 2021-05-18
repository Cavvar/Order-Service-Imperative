package io.github.cavvar.services.payment;

import io.github.cavvar.models.PaymentRequest;
import io.github.cavvar.models.PaymentResponse;

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
