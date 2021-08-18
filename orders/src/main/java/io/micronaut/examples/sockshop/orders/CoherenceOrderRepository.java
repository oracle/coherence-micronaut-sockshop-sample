/*
 * Copyright (c) 2021 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package io.micronaut.examples.sockshop.orders;

import java.util.Collection;
import java.util.Collections;

import com.tangosol.util.Filters;

import io.micronaut.coherence.data.AbstractCoherenceRepository;

import io.micronaut.coherence.data.annotation.CoherenceRepository;

/**
 * An implementation of {@link OrderRepository}
 * that that uses Coherence as a backend data store.
 */
@CoherenceRepository("orders")
public abstract class CoherenceOrderRepository
        extends AbstractCoherenceRepository<Order, String>
        implements OrderRepository {

    @Override
    public Collection<? extends Order> findOrdersByCustomerId(String customerId) {
        Collection<Order> customerOrders = getMap()
                .values(Filters.equal(o -> ((Order) o).getCustomer().getId(), customerId), null);
        return customerOrders.isEmpty() ? Collections.emptyList() : customerOrders;
    }

    @Override
    public void saveOrder(Order order) {
        save(order);
    }
}
