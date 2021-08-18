/*
 * Copyright (c) 2021 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package io.micronaut.examples.sockshop.orders;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;

import javax.inject.Singleton;
import java.util.Collections;
import java.util.Map;

import static io.micronaut.http.HttpStatus.NOT_ACCEPTABLE;

/**
 * Exception handler for {@code OrderException}s.
 */
@Singleton
@Produces
public class OrderExceptionHandler implements ExceptionHandler<OrderException, HttpResponse<Map<String, String>>> {
    @Override
    public HttpResponse<Map<String, String>> handle(HttpRequest request, OrderException exception) {
        return HttpResponse
                .status(NOT_ACCEPTABLE)
                .body(Collections.singletonMap("message", exception.getMessage()))
                .contentType(MediaType.APPLICATION_JSON_TYPE);
    }
}