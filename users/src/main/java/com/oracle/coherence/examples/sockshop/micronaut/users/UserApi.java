/*
 * Copyright (c) 2021, 2023 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package com.oracle.coherence.examples.sockshop.micronaut.users;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import static io.micronaut.http.MediaType.APPLICATION_JSON;

public interface UserApi {
    @Get("login")
    @Produces(APPLICATION_JSON)
    @Operation(summary = "Basic user authentication")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "if user is successfully authenticated"),
            @ApiResponse(responseCode = "401", description = "if authentication fail")
    })
    HttpResponse login(@Parameter(description = "Basic authentication header")
                       @Header("Authorization") String auth);

    @Post("register")
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    @Operation(summary = "Register a user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "if user is successfully registered"),
            @ApiResponse(responseCode = "409", description = "if the user is already registered")
    })
    HttpResponse register(@RequestBody(description = "The user to be registered") @Body User user);
}
