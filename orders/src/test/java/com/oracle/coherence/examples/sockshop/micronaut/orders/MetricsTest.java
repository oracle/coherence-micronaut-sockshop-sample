/*
 * Copyright (c) 2021 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package com.oracle.coherence.examples.sockshop.micronaut.orders;

import io.micronaut.http.HttpStatus;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.net.URI;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;

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
	void testCreateOrder() {
		String baseUri = "http://localhost:" + server.getPort();
		NewOrderRequest req = NewOrderRequest.builder()
				.customer(URI.create(baseUri + "/customers/homer"))
				.address(URI.create(baseUri + "/addresses/homer:1"))
				.card(URI.create(baseUri + "/cards/homer:1234"))
				.items(URI.create(baseUri + "/carts/homer/items"))
				.build();

		given().
				port(server.getPort()).
				body(req).
				contentType(ContentType.JSON).
				accept(ContentType.JSON).
		when().
				post("/orders").
		then().
				statusCode(HttpStatus.CREATED.getCode()).
				body("total", is(14.0f),
						"status", is("CREATED"));

		given().
				port(9612).
				accept(JSON).
		when().
				get("/metrics/Coherence.Cache.Size?name=orders&tier=back").
		then().
				statusCode(HttpStatus.OK.getCode()).
		assertThat().
				body("size()", is(1)).
				body("[0].tags.name", is("orders"),
						"[0].value", is(1));
	}
}
