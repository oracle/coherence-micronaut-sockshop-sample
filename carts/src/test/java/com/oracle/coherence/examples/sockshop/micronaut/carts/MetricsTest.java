/*
 * Copyright (c) 2021 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package com.oracle.coherence.examples.sockshop.micronaut.carts;

import io.micronaut.http.HttpStatus;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

@MicronautTest
class MetricsTest {

	@Inject
	EmbeddedServer server;

	@BeforeEach
	void setup() {
		RestAssured.reset();
		RestAssured.baseURI = "http://localhost";
	}

	@Test
	void testAddItem() {
		given().
				port(server.getPort()).
				contentType(ContentType.JSON).
				body(new Item("X1", 0, 10f)).
		when().
				post("/carts/{cartId}/items", "C1").
		then().
				statusCode(HttpStatus.CREATED.getCode()).
				body("itemId", is("X1"),
						"quantity", is(1),
						"unitPrice", is(10f));

		given().
				port(9612).
				accept(ContentType.JSON).
		when().
				get("/metrics/Coherence.Cache.Size?name=carts&tier=back").
		then().
				statusCode(HttpStatus.OK.getCode()).
		assertThat().
				body("size()", is(1)).
				body("[0].tags.name", is("carts"),
						"[0].value", is(1));
	}
}
