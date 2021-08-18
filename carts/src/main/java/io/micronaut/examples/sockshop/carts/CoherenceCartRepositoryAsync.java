/*
 * Copyright (c) 2021 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package io.micronaut.examples.sockshop.carts;

import io.micronaut.coherence.data.AbstractCoherenceAsyncRepository;
import io.micronaut.coherence.data.annotation.CoherenceRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * An implementation of {@link CartRepository} that that uses Coherence as a backend data
 * store.
 */
@CoherenceRepository("carts")
public abstract class CoherenceCartRepositoryAsync
        extends AbstractCoherenceAsyncRepository<Cart, String>
        implements CartRepositoryAsync {

    @Override
    public CompletionStage<Cart> getOrCreateCart(String customerId) {
        return getMap().computeIfAbsent(customerId, v -> new Cart(customerId));
    }

    @Override
    public CompletionStage<Boolean> mergeCarts(String targetId, String sourceId) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        removeById(sourceId, true)
                .whenComplete((source, t1) -> {
                    if (t1 != null) {
                        future.completeExceptionally(t1);
                    } else {
                        if (source == null) {
                            future.complete(false);
                        } else {
                            update(targetId, Cart::merge, source, Cart::new)
                            .whenComplete((target, t2) -> {
                                if (t2 != null) {
                                    future.completeExceptionally(t2);
                                } else {
                                    future.complete(true);
                                }
                            });
                        }
                    }
                });

        return future;
    }

    @Override
    public CompletionStage<Boolean> deleteCart(final String customerId) {
        return removeById(customerId);
    }

    @Override
    public CompletionStage<Item> getItem(String cartId, String itemId) {
        return getOrCreateCart(cartId).thenApply(cart -> cart.getItem(itemId));
    }

    @Override
    public CompletionStage<List<Item>> getItems(String cartId) {
        return getOrCreateCart(cartId).thenApply(Cart::getItems);
    }

    @Override
    public CompletionStage<Item> addItem(String cartId, Item item) {
        return update(cartId, Cart::add, item, Cart::new);
    }

    @Override
    public CompletionStage<Item> updateItem(String cartId, Item item) {
        return update(cartId, Cart::update, item, Cart::new);
    }

    @Override
    public CompletionStage<Void> deleteItem(String cartId, String itemId) {
        return update(cartId, Cart::remove, itemId, Cart::new).thenAccept(cart -> {});
    }
}
