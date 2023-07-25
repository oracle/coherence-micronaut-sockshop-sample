/*
 * Copyright (c) 2021, 2023 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package com.oracle.coherence.examples.sockshop.micronaut.users;

import com.oracle.coherence.common.base.Blocking;
import com.tangosol.net.CacheFactory;
import com.tangosol.util.Base;
import io.jaegertracing.internal.JaegerSpan;
import io.micronaut.context.annotation.Property;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.restassured.RestAssured;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
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
				contentType(JSON).
				body(new User("bar", "passbar", "bar@weavesocks.com", "baruser", "pass")).
			when().
				post("/register").
			then().
				statusCode(200).
				body("id", is("baruser"));

		Blocking.sleep(250);

		List<JaegerSpan> spans = reporter.getSpans();

		validateNoNullOperationNames(spans);

		JaegerSpan[] localSpans = validateOpsPresent(
				new String[]{"POST /register", "Invoke.process", "register", "registerUser"},
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
			assertThat("Unable to find operation [" + sOpNames[i] +
					   "] in spans on the member. Captured spans [" + spans + ']',
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

		if (sOpName.startsWith("register")) {
			assertThat(sOpName + ": incorrect method name",
					metadata.get("method"),
					is("register"));
		}

		if (sOpName.startsWith("POST")) {
			assertThat(sOpName + ": incorrect http path",
					metadata.get("http.path"),
					is("/register"));
		}

		if (sOpName.startsWith("Invoke.process")) {
			assertThat(sOpName + ": incorrect cache name",
					metadata.get("cache"),
					is("users"));
		}
	}

    protected static void validateNoNullOperationNames(List<JaegerSpan> spans) {
    	Optional<JaegerSpan> nullSpan =
    			spans.stream()
    					.filter(jaegerSpan -> jaegerSpan.getOperationName() == null).findAny();

    	if (nullSpan.isPresent()) {
    		Assertions.fail(String.format("Found Span will null operation name.  Spans: %s", spans));
		}
	}
}
