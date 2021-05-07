/*
 * Copyright (c) 2020, 2021 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * http://oss.oracle.com/licenses/upl.
 */

package io.micronaut.examples.sockshop.shipping;

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