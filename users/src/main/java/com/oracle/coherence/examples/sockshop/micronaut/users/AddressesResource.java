/*
 * Copyright (c) 2021 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package com.oracle.coherence.examples.sockshop.micronaut.users;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.tracing.annotation.NewSpan;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.inject.Inject;
import java.util.Collections;

@Controller("/addresses")
public class AddressesResource implements AddressApi{

    @Inject
    private UserRepository users;

    @Override
    @NewSpan
    public HttpResponse getAllAddresses() {
        return HttpResponse.ok(JsonHelpers.embed("address", Collections.emptyList()));
    }

    @Override
    @NewSpan
    public HttpResponse registerAddress(AddAddressRequest req) {
        Address address = new Address(req.number, req.street, req.city, req.postcode, req.country);
        AddressId id = users.addAddress(req.userID, address);
        return HttpResponse.ok(JsonHelpers.obj().put("id", id.toString()));
    }

    @Override
    @NewSpan
    public Address getAddress(AddressId id) {
        return users.getAddress(id);
    }

    @Override
    @NewSpan
    public HttpResponse deleteAddress(AddressId id) {
        try {
            users.removeAddress(id);
            return status(true);
        }
        catch (RuntimeException e) {
            return status(false);
        }
    }

    // --- helpers ----------------------------------------------------------

    private static HttpResponse status(boolean fSuccess) {
        return HttpResponse.ok(JsonHelpers.obj().put("status", fSuccess));
    }

    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddAddressRequest {
        public String number;
        public String street;
        public String city;
        public String postcode;
        public String country;
        public String userID;
    }
}
