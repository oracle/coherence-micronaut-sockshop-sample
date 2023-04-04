/*
 * Copyright (c) 2021, 2023 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package com.oracle.coherence.examples.sockshop.micronaut.orders;

import io.micronaut.context.annotation.Primary;

import java.util.List;

import jakarta.inject.Singleton;

//@Mock
@Singleton
@Primary
public class TestCartsClient implements CartsClient {
   public TestCartsClient() {
   }

   public List<Item> cart(String cartId) {
      return TestDataFactory.items(3);
   }
}
