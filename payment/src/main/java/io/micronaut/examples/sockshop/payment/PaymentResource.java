/*
 * Copyright (c) 2021 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package io.micronaut.examples.sockshop.payment;

import io.micrometer.core.instrument.MeterRegistry;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;

import io.micronaut.tracing.annotation.NewSpan;

import javax.inject.Inject;
import java.util.Collection;

/**
 * Implementation of the Payment Service REST API.
 */
@Controller("/payments")
public class PaymentResource implements PaymentApi {
    /**
     * Payment repository to use.
     */
    @Inject
    private PaymentRepository payments;

    /**
     * Payment service to use.
     */
    @Inject
    private PaymentService paymentService;

    @Inject
    private MeterRegistry meterRegistry;

    @Override
    @NewSpan
    public HttpResponse<Collection<? extends Authorization>> getOrderAuthorizations(String orderId) {
        return HttpResponse.ok(payments.findAuthorizationsByOrder(orderId));
    }

    @Override
    @NewSpan
    public Authorization authorize(PaymentRequest paymentRequest) {
        meterRegistry.counter("payments.authorize", "controller", "payments")
                .increment();

        String firstName = paymentRequest.getCustomer().getFirstName();
        String lastName  = paymentRequest.getCustomer().getLastName();

        Authorization auth = paymentService.authorize(
                paymentRequest.getOrderId(),
                firstName,
                lastName,
                paymentRequest.getCard(),
                paymentRequest.getAddress(),
                paymentRequest.getAmount());

        payments.saveAuthorization(auth);

        return auth;
    }
}
