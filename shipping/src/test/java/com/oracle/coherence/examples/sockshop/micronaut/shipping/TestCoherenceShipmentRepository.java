/*
 * Copyright (c) 2021 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package com.oracle.coherence.examples.sockshop.micronaut.shipping;

import io.micronaut.coherence.data.annotation.CoherenceRepository;
import io.micronaut.context.annotation.Primary;

@CoherenceRepository("shipments")
@Primary
public abstract class TestCoherenceShipmentRepository extends CoherenceShipmentRepository implements TestShipmentRepository {
    @Override
    public void clear() {
        getMap().truncate();
    }
}
