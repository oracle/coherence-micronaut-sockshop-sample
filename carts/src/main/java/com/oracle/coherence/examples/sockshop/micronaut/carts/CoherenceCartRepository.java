/*
 * Copyright (c) 2021, 2023 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package com.oracle.coherence.examples.sockshop.micronaut.carts;

import io.micronaut.coherence.data.AbstractCoherenceRepository;

import io.micronaut.coherence.data.annotation.CoherenceRepository;

import io.micronaut.tracing.annotation.NewSpan;
import java.util.List;

/**
 * An implementation of {@link CartRepository}
 * that that uses Coherence as a backend data store.
 */
@CoherenceRepository("carts")
public abstract class CoherenceCartRepository
        extends AbstractCoherenceRepository<Cart, String>
        implements CartRepository {

    @Override
    @NewSpan("getOrCreateCart")
    public Cart getOrCreateCart(String customerId) {
        return getMap().computeIfAbsent(customerId, v -> new Cart(customerId));
    }

    @Override
    @NewSpan("mergeCarts")
    public boolean mergeCarts(String targetId, String sourceId) {
        final Cart source = this.removeById(sourceId, true);
        if (source == null) {
            return false;
        }

        update(targetId, Cart::merge, source, Cart::new);

        return true;
    }

    @Override
    @NewSpan("deleteCart")
    public boolean deleteCart(String customerId) {
            return removeById(customerId);
        }

    @Override
    @NewSpan("getItems")
    public List<Item> getItems(final String cartId) {
        return getOrCreateCart(cartId).getItems();
    }

    @Override
    @NewSpan("getItem")
    public Item getItem(String cartId, String itemId) {
        return getOrCreateCart(cartId).getItem(itemId);
    }

    @Override
    @NewSpan("addItem")
    public Item addItem(String cartId, Item item) {
        return update(cartId, Cart::add, item, Cart::new);
    }

    @Override
    @NewSpan("updateItem")
    public Item updateItem(String cartId, Item item) {
        return update(cartId, Cart::update, item, Cart::new);
    }

    @Override
    @NewSpan("deleteItem")
    public void deleteItem(String cartId, String itemId) {
        update(cartId, Cart::remove, itemId, Cart::new);
    }
}
