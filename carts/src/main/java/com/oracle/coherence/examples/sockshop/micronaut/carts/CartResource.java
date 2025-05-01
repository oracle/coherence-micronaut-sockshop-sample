/*
 * Copyright (c) 2021, 2023 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package com.oracle.coherence.examples.sockshop.micronaut.carts;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Controller;
import io.micronaut.tracing.annotation.NewSpan;

import jakarta.inject.Inject;


/**
 * Implementation of the Cart Service REST API.
 */
@Controller("/carts")
public class CartResource implements CartApi {

    @Inject
    private CartRepository carts;

    @Override
    @NewSpan("get-cart")
    public Cart getCart(String customerId) {
        return carts.getOrCreateCart(customerId);
    }

    @Override
    @NewSpan("delete-cart")
    public HttpResponse deleteCart(String customerId) {
        return carts.deleteCart(customerId)
                ? HttpResponse.accepted()
                : HttpResponse.notFound();
    }

    @Override
    @NewSpan("merge-carts")
    public HttpResponse mergeCarts(String customerId, String sessionId) {
        boolean fMerged = carts.mergeCarts(customerId, sessionId);
        return fMerged
                ? HttpResponse.accepted()
                : HttpResponse.status(HttpStatus.NOT_FOUND);
    }
}
