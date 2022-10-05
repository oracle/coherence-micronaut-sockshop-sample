/*
 * Copyright (c) 2021 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package com.oracle.coherence.examples.sockshop.micronaut.orders;

import com.oracle.bedrock.testsupport.deferred.Eventually;

import io.micronaut.context.ApplicationContext;
import io.micronaut.http.HttpStatus;
import io.micronaut.runtime.server.EmbeddedServer;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.mapper.ObjectMapperType;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import javax.inject.Inject;

import java.net.URI;
import java.time.LocalDate;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;

/**
 * Integration tests for {@link OrderResource},
 * using Coherence for persistence.
 */
@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CoherenceOrderResourceTest {
    @Inject
    ApplicationContext context;

    @Inject
    EmbeddedServer server;

    protected TestOrderRepository orders;

    @BeforeEach
    protected void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = server.getPort();

        orders = context.getBean(TestOrderRepository.class);
        orders.clear();
    }


    @Test
    protected void testGetMissingOrder() {
        when().
                get("/orders/{orderId}", "XYZ").
                then().
                statusCode(HttpStatus.NOT_FOUND.getCode());
    }

    @Test
    protected void testGetOrder() {
        Order order = TestDataFactory.order("homer", 1);
        orders.saveOrder(order);
        Order saved = get("/orders/{orderId}", order.getOrderId()).as(Order.class, ObjectMapperType.JACKSON_2);

        assertThat(saved, Matchers.is(order));
    }

    @Test
    protected void testFindOrdersByCustomerId() {
        orders.saveOrder(TestDataFactory.order("homer", 1));
        orders.saveOrder(TestDataFactory.order("homer", 2));
        orders.saveOrder(TestDataFactory.order("marge", 5));

        given().
                queryParam("custId", "homer").
                when().
                get("/orders/search/customerId").
                then().
                statusCode(HttpStatus.OK.getCode()).
                body("_embedded.customerOrders.total", containsInAnyOrder(1f, 5f));

        given().
                queryParam("custId", "marge").
                when().
                get("/orders/search/customerId").
                then().
                statusCode(HttpStatus.OK.getCode()).
                body("_embedded.customerOrders.total", containsInAnyOrder(55f));

        given().
                queryParam("custId", "bart").
                when().
                get("/orders/search/customerId").

                then().
                statusCode(HttpStatus.NOT_FOUND.getCode());
    }


    @Test
    protected void testInvalidOrder() {
        NewOrderRequest req = NewOrderRequest.builder().build();

        given().
                body(req).
                contentType(ContentType.JSON).
                accept(ContentType.JSON).
                when().
                post("/orders").
                then().
                statusCode(HttpStatus.NOT_ACCEPTABLE.getCode())
                .body("message", is("Invalid order request. Order requires customer, address, card and items."));
    }

    @Test
    protected void testCreateOrder() {
        String baseUri = "http://localhost:" + server.getPort();
        NewOrderRequest req = NewOrderRequest.builder()
                .customer(URI.create(baseUri + "/customers/homer"))
                .address(URI.create(baseUri + "/addresses/homer:1"))
                .card(URI.create(baseUri + "/cards/homer:1234"))
                .items(URI.create(baseUri + "/carts/homer/items"))
                .build();

        String orderId = given().
                body(req).
                contentType(ContentType.JSON).
                accept(ContentType.JSON).
        when().
                post("/orders").
        then().
                statusCode(HttpStatus.CREATED.getCode()).
                body("total", is(14.0f),
                     "status", is("CREATED")).
                extract().response().body().path("id");

        Eventually.assertDeferred(() -> orders.get(orderId).getStatus(), Matchers.is(Order.Status.SHIPPED));

        Order order = orders.get(orderId);
        assertThat(order.getPayment().isAuthorised(), is(true));
        assertThat(order.getShipment().getCarrier(), is("UPS"));
        assertThat(order.getShipment().getDeliveryDate(), is(LocalDate.now().plusDays(2)));
    }

    @Test
    protected void testPaymentFailure() {
        String baseUri = "http://localhost:" + server.getPort();
        NewOrderRequest req = NewOrderRequest.builder()
                .customer(URI.create(baseUri + "/customers/lisa"))
                .address(URI.create(baseUri + "/addresses/lisa:1"))
                .card(URI.create(baseUri + "/cards/lisa:1234"))
                .items(URI.create(baseUri + "/carts/lisa/items"))
                .build();

        String orderId = given().
                body(req).
                contentType(ContentType.JSON).
                accept(ContentType.JSON).
                when().
                post("/orders").
                then().
                statusCode(HttpStatus.CREATED.getCode()).
                body("total", is(14.0f),
                        "status", is("CREATED")).
                extract().response().body().path("id");

        Eventually.assertDeferred(() -> orders.get(orderId).getStatus(), Matchers.is(Order.Status.PAYMENT_FAILED));

        Order order = orders.get(orderId);
        assertThat(order.getPayment().isAuthorised(), is(false));
        assertThat(order.getPayment().getMessage(), is("Unable to parse authorization packet"));
    }

    @Test
    protected void testPaymentDeclined() {
        String baseUri = "http://localhost:" + server.getPort();
        NewOrderRequest req = NewOrderRequest.builder()
                .customer(URI.create(baseUri + "/customers/bart"))
                .address(URI.create(baseUri + "/addresses/bart:1"))
                .card(URI.create(baseUri + "/cards/bart:1234"))
                .items(URI.create(baseUri + "/carts/bart/items"))
                .build();

        String orderId = given().
                body(req).
                contentType(ContentType.JSON).
                accept(ContentType.JSON).
        when().
                post("/orders").
        then().
                statusCode(HttpStatus.CREATED.getCode()).
                body("total", is(14.0f),
                    "status", is("CREATED")).
                extract().response().body().path("id");

        Eventually.assertDeferred(() -> orders.get(orderId).getStatus(), Matchers.is(Order.Status.PAYMENT_FAILED));

        Order order = orders.get(orderId);
        assertThat(order.getPayment().isAuthorised(), is(false));
        assertThat(order.getPayment().getMessage(), is("Minors need parent approval"));
    }

}