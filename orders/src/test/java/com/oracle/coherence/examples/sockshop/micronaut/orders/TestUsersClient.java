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
public class TestUsersClient implements UsersClient {
   public TestUsersClient() {
   }

   public Address address(String addressId) {
      return TestDataFactory.address();
   }

   public Card card(String cardId) {
      return TestDataFactory.card();
   }

   public Customer customer(String customerId) {
      return TestDataFactory.customer(customerId);
   }
}
