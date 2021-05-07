/*
 * Copyright (c) 2020, 2021 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * http://oss.oracle.com/licenses/upl.
 */

package io.micronaut.examples.sockshop.shipping;


import io.micronaut.http.annotation.PathVariable;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;


public interface ShippingApi {
    @Operation(summary = "Return the Shipment for the specified order")
    Shipment getShipmentByOrderId(@Parameter(description = "Order identifier")
                                  @PathVariable("orderId") String orderId);

    @Operation(summary = "Ship the specified shipping request")
    Shipment ship(@Parameter(description = "Shipping request") ShippingRequest req);
}
