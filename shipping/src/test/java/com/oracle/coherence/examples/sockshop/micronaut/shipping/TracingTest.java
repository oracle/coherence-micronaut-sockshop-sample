/*
 * Copyright (c) 2021 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package com.oracle.coherence.examples.sockshop.micronaut.shipping;

import com.oracle.coherence.common.base.Blocking;
import com.tangosol.net.CacheFactory;
import com.tangosol.util.Base;
import io.jaegertracing.internal.JaegerSpan;
import io.micronaut.context.annotation.Property;
import io.micronaut.http.HttpStatus;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.oracle.coherence.examples.sockshop.micronaut.shipping.TestDataFactory.shippingRequest;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@MicronautTest
@Property(name = "tracing.jaeger.enabled", value = "true")
public class TracingTest {
	@Inject
	EmbeddedServer server;

	@Inject
	TracerFactory.SafeInMemoryReporter reporter;

	@BeforeEach
	void setup() {
		RestAssured.reset();
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = server.getPort();
		reporter.clear();
	}

	@Test
	void testTracing() throws InterruptedException {
		given().
				body(shippingRequest("A123", 1)).
				contentType(ContentType.JSON).
				accept(ContentType.JSON).
			when().
				post("/shipping").
			then().
				statusCode(HttpStatus.OK.getCode()).
				body("carrier", Matchers.is("FEDEX"),
						"deliveryDate", Matchers.is(LocalDate.now().plusDays(1).toString()));

		Blocking.sleep(250);

		JaegerSpan[] localSpans = validateOpsPresent(
				new String[]{"POST /shipping", "Put.process", "ship"},
				reporter.getSpans()
		);
		Arrays.stream(localSpans).forEach(TracingTest::validateTagsForSpan);
	}

	protected static JaegerSpan[] validateOpsPresent(String[] sOpNames, List<JaegerSpan> spans) {
		assertThat(sOpNames, is(notNullValue()));
		assertThat("Spans not recorded.", spans.isEmpty(), is(false));

		JaegerSpan[] spansFound = new JaegerSpan[sOpNames.length];
		for (int i = 0, len = sOpNames.length; i < len; i++) {
			if (spansFound[i] == null) {
				final String searchFor = sOpNames[i];

				spansFound[i] = spans.stream().filter(
						span -> Base.equals(searchFor, span.getOperationName())).findFirst().orElse(null);
			}
		}

		for (int i = 0, len = sOpNames.length; i < len; i++) {
			assertThat("Unable to find operation " + sOpNames[i] + " in spans on the member.",
					spansFound[i],
					is(notNullValue()));
		}
		return spansFound;
	}

	@SuppressWarnings("SameParameterValue")
	protected static void validateTagsForSpan(JaegerSpan span) {
		assertThat(span, is(notNullValue()));
		Map<String, Object> metadata = span.getTags();
		String sOpName = span.getOperationName();

		CacheFactory.log("Validating span: " + span + ", tags: " + span.getTags());

		if (sOpName.startsWith("ship")) {
			assertThat(sOpName + ": incorrect method name",
					metadata.get("method"),
					is("ship"));
		}

		if (sOpName.startsWith("POST")) {
			assertThat(sOpName + ": incorrect http path",
					metadata.get("http.path"),
					is("/shipping"));
		}

		if (sOpName.startsWith("Put.process")) {
			assertThat(sOpName + ": incorrect cache name",
					metadata.get("cache"),
					is("shipments"));
		}
	}
}
