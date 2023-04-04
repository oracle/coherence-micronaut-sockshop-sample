/*
 * Copyright (c) 2021, 2023 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package com.oracle.coherence.examples.sockshop.micronaut.shipping;

import io.micronaut.context.ApplicationContext;
import io.micronaut.http.HttpStatus;
import io.micronaut.runtime.server.EmbeddedServer;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;
import java.time.LocalDate;

import static com.oracle.coherence.examples.sockshop.micronaut.shipping.TestDataFactory.shipment;
import static com.oracle.coherence.examples.sockshop.micronaut.shipping.TestDataFactory.shippingRequest;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.is;

/**
 * Integration tests for {@link ShippingResource}.
 */
public abstract class ShippingResourceTest {
    @Inject
    EmbeddedServer server;

    @Inject
    ApplicationContext context;

    private TestShipmentRepository shipments;

    @BeforeEach
    void setup() {
        // Configure RestAssured to run tests against our application
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = server.getPort();

        shipments = context.getBean(TestShipmentRepository.class);
        shipments.clear();
    }

    @Test
    void testFedEx() {
        given().
                body(shippingRequest("A123", 1)).
                contentType(ContentType.JSON).
                accept(ContentType.JSON).
        when().
                post("/shipping").
        then().
                statusCode(HttpStatus.OK.getCode()).
                body("carrier", is("FEDEX"),
                     "deliveryDate", is(LocalDate.now().plusDays(1).toString())
                );
    }

    @Test
    void testUPS() {
        given().
                body(shippingRequest("A456", 3)).
                contentType(ContentType.JSON).
                accept(ContentType.JSON).
        when().
                post("/shipping").
        then().
                statusCode(HttpStatus.OK.getCode()).
                body("carrier", is("UPS"),
                     "deliveryDate", is(LocalDate.now().plusDays(3).toString())
                );
    }

    @Test
    void testUSPS() {
        given().
                body(shippingRequest("A789", 10)).
                contentType(ContentType.JSON).
                accept(ContentType.JSON).
        when().
                post("/shipping").
        then().
                statusCode(HttpStatus.OK.getCode()).
                body("carrier", is("USPS"),
                     "deliveryDate", is(LocalDate.now().plusDays(5).toString())
                );
    }

    @Test
    void testGetShipmentByOrder() {
        LocalDate deliveryDate = LocalDate.now().plusDays(2);
        shipments.saveShipment(shipment("A123", "UPS", "1Z999AA10123456784", deliveryDate));

        when().
                get("/shipping/{orderId}", "A123").
        then().
                statusCode(HttpStatus.OK.getCode()).
                body("carrier", is("UPS"),
                     "trackingNumber", is("1Z999AA10123456784"),
                     "deliveryDate", is(deliveryDate.toString())
                );

        when().
                get("/shipments/{orderId}", "B456").
        then().
                statusCode(HttpStatus.NOT_FOUND.getCode());
    }
}
