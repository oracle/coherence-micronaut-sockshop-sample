/*
 * Copyright (c) 2021 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package com.oracle.coherence.examples.sockshop.micronaut.carts;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;

/**
 * Integration tests for {@link CartRepository},
 * using Coherence for persistence.
 */
@MicronautTest
public class CoherenceCartResourceAsyncTest extends CoherenceCartResourceTest {
    protected String getBasePath() {
        return "/carts-async";
    }

    protected CartRepository getCartsRepository() {
        return new SyncCartRepository(context.getBean(CartRepositoryAsync.class));
    }
}
