/*
 * Copyright (c) 2021 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package io.micronaut.examples.sockshop.payment;

import io.micronaut.context.ApplicationContext;
import io.micronaut.http.HttpStatus;
import io.micronaut.runtime.server.EmbeddedServer;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

/**
 * Integration tests for {@link PaymentResource}.
 */
public abstract class PaymentResourceTest {
    @Inject
    ApplicationContext context;

    @Inject
    EmbeddedServer server;


    private TestPaymentRepository payments;

    @BeforeEach
    void setup() throws Exception {
        // Configure RestAssured to run tests against our application
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = server.getPort();

        payments = context.getBean(TestPaymentRepository.class);
        payments.clear();
    }

    @Test
    void testSuccessfulAuthorization() {
        given().
                body(TestDataFactory.paymentRequest("A123xxx", 50)).
                contentType(ContentType.JSON).
                accept(ContentType.JSON).
        when().
                post("/payments").
        then().
                statusCode(HttpStatus.OK.getCode()).
                body("authorised", is(true),
                     "message", is("Payment authorized.")
                );
    }

    @Test
    void testDeclinedAuthorization() {
        given().
                body(TestDataFactory.paymentRequest("A123", 150)).
                contentType(ContentType.JSON).
                accept(ContentType.JSON).
        when().
                post("/payments").
        then().
                statusCode(HttpStatus.OK.getCode()).
                body("authorised", is(false),
                     "message", is("Payment declined: amount exceeds 100.00")
                );
    }

    @Test
    void testInvalidPaymentAmount() {
        given().
                body(TestDataFactory.paymentRequest("A123", -50)).
                contentType(ContentType.JSON).
                accept(ContentType.JSON).
        when().
                post("/payments").
        then().
                statusCode(HttpStatus.OK.getCode()).
                body("authorised", is(false),
                     "message", is("Invalid payment amount.")
                );
    }

    @Test
    void testFindPaymentsByOrder() {
        LocalDateTime time = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        payments.saveAuthorization(TestDataFactory.auth("A123", time, new Err("Payment service unavailable")));
        payments.saveAuthorization(TestDataFactory.auth("A123", time.plusSeconds(5), false, "Payment declined"));
        payments.saveAuthorization(TestDataFactory.auth("A123", time.plusSeconds(10), true, "Payment processed"));
        payments.saveAuthorization(TestDataFactory.auth("B456", time, true, "Payment processed"));

        when().
                get("/payments/{orderId}", "A123").
        then().
                statusCode(HttpStatus.OK.getCode()).
                body("$", hasSize(3));

        when().
                get("/payments/{orderId}", "B456").
        then().
                statusCode(HttpStatus.OK.getCode()).
                body("$", hasSize(1));

        when().
                get("/payments/{orderId}", "C789").
        then().
                statusCode(HttpStatus.OK.getCode()).
                body("$", hasSize(0));
    }
}
