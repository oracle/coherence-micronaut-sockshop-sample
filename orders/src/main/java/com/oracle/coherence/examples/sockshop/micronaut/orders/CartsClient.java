/*
 * Copyright (c) 2021 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package com.oracle.coherence.examples.sockshop.micronaut.orders;

import java.util.List;


import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.client.annotation.Client;
//import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;


/**
 * Client-side interface for Carts REST service.
 */
//@RegisterRestClient(baseUri = "http://carts/")
@Client("http://carts/")
public interface CartsClient {
   /**
    * Get cart items.
    *
    * @param cartId  cart identifier
    *
    * @return cart items from the specified cart
    */
   @Get("/carts/{cartId}/items")
   @Consumes(MediaType.APPLICATION_JSON)
   List<Item> cart(@PathVariable("cartId") String cartId);
}
