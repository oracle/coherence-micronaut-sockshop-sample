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
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import jakarta.inject.Inject;
import java.util.Collections;

@Controller("/cards")
public class CardsResource implements CardApi{

    @Inject
    private UserRepository users;

    @Override
    @NewSpan
    public HttpResponse getAllCards() {
        return HttpResponse.ok(JsonHelpers.embed("card", Collections.emptyList()));
    }

    @Override
    @NewSpan
    public HttpResponse registerCard(AddCardRequest req) {
        Card card = new Card(req.longNum, req.expires, req.ccv);
        CardId id = users.addCard(req.userID, card);

        return HttpResponse.ok(JsonHelpers.obj().put("id", id.toString()));
    }

    @Override
    @NewSpan
    public Card getCard(CardId id) {
        return users.getCard(id).mask();
    }

    @Override
    @NewSpan
    public HttpResponse deleteCard(CardId id) {
        try {
            users.removeCard(id);
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
    public static class AddCardRequest {
        public String longNum;
        public String expires;
        public String ccv;
        public String userID;
    }
}
