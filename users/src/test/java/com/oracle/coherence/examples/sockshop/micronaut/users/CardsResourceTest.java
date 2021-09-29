/*
 * Copyright (c) 2021 Oracle and/or its affiliates.
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

import javax.inject.Inject;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

/**
 * Integration tests for {@link CardsResource}.
 */
@MicronautTest
public class CardsResourceTest {
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
    public void testRegisterCard() {
        users.register(new User("foo", "passfoo", "foo@weavesocks.com", "foouser", "pass"));
        given().
                contentType(JSON).
                body(new CardsResource.AddCardRequest("3691369136913691", "01/21", "789", "foouser")).
                when().
                post("/cards").
                then().
                statusCode(200).
                body("id", containsString("foouser"));
    }

    @Test
    public void testGetCard() {
        User u = users.getOrCreate("cardUser");
        u.setUsername("cardUser");
        CardId cardId = u.addCard(new Card("3691369136913691", "01/21", "789")).getId();
        users.register(u);
        given().
                pathParam("id", cardId.toString()).
                when().
                get("/cards/{id}").
                then().
                statusCode(HttpStatus.OK.getCode()).
                body("longNum", containsString("3691"),
                        "ccv", is("789"));
    }

    @Test
    public void testDeleteCard() {
        User u = users.getOrCreate("cardUser");
        u.setUsername("cardUser");
        CardId cardId = u.addCard(new Card("3691369136913691", "01/21", "789")).getId();
        users.register(u);
        given().
                pathParam("id", cardId.toString()).
                when().
                get("/cards/{id}").
                then().
                statusCode(HttpStatus.OK.getCode());
    }

    @Test
    public void testGetAllCards() {
        when().
                get("/cards").
                then().
                statusCode(HttpStatus.OK.getCode());
    }
}
