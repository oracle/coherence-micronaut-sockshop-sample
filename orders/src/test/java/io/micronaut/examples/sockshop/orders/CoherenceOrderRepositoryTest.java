/*
 * Copyright (c) 2020, 2021 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * http://oss.oracle.com/licenses/upl.
 */

package io.micronaut.examples.sockshop.orders;

import io.micronaut.context.ApplicationContext;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Tests for Coherence repository implementation.
 */
@MicronautTest
class CoherenceOrderRepositoryTest {

    @Inject
    ApplicationContext context;


    private TestOrderRepository orders;

    @BeforeEach
    void setup() {
        orders = getOrderRepository();
        orders.clear();
    }

    @Test
    void testFindOrdersByCustomer() {
        orders.saveOrder(TestDataFactory.order("homer", 1));
        orders.saveOrder(TestDataFactory.order("homer", 2));
        orders.saveOrder(TestDataFactory.order("marge", 5));

        assertThat(orders.findOrdersByCustomerId("homer").size(), is(2));
        assertThat(orders.findOrdersByCustomerId("marge").size(), is(1));
    }

    @Test
    void testOrderCreation() {
        Order order = TestDataFactory.order("homer", 1);
        orders.saveOrder(order);

        assertThat(orders.get(order.getOrderId()), is(order));
    }

    public TestOrderRepository getOrderRepository() {
        return context.getBean(TestOrderRepository.class);
    }
}