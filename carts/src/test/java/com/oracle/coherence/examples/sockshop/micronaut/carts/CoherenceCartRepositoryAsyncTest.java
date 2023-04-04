/*
 * Copyright (c) 2021, 2023 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package com.oracle.coherence.examples.sockshop.micronaut.carts;

import io.micronaut.context.ApplicationContext;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;

import jakarta.inject.Inject;

/**
 * Tests for Coherence repository implementation.
 */
@MicronautTest
class CoherenceCartRepositoryAsyncTest extends CartRepositoryTest {

    @Inject
    ApplicationContext context;

    @Override
    protected CartRepository getCartRepository() {
        CartRepositoryAsync cartsAsync = context.getBean(CartRepositoryAsync.class);
        return new SyncCartRepository(cartsAsync);
    }
}
