/*
 * Copyright (c) 2021, 2023 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package com.oracle.coherence.examples.sockshop.micronaut.shipping;


import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.PathVariable;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;


public interface ShippingApi {
    @Operation(summary = "Return the Shipment for the specified order")
    Shipment getShipmentByOrderId(@Parameter(description = "Order identifier")
                                  @PathVariable("orderId") String orderId);

    @Operation(summary = "Ship the specified shipping request")
    Shipment ship(@RequestBody(description = "Shipping request") @Body ShippingRequest req);
}
