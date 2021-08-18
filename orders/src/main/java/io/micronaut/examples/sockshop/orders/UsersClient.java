/*
 * Copyright (c) 2021 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package io.micronaut.examples.sockshop.orders;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.client.annotation.Client;

@Client("http://user/")
public interface UsersClient {
    @Get("/addresses/{addressId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Address address(@PathVariable("addressId") String addressId);

    @Get("/cards/{cardId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Card card(@PathVariable("cardId") String cardId);

    @Get("/customers/{customerId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Customer customer(@PathVariable("customerId") String customerId);
}
