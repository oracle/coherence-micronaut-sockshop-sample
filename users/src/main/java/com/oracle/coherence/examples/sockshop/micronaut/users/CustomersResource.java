/*
 * Copyright (c) 2021, 2023 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package com.oracle.coherence.examples.sockshop.micronaut.users;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.tracing.annotation.NewSpan;

import jakarta.inject.Inject;

@Controller("/customers")
public class CustomersResource implements CustomerApi {

    @Inject
    private UserRepository users;

    @Override
    @NewSpan
    public HttpResponse getAllCustomers() {
        return HttpResponse.ok(JsonHelpers.embed("customer", users.getAllUsers()));
    }

    @Override
    @NewSpan
    public HttpResponse getCustomer(String id) {
        return HttpResponse.ok(users.getOrCreate(id));
    }

    @Override
    @NewSpan
    public HttpResponse deleteCustomer(String id) {
        User prev = users.removeUser(id);
        return HttpResponse.ok(JsonHelpers.obj().put("status", prev != null));
    }

    @Override
    @NewSpan
    public HttpResponse getCustomerCards(String id) {
        User user = users.getUser(id);
        return HttpResponse.ok(JsonHelpers.embed("card", user.getCards().stream().map(Card::mask).toArray()));
    }

    @Override
    @NewSpan
    public HttpResponse getCustomerAddresses(String id) {
        User user = users.getUser(id);
        return HttpResponse.ok(JsonHelpers.embed("address", user.getAddresses()));
    }
}
