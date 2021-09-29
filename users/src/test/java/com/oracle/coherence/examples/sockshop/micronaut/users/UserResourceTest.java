/*
 * Copyright (c) 2021 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package com.oracle.coherence.examples.sockshop.micronaut.users;

import io.micronaut.context.ApplicationContext;
import io.micronaut.runtime.server.EmbeddedServer;

import io.restassured.RestAssured;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

import static org.hamcrest.Matchers.is;

/**
 * Integration tests for {@link UserResource}.
 */

public abstract class UserResourceTest {
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

    protected abstract UserRepository getUserRepository();

    @Test
    public void testAuthentication() {
        users.register(new User("foo", "passfoo", "foo@weavesocks.com", "foouser", "pass"));
        given().auth().preemptive().basic("foouser", "pass").
        when().
            get("/login").
        then().
            assertThat().
            statusCode(200);
    }

    @Test
    public void testRegister() {
        users.removeUser("baruser");
        given().
            contentType(JSON).
            body(new User("bar", "passbar", "bar@weavesocks.com", "baruser", "pass")).
        when().
            post("/register").
        then().
            statusCode(200).
            body("id", is("baruser"));
    }
}
