/*
 * Copyright (c) 2021 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package io.micronaut.examples.sockshop.orders;

import io.micronaut.context.annotation.Primary;

import java.util.List;

import javax.inject.Singleton;

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
