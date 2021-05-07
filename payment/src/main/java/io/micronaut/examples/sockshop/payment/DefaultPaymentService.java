/*
 * Copyright (c) 2020, 2021 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * http://oss.oracle.com/licenses/upl.
 */

package io.micronaut.examples.sockshop.payment;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

import io.micronaut.context.annotation.Property;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;

import java.time.LocalDateTime;

/**
 * Trivial {@link PaymentService} implementation for demo and testing purposes.
 * <p/>
 * It approves all payment requests with the total amount lower or equal
 * to the {@code payment.limit} configuration property (100 by default),
 * and declines all requests above that amount.
 */
@Singleton
public class DefaultPaymentService implements PaymentService {
    /**
     * Payment limit
     */
    private float paymentLimit;

    private Counter paymentSuccess;

    private Counter paymentFailure;

    @Inject
    private MeterRegistry meterRegistry;

    /**
     * Construct {@code DefaultPaymentService} instance.
     */
    public DefaultPaymentService() {
    }

     /**
     * Construct {@code DefaultPaymentService} instance with {@link Counter}s for testing purposes.
     */
    public DefaultPaymentService(float paymentLimit, Counter paymentSuccess, Counter paymentFailure) {
        this.paymentSuccess = paymentSuccess;
        this.paymentFailure = paymentFailure;
        this.paymentLimit = paymentLimit;
    }

    /**
     * Construct {@code DefaultPaymentService} instance.
     *
     * @param paymentLimit payment limit
     */
    @Inject
    public DefaultPaymentService(@Property(name = "payment.limit", defaultValue = "100") float paymentLimit) {
        this.paymentLimit = paymentLimit;
    }

    @PostConstruct
    public void init() {
        paymentSuccess = Counter.builder("payment.success")
                .register(meterRegistry);
        paymentFailure = Counter.builder("payment.failure")
                .register(meterRegistry);
    }

    @Override
    public Authorization authorize(String orderId, String firstName, String lastName, Card card, Address address, float amount) {
        boolean fAuthorized = amount > 0 && amount <= paymentLimit;

        String message = fAuthorized ? "Payment authorized." :
                amount <= 0 ? "Invalid payment amount." :
                        "Payment declined: amount exceeds " + String.format("%.2f", paymentLimit);

        if (fAuthorized) {
            paymentSuccess.increment();
        } else {
            paymentFailure.increment();
        }

        return Authorization.builder()
                .orderId(orderId)
                .time(LocalDateTime.now())
                .authorised(fAuthorized)
                .message(message)
                .build();
    }
}
