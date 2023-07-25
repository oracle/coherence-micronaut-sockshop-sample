/*
 * Copyright (c) 2021, 2023 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package com.oracle.coherence.examples.sockshop.micronaut.payment;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import java.util.Collection;

import static io.micronaut.http.MediaType.APPLICATION_JSON;

public interface PaymentApi {
    @Get("{orderId}")
    @Produces(APPLICATION_JSON)
    @Operation(summary = "Return the payment authorization for the specified order")
    HttpResponse<Collection<? extends Authorization>> getOrderAuthorizations(@Parameter(description = "Order identifier")
                                        @PathVariable("orderId") String orderId);

    @Post
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    @Operation(summary = "Authorize a payment request")
    Authorization authorize(@RequestBody(description = "Payment request") @Body PaymentRequest paymentRequest);
}
