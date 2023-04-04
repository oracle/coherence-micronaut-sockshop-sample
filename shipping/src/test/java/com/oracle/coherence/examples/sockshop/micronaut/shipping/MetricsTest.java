/*
 * Copyright (c) 2021, 2023 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package com.oracle.coherence.examples.sockshop.micronaut.shipping;

import io.micronaut.http.HttpStatus;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;
import java.time.LocalDate;

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
	void testFedEx() {
		given().
				port(server.getPort()).
				body(TestDataFactory.shippingRequest("A123", 1)).
				contentType(ContentType.JSON).
				accept(ContentType.JSON).
		when().
				post("/shipping").
		then().
				statusCode(HttpStatus.OK.getCode()).
				body("carrier", is("FEDEX"),
						"deliveryDate", is(LocalDate.now().plusDays(1).toString())
				);

		given().
				port(9612).
				accept(JSON).
		when().
				get("/metrics/Coherence.Cache.Size?name=shipments&tier=back").
		then().
				statusCode(HttpStatus.OK.getCode()).
		assertThat().
				body("size()", is(1)).
				body("[0].tags.name", is("shipments"),
						"[0].value", is(1));
	}
}
