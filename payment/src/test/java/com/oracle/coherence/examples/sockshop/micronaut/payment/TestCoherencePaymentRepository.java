/*
 * Copyright (c) 2021 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package com.oracle.coherence.examples.sockshop.micronaut.payment;

import io.micronaut.coherence.data.annotation.CoherenceRepository;
import io.micronaut.context.annotation.Primary;

@Primary
@CoherenceRepository("payments")
public abstract class TestCoherencePaymentRepository extends CoherencePaymentRepository implements TestPaymentRepository {

    @Override
    public void clear() {
        getMap().truncate();
    }
}
