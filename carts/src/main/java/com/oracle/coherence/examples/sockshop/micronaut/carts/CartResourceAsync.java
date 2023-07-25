/*
 * Copyright (c) 2021, 2023 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package com.oracle.coherence.examples.sockshop.micronaut.carts;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.tracing.annotation.NewSpan;

import jakarta.inject.Inject;

import java.util.concurrent.CompletionStage;


/**
 * Implementation of the Cart Service REST API.
 */
@Controller("/carts-async")
public class CartResourceAsync implements CartApiAsync {

    @Inject
    private CartRepositoryAsync carts;

    @Override
    @NewSpan
    public CompletionStage<Cart> getCart(String customerId) {
        return carts.getOrCreateCart(customerId);
    }

    @Override
    @NewSpan
    public CompletionStage<HttpResponse> deleteCart(String customerId) {
        return carts.deleteCart(customerId)
                .thenApply(fDeleted ->
                        fDeleted
                         ? HttpResponse.accepted()
                         : HttpResponse.notFound());
    }

    @Override
    @NewSpan
    public CompletionStage<HttpResponse> mergeCarts(String customerId, String sessionId) {
        return carts.mergeCarts(customerId, sessionId)
                .thenApply(fMerged ->
                       fMerged
                        ? HttpResponse.accepted()
                        : HttpResponse.notFound());
    }
}
