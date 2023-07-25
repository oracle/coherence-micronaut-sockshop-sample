/*
 * Copyright (c) 2021, 2023 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package com.oracle.coherence.examples.sockshop.micronaut.carts;


import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Patch;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;
import java.util.concurrent.CompletionStage;

import static io.micronaut.http.MediaType.APPLICATION_JSON;


/**
 * REST API for {@code /items} sub-resource.
 */
public interface ItemsApiAsync {
    @Get
    @Produces(APPLICATION_JSON)
    @Operation(summary = "Return the list of products in the customer's shopping cart")
    @ApiResponse(
            responseCode = "200",
            description = "The list of products in the customer's shopping cart",
            content = @Content(mediaType = APPLICATION_JSON,
                               array = @ArraySchema(
                               schema = @Schema(implementation = Item.class))))
    CompletionStage<List<Item>> getItems(@PathVariable("customerId") String customerId);

    @Post
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    @Operation(summary = "Add item to the shopping cart",
               description = "This operation will add item to the shopping cart if it "
                       + "doesn't already exist, or increment quantity by the specified "
                       + "number of items if it does")
    @ApiResponse(responseCode = "201",
                 description = "Added item",
                 content = @Content(mediaType = APPLICATION_JSON,
                                    schema = @Schema(implementation = Item.class)))
    CompletionStage<HttpResponse<Item>> addItem(@PathVariable("customerId") String customerId,
                                                @RequestBody(description = "Item to add to the cart") @Body Item item);

    @Get("{itemId}")
    @Produces(APPLICATION_JSON)
    @Operation(summary = "Return specified item from the shopping cart")
    @ApiResponses({
        @ApiResponse(responseCode = "200",
                     description = "If specified item exists in the cart",
                     content = @Content(mediaType = APPLICATION_JSON,
                                        schema = @Schema(implementation = Item.class))),
        @ApiResponse(responseCode = "404",
                     description = "If specified item does not exist in the cart")
    })
    CompletionStage<HttpResponse<Item>> getItem(@PathVariable("customerId") String customerId,
                                                @Parameter(name = "itemId", description = "Item identifier")
                                                @PathVariable("itemId") String itemId);

    @Delete("{itemId}")
    @Operation(summary = "Remove specified item from the shopping cart, if it exists")
    @ApiResponse(responseCode = "202", description = "Regardless of whether the specified item exists in the cart")
    CompletionStage<HttpResponse> deleteItem(@PathVariable("customerId") String customerId,
                                             @Parameter(name = "itemId", description = "Item identifier")
                                             @PathVariable("itemId") String itemId);

    @Patch
    @Consumes(APPLICATION_JSON)
    @Operation(summary = "Update item in a shopping cart",
               description = "This operation will add item to the shopping cart if it "
                       + "doesn't already exist, or replace it with the specified item "
                       + "if it does")
    @ApiResponse(responseCode = "202", description = "Regardless of whether the specified item exists in the cart")
    CompletionStage<HttpResponse> updateItem(@PathVariable("customerId") String customerId,
                                             @RequestBody(description = "Item to update") @Body Item item);
}
