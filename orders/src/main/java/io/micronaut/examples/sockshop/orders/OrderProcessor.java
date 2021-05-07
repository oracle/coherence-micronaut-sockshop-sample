/*
 * Copyright (c) 2020, 2021 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * http://oss.oracle.com/licenses/upl.
 */

package io.micronaut.examples.sockshop.orders;

/**
 * Business interface for the {@code OrderProcessor} service.
 */
public interface OrderProcessor {
    /**
     * Process new order.
     *
     * @param order  the order to process
     */
    void processOrder(Order order);
}
