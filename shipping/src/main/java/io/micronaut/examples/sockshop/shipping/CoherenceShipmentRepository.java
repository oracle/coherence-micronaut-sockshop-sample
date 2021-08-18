/*
 * Copyright (c) 2021 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package io.micronaut.examples.sockshop.shipping;

import io.micronaut.coherence.data.AbstractCoherenceRepository;
import io.micronaut.coherence.data.annotation.CoherenceRepository;

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
    public Shipment getShipment(String orderId) {
        return get(orderId);
    }

    @Override
    public void saveShipment(Shipment shipment) {
        save(shipment);
    }
}
