/*
 * Copyright (c) 2020, 2021 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * http://oss.oracle.com/licenses/upl.
 */

package io.micronaut.examples.sockshop.payment;

import io.micronaut.coherence.data.AbstractCoherenceRepository;
import io.micronaut.coherence.data.annotation.CoherenceRepository;

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
    public void saveAuthorization(Authorization auth) {
        save(auth);
    }

    @Override
    public Collection<? extends Authorization> findAuthorizationsByOrder(String orderId) {
        return getAll(equal(Authorization::getOrderId, orderId));
    }
}
