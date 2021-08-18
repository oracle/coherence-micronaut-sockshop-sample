/*
 * Copyright (c) 2021 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package io.micronaut.examples.sockshop.orders;

import io.micrometer.core.annotation.Timed;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;

import io.micronaut.tracing.annotation.NewSpan;
import lombok.extern.java.Log;

import javax.inject.Inject;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Implementation of the Orders Service REST API.
 */
@Controller("/orders")
@Log
public class OrderResource implements OrderApi {
    /**
     * Order repository to use.
     */
    @Inject
    private OrderRepository orders;

    /**
     * Order processor to use.
     */
    @Inject
    private OrderProcessor processor;

    @Inject
    protected CartsClient cartsService;

    @Inject
    protected UsersClient usersService;

    @Override
    @NewSpan
    public HttpResponse getOrdersForCustomer(String customerId) {
        Collection<? extends Order> customerOrders = orders.findOrdersByCustomerId(customerId);
        if (customerOrders.isEmpty()) {
            return HttpResponse.notFound();
        }
        return wrap(customerOrders);
    }

    private HttpResponse wrap(Object value) {
        Map<String, Map<String, Object>> map = Collections.singletonMap("_embedded", Collections.singletonMap("customerOrders", value));
        return HttpResponse.ok(map);
    }

    @Override
    @NewSpan
    public HttpResponse getOrder(String orderId) {
        Order order = orders.get(orderId);
        return order == null
                ? HttpResponse.notFound()
                : HttpResponse.ok(order);
    }

    @Override
    @NewSpan
    @Timed("order.new")
    public HttpResponse newOrder(NewOrderRequest request) {
        log.info("Processing new order: " + request);

        if (request.address == null || request.customer == null || request.card == null || request.items == null) {
            throw new InvalidOrderException("Invalid order request. Order requires customer, address, card and items.");
        }

        String itemsPath = request.items.getPath();
        String addressPath = request.address.getPath();
        String cardPath = request.card.getPath();
        String customerPath = request.customer.getPath();
        if (!itemsPath.startsWith("/carts/") || !itemsPath.endsWith("/items") ||
            !addressPath.startsWith("/addresses/") ||
            !cardPath.startsWith("/cards/") ||
            !customerPath.startsWith("/customers/")) {
            throw new InvalidOrderException("Invalid order request. Order requires the URIs to have path /customers/xxx, /addresses/xxx, /cards/xxx and /carts/xxx/items.");
        }

        List<Item> items    = cartsService.cart(itemsPath.substring(7, itemsPath.length() - 6));
        Address    address  = usersService.address(addressPath.substring(11));
        Card       card     = usersService.card(cardPath.substring(7));
        Customer   customer = usersService.customer(customerPath.substring(11));

        Order order = Order.builder()
                .customer(customer)
                .address(address)
                .card(card)
                .items(items)
                .build();

        processor.processOrder(order);

        log.info("Created Order: " + order.getOrderId());
        return HttpResponse.created(order);
    }

    // ---- inner class: InvalidOrderException ------------------------------

    /**
     * An exception that is thrown if the arguments in the {@code NewOrderRequest}
     * are invalid.
     */
    public static class InvalidOrderException extends OrderException {
        public InvalidOrderException(String s) {
            super(s);
        }
    }
}
