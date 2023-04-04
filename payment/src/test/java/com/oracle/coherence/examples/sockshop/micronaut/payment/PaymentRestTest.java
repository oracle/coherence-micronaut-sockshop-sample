/*
 * Copyright (c) 2021, 2023 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package com.oracle.coherence.examples.sockshop.micronaut.payment;

import io.micronaut.context.ApplicationContext;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@MicronautTest
public class PaymentRestTest {
    @Inject
    private PaymentClient client;

    @Inject
    ApplicationContext context;

    private TestPaymentRepository payments;

    @BeforeEach
    void setup() {
        payments = context.getBean(TestPaymentRepository.class);
        payments.clear();
    }

    @Test
    void testSuccessfulAuthorization() {
        Authorization authorization = client.authorize(TestDataFactory.paymentRequest("A123", 50));
        assertThat(authorization.isAuthorised(), is(true));
        assertThat(authorization.getMessage(), is("Payment authorized."));
    }

    @Test
    void testDeclinedAuthorization() {
        Authorization authorization = client.authorize(TestDataFactory.paymentRequest("A123", 150));
        assertThat(authorization.isAuthorised(), is(false));
        assertThat(authorization.getMessage(), is("Payment declined: amount exceeds 100.00"));
    }

    @Test
    void testInvalidPaymentAmount() {
        Authorization authorization = client.authorize(TestDataFactory.paymentRequest("A123", -50));
        assertThat(authorization.isAuthorised(), is(false));
        assertThat(authorization.getMessage(), is("Invalid payment amount."));
    }

    @Test
    void testFindPaymentsByOrder() {
        LocalDateTime time = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        payments.saveAuthorization(TestDataFactory.auth("A123", time, new Err("Payment service unavailable")));
        payments.saveAuthorization(TestDataFactory.auth("A123", time.plusSeconds(5), false, "Payment declined"));
        payments.saveAuthorization(TestDataFactory.auth("A123", time.plusSeconds(10), true, "Payment processed"));
        payments.saveAuthorization(TestDataFactory.auth("B456", time, true, "Payment processed"));

        assertThat(client.getOrderAuthorizations("A123").body(), hasSize(3));
        assertThat(client.getOrderAuthorizations("B456").body(), hasSize(1));
        assertThat(client.getOrderAuthorizations("C789").body(), hasSize(0));
    }
}
