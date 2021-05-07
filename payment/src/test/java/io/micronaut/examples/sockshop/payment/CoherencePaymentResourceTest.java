/*
 * Copyright (c) 2020, 2021 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * http://oss.oracle.com/licenses/upl.
 */

package io.micronaut.examples.sockshop.payment;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;

/**
 * Integration tests for {@link PaymentResource},
 * using Coherence for persistence.
 */
@MicronautTest
public class CoherencePaymentResourceTest extends PaymentResourceTest {
}
