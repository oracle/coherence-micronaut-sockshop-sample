/*
 * Copyright (c) 2021, 2023 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package com.oracle.coherence.examples.sockshop.micronaut.payment;

import io.micronaut.coherence.data.AbstractCoherenceRepository;
import io.micronaut.coherence.data.annotation.CoherenceRepository;

import io.micronaut.tracing.annotation.NewSpan;
import java.util.Collection;

import static com.tangosol.util.Filters.equal;

/**
 * An implementation of {@link PaymentRepository}
 * that that uses Coherence as a backend data store.
 */
@CoherenceRepository("payments")
public abstract class CoherencePaymentRepository
        extends AbstractCoherenceRepository<Authorization, AuthorizationId>
        implements PaymentRepository {

    @Override
    @NewSpan("saveAuthorization")
    public void saveAuthorization(Authorization auth) {
        save(auth);
    }

    @Override
    @NewSpan("findAuthorizationsByOrder")
    public Collection<? extends Authorization> findAuthorizationsByOrder(String orderId) {
        return getAll(equal(Authorization::getOrderId, orderId));
    }
}
