/*
 * Copyright (c) 2020, 2021 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * http://oss.oracle.com/licenses/upl.
 */

package io.micronaut.examples.sockshop.users;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import static io.micronaut.http.MediaType.APPLICATION_JSON;

public interface AddressApi {
    @Get
    @Produces(APPLICATION_JSON)
    @Operation(summary = "Return all addresses associated with a user; or an empty list if no address found")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "if the retrieval is successful")
    })
    HttpResponse getAllAddresses();

    @Post
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    @Operation(summary = "Register address for a user; no-op if the address exist")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "if address is successfully registered")
    })
    HttpResponse registerAddress(@Parameter(description = "Add Address request") AddressesResource.AddAddressRequest req);

    @Get("{id}")
    @Produces(APPLICATION_JSON)
    @Operation(summary = "Return addresses for the specified identifier")
    Address getAddress(@Parameter(description = "Address identifier")
                       @PathVariable("id") AddressId id);

    @Delete("{id}")
    @Produces(APPLICATION_JSON)
    @Operation(summary = "Delete address for the specified identifier")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "if address is successfully deleted")
    })
    HttpResponse deleteAddress(@Parameter(description = "Address identifier")
                               @PathVariable("id") AddressId id);
}
