/*
 * Copyright (c) 2021, 2023 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package com.oracle.coherence.examples.sockshop.micronaut.catalog;

import io.micronaut.runtime.server.EmbeddedServer;
import io.restassured.RestAssured;

import jakarta.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;

/**
 * Integration tests for {@link CatalogResource}.
 */
public abstract class CatalogResourceTest {
    @Inject
    EmbeddedServer server;

    @BeforeEach
    void setup() {
        // Configure RestAssured to run tests against our application
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = server.getPort();
    }

    @Test
    void testQueryWithoutFilter() {
        when().
                get("/catalogue").
        then().
                statusCode(200).
                body("name", containsInAnyOrder("Nerd leg", "YouTube.sock", "Classic", "Figueroa",
                                                "SuperSport XL", "Cat socks", "Crossed", "Colourful", "Holy"));
    }

    @Test
    void testQueryWithFilter() {
        when().
                get("/catalogue?tags=sport").
        then().
                statusCode(200).
                body("name", contains("SuperSport XL"));
    }

    @Test
    void testQueryPaging() {
        when().
                get("/catalogue?order=name&page=2&size=3").
        then().
                statusCode(200).
                body("name", contains("Crossed", "Figueroa", "Holy"));
    }

    @Test
    void testGetById() {
        when().
                get("/catalogue/{productId}", "03fef6ac-1896-4ce8-bd69-b798f85c6e0b").
        then().
                statusCode(200).
                body("name", is("Holy"));
    }

    @Test
    void testGetByMissingId() {
        when().
                get("/catalogue/{productId}", "bad_ID").
        then().
                statusCode(404);
    }

    @Test
    void testGetImage() {
        when().
                get("/catalogue/images/{imageId}", "puma_1.jpeg").
        then().
                statusCode(200);
    }

    @Test
    void testGetMissingImage() {
        when().
                get("/catalogue/images/{imageId}", "bad_ID").
        then().
                statusCode(404);
    }

    @Test
    void testSockCountWithoutFilter() {
        when().
                get("/catalogue/size").
        then().
                statusCode(200).
                body("size", is(9));
    }

    @Test
    void testSockCountWithFilter() {
        when().
                get("/catalogue/size?tags=formal").
        then().
                statusCode(200).
                body("size", is(5));
    }

    @Test
    void testGetTags() {
        when().
                get("/tags").
        then().
                statusCode(200).
                body("tags",
                     containsInAnyOrder("formal", "red", "magic", "green", "blue",
                                        "geek", "black", "skin", "action", "brown", "sport"));
    }
}
