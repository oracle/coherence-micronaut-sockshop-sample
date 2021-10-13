/*
 * Copyright (c) 2021 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package com.oracle.coherence.examples.sockshop.micronaut.catalog;

import com.tangosol.net.NamedCache;
import io.micronaut.http.HttpStatus;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

@MicronautTest
class MetricsTest {

	@Inject
	private NamedCache<String, Sock> socks;


	@BeforeEach
	void setup() {
		RestAssured.reset();
		RestAssured.baseURI = "http://localhost";
	}

	@Test
	void testAddItem() {
		socks.put("example", new Sock());
		socks.put("another", new Sock());

		given().
				port(9612).
				accept(JSON).
		when().
				get("/metrics").
		then().
				statusCode(HttpStatus.OK.getCode());

		given().
				port(9612).
				accept(JSON).
		when().
				get("/metrics/Coherence.Cache.Size?name=socks&tier=back").
		then().
				statusCode(HttpStatus.OK.getCode()).
		assertThat().
				body("size()", Matchers.is(1)).
				body("[0].tags.name", Matchers.is("socks"),
						"[0].value", Matchers.is(2));
	}
}
