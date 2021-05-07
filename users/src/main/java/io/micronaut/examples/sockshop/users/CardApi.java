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

public interface CardApi {
    @Get
    @Produces(APPLICATION_JSON)
    @Operation(summary = "Return all cards associated with a user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "if retrieval is successful")
    })
    HttpResponse getAllCards();

    @Post
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    @Operation(summary = "Register a credit card for a user; no-op if the card exist")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "if card is successfully registered")
    })
    HttpResponse registerCard(@Parameter(description = "Add card request") CardsResource.AddCardRequest req);

    @Get("{id}")
    @Produces(APPLICATION_JSON)
    @Operation(summary = "Return card for the specified identifier")
    Card getCard(@Parameter(description = "Card identifier")
                 @PathVariable("id") CardId id);

    @Delete("{id}")
    @Produces(APPLICATION_JSON)
    @Operation(summary = "Delete card for the specified identifier")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "if card is successfully deleted")
    })
    HttpResponse deleteCard(@Parameter(description = "Card identifier")
                            @PathVariable("id") CardId id);
}
