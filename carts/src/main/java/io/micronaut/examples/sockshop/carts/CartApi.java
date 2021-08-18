/*
 * Copyright (c) 2021 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package io.micronaut.examples.sockshop.carts;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.annotation.QueryValue;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import static io.micronaut.http.MediaType.APPLICATION_JSON;


/**
 * REST API for {@code /carts} service.
 */
public interface CartApi {
    @Get("{customerId}")
    @Produces(APPLICATION_JSON)
    @Operation(summary = "Return customer's shopping cart")
    Cart getCart(@Parameter(name = "customerId", description = "Customer identifier")
                 @PathVariable("customerId") String customerId);

    @Delete("{customerId}")
    @Operation(summary = "Delete customer's shopping cart")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "if the shopping cart was successfully deleted"),
            @ApiResponse(responseCode = "404", description = "if the shopping cart doesn't exist")
    })
    HttpResponse deleteCart(@Parameter(name = "customerId", description = "Customer identifier")
                            @PathVariable("customerId") String customerId);

    @Get("{customerId}/merge")
    @Operation(summary = "Merge one shopping cart into another",
            description = "Customer can add products to a shopping cart anonymously, "
                    + "but when she logs in the anonymous shopping cart needs to be merged "
                    + "into the customer's own shopping cart")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "if the shopping carts were successfully merged"),
            @ApiResponse(responseCode = "404", description = "if the session shopping cart doesn't exist")
    })
    HttpResponse mergeCarts(@Parameter(name = "customerId", description = "Customer identifier")
                            @PathVariable("customerId") String customerId,
                            @Parameter(name = "sessionId", required = true, description = "Anonymous session identifier")
                            @QueryValue("sessionId") String sessionId);
}
