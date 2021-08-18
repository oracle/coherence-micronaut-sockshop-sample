/*
 * Copyright (c) 2021 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package io.micronaut.examples.sockshop.users;

import javax.inject.Inject;

import io.micronaut.context.ApplicationContext;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;

import io.restassured.RestAssured;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

import static org.hamcrest.Matchers.is;

/**
 * Integration tests for {@link AddressesResource}.
 */
@MicronautTest
public class CustomerResourceTest {
    @Inject
    EmbeddedServer server;

    @Inject
    ApplicationContext context;

    private UserRepository users;

    @BeforeEach
    void setup() {
        // Configure RestAssured to run tests against our application
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = server.getPort();
        users = getUserRepository();
        User user = new User("Test", "User", "user@weavesocks.com", "user", "pass");
        user.addCard(new Card("1234123412341234", "12/19", "123"));
        user.addAddress(new Address("123", "Main St", "Springfield", "12123", "USA"));
        users.register(user);
    }

    protected UserRepository getUserRepository() {
        return context.getBean(UserRepository.class);
    }

    @Test
    public void testAllCustomers() {
        when().
            get("/customers").
        then().log().all().
            statusCode(200).
                body("size()", is(1));
    }

    @Test
    void testGetCustomer() {
        when().
            get("/customers/{id}", "user").
        then().log().all().
            statusCode(200).
            body("firstName", is("Test"));
    }

    @Test
    void testDeleteCustomer() {
        given().
            pathParam("id", "user").
        when().
            delete("/customers/{id}").
        then().
            statusCode(200).
            body("status", is(true));
    }

    @Test
    void testGetCustomerCards() {
        when().
            get("/customers/{id}/cards", "user").
        then().
            statusCode(200).
            body("size()", is(1));
    }

    @Test
    void testGetCustomerAddresses() {
        when().
            get("/customers/{id}/addresses", "user").
        then().
            statusCode(200).
            body("size()", is(1));
    }
}
