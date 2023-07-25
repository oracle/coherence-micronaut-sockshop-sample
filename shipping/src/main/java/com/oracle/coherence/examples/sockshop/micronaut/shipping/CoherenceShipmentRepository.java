/*
 * Copyright (c) 2021, 2023 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package com.oracle.coherence.examples.sockshop.micronaut.shipping;

import io.micronaut.coherence.data.AbstractCoherenceRepository;
import io.micronaut.coherence.data.annotation.CoherenceRepository;
import io.micronaut.tracing.annotation.NewSpan;


/**
 * An implementation of {@link ShipmentRepository}
 * that that uses Coherence as a backend data store.
 */
@CoherenceRepository("shipments")
//@Traced
public abstract class CoherenceShipmentRepository
        extends AbstractCoherenceRepository<Shipment, String>
        implements ShipmentRepository {
    @Override
    @NewSpan("getShipment")
    public Shipment getShipment(String orderId) {
        return get(orderId);
    }

    @Override
    @NewSpan("saveShipment")
    public void saveShipment(Shipment shipment) {
        save(shipment);
    }
}
