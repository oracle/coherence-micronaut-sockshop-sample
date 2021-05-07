/*
 * Copyright (c) 2020, 2021 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * http://oss.oracle.com/licenses/upl.
 */

package io.micronaut.examples.sockshop.orders;


import io.micronaut.context.annotation.Primary;

import javax.inject.Singleton;

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
