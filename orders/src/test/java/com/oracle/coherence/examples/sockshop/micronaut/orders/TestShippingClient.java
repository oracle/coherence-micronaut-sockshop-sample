/*
 * Copyright (c) 2021, 2023 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package com.oracle.coherence.examples.sockshop.micronaut.orders;


import io.micronaut.context.annotation.Primary;

import jakarta.inject.Singleton;

//@Mock
@Singleton
@Primary
public class TestShippingClient implements ShippingClient {
   public TestShippingClient() {
   }

   public Shipment ship(ShippingRequest request) {
      return TestDataFactory.shipment(request.getCustomer().getId());
   }
}
