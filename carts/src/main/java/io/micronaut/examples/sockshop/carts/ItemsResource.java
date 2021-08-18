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

import java.util.List;


/**
 * Implementation of Items sub-resource REST API.
 */
@Controller("/carts/{customerId}/items")
public class ItemsResource implements ItemsApi {

    @Inject
    private CartRepository carts;

    @Override
    @NewSpan
    public List<Item> getItems(String customerId) {
        return carts.getItems(customerId);
    }

    @Override
    @NewSpan
    public HttpResponse<Item> addItem(String customerId, Item item) {
        if (item.getQuantity() == 0) {
            item.setQuantity(1);
        }
        Item result = carts.addItem(customerId, item);
        return HttpResponse.created(result);
    }

    @Override
    @NewSpan
    public HttpResponse<Item> getItem(String customerId, String itemId) {
        Item item = carts.getItem(customerId, itemId);
        return item == null
                ? HttpResponse.status(HttpStatus.NOT_FOUND)
                : HttpResponse.ok(item);
    }

    @Override
    @NewSpan
    public HttpResponse deleteItem(String customerId, String itemId) {
        carts.deleteItem(customerId, itemId);
        return HttpResponse.accepted();
    }

    @Override
    @NewSpan
    public HttpResponse updateItem(String customerId, Item item) {
        carts.updateItem(customerId, item);
        return HttpResponse.accepted();
    }
}
