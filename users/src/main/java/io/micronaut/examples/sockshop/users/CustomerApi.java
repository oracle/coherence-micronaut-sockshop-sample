/*
 * Copyright (c) 2021 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package io.micronaut.examples.sockshop.users;


import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Produces;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import static io.micronaut.http.MediaType.APPLICATION_JSON;

public interface CustomerApi {
    @Get
    @Produces(APPLICATION_JSON)
    @Operation(summary = "Return all customers; or empty collection if no customer found")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "if the retrieval is successful")
    })
    HttpResponse getAllCustomers();

    @Get("{id}")
    @Produces(APPLICATION_JSON)
    @Operation(summary = "Return customer for the specified identifier")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "if the retrieval is successful")
    })
    HttpResponse getCustomer(@Parameter(description = "Customer identifier")
                             @PathVariable("id") String id);

    @Delete("{id}")
    @Produces(APPLICATION_JSON)
    @Operation(summary = "Delete customer for the specified identifier")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "if the delete is successful")
    })
    HttpResponse deleteCustomer(@Parameter(description = "Customer identifier")
                                @PathVariable("id") String id);

    @Get("{id}/cards")
    @Produces(APPLICATION_JSON)
    @Operation(summary = "Return all cards for the specified customer identifier")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "if the retrieval is successful")
    })
    HttpResponse getCustomerCards(@Parameter(description = "Customer identifier")
                                  @PathVariable("id") String id);

    @Get("{id}/addresses")
    @Produces(APPLICATION_JSON)
    @Operation(summary = "Return all addresses for the specified customer identifier")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "if the retrieval is successful")
    })
    HttpResponse getCustomerAddresses(@Parameter(description = "Customer identifier")
                                      @PathVariable("id") String id);
}
