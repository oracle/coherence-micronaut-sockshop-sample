/*
 * Copyright (c) 2020, 2021 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * http://oss.oracle.com/licenses/upl.
 */

package io.micronaut.examples.sockshop.carts;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.tracing.annotation.NewSpan;

import javax.inject.Inject;

import java.util.List;
import java.util.concurrent.CompletionStage;


/**
 * Implementation of Items sub-resource REST API.
 */
@Controller("/carts-async/{customerId}/items")
public class ItemsResourceAsync implements ItemsApiAsync {

    @Inject
    private CartRepositoryAsync carts;

    @Override
    @NewSpan
    public CompletionStage<List<Item>> getItems(String customerId) {
        return carts.getItems(customerId);
    }

    @Override
    @NewSpan
    public CompletionStage<HttpResponse<Item>> addItem(String customerId, Item item) {
        if (item.getQuantity() == 0) {
            item.setQuantity(1);
        }

        return carts.addItem(customerId, item)
                    .thenApply(HttpResponse::created);
    }

    @Override
    @NewSpan
    public CompletionStage<HttpResponse<Item>> getItem(String customerId, String itemId) {
        return carts.getItem(customerId, itemId)
                    .thenApply(item ->
                            item == null
                            ? HttpResponse.notFound()
                            : HttpResponse.ok(item));
    }

    @Override
    @NewSpan
    public CompletionStage<HttpResponse> deleteItem(String customerId, String itemId) {
        return carts.deleteItem(customerId, itemId)
                .thenApply(ignore -> HttpResponse.accepted());
    }

    @Override
    @NewSpan
    public CompletionStage<HttpResponse> updateItem(String customerId, Item item) {
        return carts.updateItem(customerId, item)
                .thenApply(ignore -> HttpResponse.accepted());
    }
}
