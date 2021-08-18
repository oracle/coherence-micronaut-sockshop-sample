/*
 * Copyright (c) 2021 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package io.micronaut.examples.sockshop.carts;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Controller;
import io.micronaut.tracing.annotation.NewSpan;

import javax.inject.Inject;


/**
 * Implementation of the Cart Service REST API.
 */
@Controller("/carts")
public class CartResource implements CartApi {

    @Inject
    private CartRepository carts;

    @Override
    @NewSpan
    public Cart getCart(String customerId) {
        return carts.getOrCreateCart(customerId);
    }

    @Override
    @NewSpan
    public HttpResponse deleteCart(String customerId) {
        return carts.deleteCart(customerId)
                ? HttpResponse.accepted()
                : HttpResponse.notFound();
    }

    @Override
    @NewSpan
    public HttpResponse mergeCarts(String customerId, String sessionId) {
        boolean fMerged = carts.mergeCarts(customerId, sessionId);
        return fMerged
                ? HttpResponse.accepted()
                : HttpResponse.status(HttpStatus.NOT_FOUND);
    }
}
