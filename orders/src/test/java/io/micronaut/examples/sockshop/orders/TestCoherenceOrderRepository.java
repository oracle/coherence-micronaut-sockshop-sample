/*
 * Copyright (c) 2021 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package io.micronaut.examples.sockshop.orders;

import io.micronaut.coherence.data.annotation.CoherenceRepository;
import io.micronaut.context.annotation.Primary;

@CoherenceRepository("orders")
@Primary
public abstract class TestCoherenceOrderRepository extends CoherenceOrderRepository
        implements TestOrderRepository {
    private String lastOrderId;

    public void clear() {
        getMap().truncate();
    }

    @Override
    public Order save(Order order) {
        super.save(order);
        lastOrderId = order.getOrderId();
        return order;
    }

    @Override
    public String getLastOrderId() {
        return lastOrderId;
    }
}
