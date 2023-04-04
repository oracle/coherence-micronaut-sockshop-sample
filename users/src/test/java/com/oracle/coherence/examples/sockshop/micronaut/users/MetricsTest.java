/*
 * Copyright (c) 2021 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package com.oracle.coherence.examples.sockshop.micronaut.users;

import io.micronaut.http.HttpStatus;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;

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
	public void testRegister() {
		given().
				port(server.getPort()).
				contentType(JSON).
				body(new User("bar", "passbar", "bar@weavesocks.com", "baruser", "pass")).
		when().
				post("/register").
		then().
				statusCode(HttpStatus.OK.getCode()).
				body("id", is("baruser"));

		given().
				port(9612).
				accept(JSON).
		when().
				get("/metrics/Coherence.Cache.Size?name=users&tier=back").
		then().
				statusCode(HttpStatus.OK.getCode()).
		assertThat().
				body("size()", is(1)).
				body("[0].tags.name", is("users"),
						"[0].value", is(1));
	}
}
