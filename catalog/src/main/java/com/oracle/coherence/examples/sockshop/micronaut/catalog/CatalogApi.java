/*
 * Copyright (c) 2021, 2023 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package com.oracle.coherence.examples.sockshop.micronaut.catalog;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.annotation.QueryValue;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.annotation.Nullable;

import java.util.Collection;

import static io.micronaut.http.MediaType.APPLICATION_JSON;

/**
 * REST API for {@code /catalog} service.
 */
public interface CatalogApi {
    @Get
    @Produces(APPLICATION_JSON)
    @Operation(summary = "Return the socks that match the specified query parameters")
    Collection<? extends Sock> getSocks(@Parameter(description = "tag identifiers")
                                        @QueryValue("tags") String tags,
                                        @Parameter(name = "order", description = "order identifier")
                                        @QueryValue(value = "order", defaultValue = "price") String order,
                                        @Parameter(description = "page number")
                                        @QueryValue(value = "page", defaultValue = "1") int pageNum,
                                        @Parameter(description = "page size")
                                        @QueryValue(value = "size", defaultValue = "10") int pageSize);

    @Get("/size")
    @Produces(APPLICATION_JSON)
    @Operation(summary = "Return sock count for the specified tag identifiers")
    CatalogResource.Count getSockCount(@Parameter(description = "tag identifiers")
                                       @Nullable @QueryValue("tags") String tags);

    @SuppressWarnings("rawtypes")
    @Get("/{id}")
    @Produces(APPLICATION_JSON)
    @Operation(summary = "Return socks for the specified sock identifier")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "if socks are found"),
            @ApiResponse(responseCode = "404", description = "if socks do not exist")
    })
    HttpResponse getSock(@Parameter(description = "sock identifier")
                         @PathVariable("id") String sockId);

    @SuppressWarnings("rawtypes")
    @Get("/images/{image}")
    @Produces(MediaType.IMAGE_JPEG)
    @Operation(summary = "Return the sock images for the specified image identifer")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "if image is found"),
            @ApiResponse(responseCode = "404", description = "if image does not exist")
    })
    HttpResponse getImage(@Parameter(description = "image identifier")
                          @PathVariable("image") String image);
}
