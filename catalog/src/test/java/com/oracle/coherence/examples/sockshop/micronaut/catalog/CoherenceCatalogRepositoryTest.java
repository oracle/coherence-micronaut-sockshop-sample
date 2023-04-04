/*
 * Copyright (c) 2021, 2023 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package com.oracle.coherence.examples.sockshop.micronaut.catalog;

import io.micronaut.context.ApplicationContext;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;

import jakarta.inject.Inject;

/**
 * Tests for Coherence repository implementation.
 */
@MicronautTest
class CoherenceCatalogRepositoryTest extends CatalogRepositoryTest {

    @Inject
    ApplicationContext context;


    public CatalogRepository getCatalogRepository() {
        return context.getBean(CatalogRepository.class);
    }
}