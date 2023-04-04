/*
 * Copyright (c) 2021, 2023 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package com.oracle.coherence.examples.sockshop.micronaut.users;

import io.micronaut.context.ApplicationContext;
import io.micronaut.http.HttpStatus;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;

import io.restassured.RestAssured;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

/**
 * Integration tests for {@link AddressesResource}.
 */

@MicronautTest
public class AddressesResourceTest {
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
        users.removeUser("foouser");
        users.removeUser("baruser");
    }

    protected UserRepository getUserRepository() {
        return context.getBean(UserRepository.class);
    }

    @Test
    public void testRegisterAddress() {
        users.register(new User("foo", "passfoo", "foo@weavesocks.com", "foouser", "pass"));
        given().
            contentType(JSON).
            body(new AddressesResource.AddAddressRequest("16", "huntington", "lexington", "01886", "us", "foouser")).
        when().
            post("/addresses").
        then().
            statusCode(200).
            body("id", containsString("foouser"));
    }

    @Test
    public void testGetAddress() {
        User u = new User("foo", "passfoo", "foo@weavesocks.com", "foouser", "pass");
        AddressId addrId = u.addAddress(new Address("555", "woodbury St", "Westford", "01886", "USA")).getId();
        users.register(u);

        given().
              pathParam("id", addrId.toString()).
        when().
              get("/addresses/{id}").
        then().
            statusCode(HttpStatus.OK.getCode()).
            body("number", is("555"),
                    "city", is("Westford"));
    }

    @Test
    public void testDeleteAddress() {
        User u = users.getOrCreate("foouser");
        u.setUsername("foouser");
        AddressId addrId = u.addAddress(new Address("555", "woodbury St", "Westford", "01886", "USA")).getId();
        users.register(u);

        given().
            pathParam("id", addrId.toString()).
        when().
            delete("/addresses/{id}").
        then().
            statusCode(200);
    }
}
