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

import java.util.List;


/**
 * Implementation of Items sub-resource REST API.
 */
@Controller("/carts/{customerId}/items")
public class ItemsResource implements ItemsApi {

    @Inject
    private CartRepository carts;

    @Override
    @NewSpan("get-items")
    public List<Item> getItems(String customerId) {
        return carts.getItems(customerId);
    }

    @Override
    @NewSpan("add-item")
    public HttpResponse<Item> addItem(String customerId, Item item) {
        if (item.getQuantity() == 0) {
            item.setQuantity(1);
        }
        Item result = carts.addItem(customerId, item);
        return HttpResponse.created(result);
    }

    @Override
    @NewSpan("get-items")
    public HttpResponse<Item> getItem(String customerId, String itemId) {
        Item item = carts.getItem(customerId, itemId);
        return item == null
                ? HttpResponse.status(HttpStatus.NOT_FOUND)
                : HttpResponse.ok(item);
    }

    @Override
    @NewSpan("delete-item")
    public HttpResponse deleteItem(String customerId, String itemId) {
        carts.deleteItem(customerId, itemId);
        return HttpResponse.accepted();
    }

    @Override
    @NewSpan("update-item")
    public HttpResponse updateItem(String customerId, Item item) {
        carts.updateItem(customerId, item);
        return HttpResponse.accepted();
    }
}
