/*
 * Copyright (c) 2021 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package com.oracle.coherence.examples.sockshop.micronaut.users;


import io.micronaut.test.extensions.junit5.annotation.MicronautTest;

/**
 * Integration tests for {@link UserResource},
 * using Coherence for persistence.
 */
@MicronautTest
public class CoherenceUserResourceTest extends UserResourceTest {
    protected UserRepository getUserRepository() {
        return context.getBean(UserRepository.class);
    }
}
