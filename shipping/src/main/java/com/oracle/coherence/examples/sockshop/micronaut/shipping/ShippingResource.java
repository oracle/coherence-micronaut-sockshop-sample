/*
 * Copyright (c) 2021 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package com.oracle.coherence.examples.sockshop.micronaut.shipping;

import io.micrometer.core.annotation.Timed;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;

import io.micronaut.tracing.annotation.NewSpan;

import javax.inject.Inject;

import java.time.LocalDate;

import static io.micronaut.http.MediaType.APPLICATION_JSON;

/**
 * Implementation of the Shipping Service REST API.
 */
@Controller("/shipping")
public class ShippingResource implements ShippingApi {
    /**
     * Shipment repository to use.
     */
    @Inject
    private ShipmentRepository shipments;

    @Get("{orderId}")
    @Produces(APPLICATION_JSON)
    @NewSpan
    public Shipment getShipmentByOrderId(String orderId) {
        return shipments.getShipment(orderId);
    }

    @Post
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    @NewSpan
    @Timed("ship")
    public Shipment ship(@Body ShippingRequest req) {
        // defaults
        String carrier = "USPS";
        String trackingNumber = "9205 5000 0000 0000 0000 00";
        LocalDate deliveryDate = LocalDate.now().plusDays(5);

        if (req.getItemCount() == 1) {  // use FedEx
            carrier = "FEDEX";
            trackingNumber = "231300687629630";
            deliveryDate = LocalDate.now().plusDays(1);
        } else if (req.getItemCount() <= 3) {  // use UPS
            carrier = "UPS";
            trackingNumber = "1Z999AA10123456784";
            deliveryDate = LocalDate.now().plusDays(3);
        }

        Shipment shipment = Shipment.builder()
                .orderId(req.getOrderId())
                .carrier(carrier)
                .trackingNumber(trackingNumber)
                .deliveryDate(deliveryDate)
                .build();

        shipments.saveShipment(shipment);

        return shipment;
    }
}
