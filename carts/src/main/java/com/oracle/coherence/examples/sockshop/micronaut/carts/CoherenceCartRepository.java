/*
 * Copyright (c) 2021 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package com.oracle.coherence.examples.sockshop.micronaut.carts;

import io.micronaut.coherence.data.AbstractCoherenceRepository;

import io.micronaut.coherence.data.annotation.CoherenceRepository;

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
    public Cart getOrCreateCart(String customerId) {
        return getMap().computeIfAbsent(customerId, v -> new Cart(customerId));
    }

    @Override
    public boolean mergeCarts(String targetId, String sourceId) {
        final Cart source = this.removeById(sourceId, true);
        if (source == null) {
            return false;
        }

        update(targetId, Cart::merge, source, Cart::new);

        return true;
    }

    @Override
    public boolean deleteCart(String customerId) {
            return removeById(customerId);
        }

    @Override
    public List<Item> getItems(final String cartId) {
        return getOrCreateCart(cartId).getItems();
    }

    @Override
    public Item getItem(String cartId, String itemId) {
        return getOrCreateCart(cartId).getItem(itemId);
    }

    @Override
    public Item addItem(String cartId, Item item) {
        return update(cartId, Cart::add, item, Cart::new);
    }

    @Override
    public Item updateItem(String cartId, Item item) {
        return update(cartId, Cart::update, item, Cart::new);
    }

    @Override
    public void deleteItem(String cartId, String itemId) {
        update(cartId, Cart::remove, itemId, Cart::new);
    }
}
