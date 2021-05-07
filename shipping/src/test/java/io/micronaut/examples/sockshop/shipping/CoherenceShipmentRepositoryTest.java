/*
 * Copyright (c) 2020, 2021 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * http://oss.oracle.com/licenses/upl.
 */

package io.micronaut.examples.sockshop.shipping;

import io.micronaut.context.ApplicationContext;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;

import javax.inject.Inject;

/**
 * Tests for Coherence repository implementation.
 */
@MicronautTest
class CoherenceShipmentRepositoryTest extends ShipmentRepositoryTest {

    @Inject
    ApplicationContext context;

    public TestShipmentRepository getShipmentRepository() {
        return context.getBean(TestShipmentRepository.class);
    }
}
