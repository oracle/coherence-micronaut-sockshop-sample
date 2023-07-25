/*
 * Copyright (c) 2021, 2023 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package com.oracle.coherence.examples.sockshop.micronaut.orders;


import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.annotation.QueryValue;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import static io.micronaut.http.MediaType.APPLICATION_JSON;

public interface OrderApi {
    @Get("search/customerId")
    @Produces(APPLICATION_JSON)
    @Operation(summary = "Return the orders for the specified customer")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "if orders exist"),
            @ApiResponse(responseCode = "404", description = "if orders do not exist")
    })
    HttpResponse getOrdersForCustomer(@Parameter(description = "Customer identifier")
                                  @QueryValue("custId") String customerId);

    @Get("{id}")
    @Produces(APPLICATION_JSON)
    @Operation(summary = "Return the order for the specified order")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "if the order exist"),
            @ApiResponse(responseCode = "404", description = "if the order doesn't exist")
    })
    HttpResponse getOrder(@Parameter(description = "Order identifier")
                      @PathVariable("id") String orderId);

    @Post
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    @Operation(summary = "Place a new order for the specified order request")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "if the request is successfully processed"),
            @ApiResponse(responseCode = "406", description = "if the payment is not authorized")
    })
    HttpResponse newOrder(@RequestBody(description = "Order request") @Body NewOrderRequest request);
}
