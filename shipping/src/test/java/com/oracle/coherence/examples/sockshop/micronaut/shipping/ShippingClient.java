/*
 * Copyright (c) 2021 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package com.oracle.coherence.examples.sockshop.micronaut.shipping;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.client.annotation.Client;

import static io.micronaut.http.MediaType.APPLICATION_JSON;

@Client("/shipping")
public interface ShippingClient extends ShippingApi {
    @Override
    @Get("{orderId}")
    @Consumes(APPLICATION_JSON)
    Shipment getShipmentByOrderId(String orderId);

    @Override
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    @Post
    Shipment ship(@Body ShippingRequest request);
}