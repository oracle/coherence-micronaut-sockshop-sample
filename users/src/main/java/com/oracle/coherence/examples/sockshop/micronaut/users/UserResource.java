/*
 * Copyright (c) 2021 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package com.oracle.coherence.examples.sockshop.micronaut.users;

import java.nio.charset.StandardCharsets;

import java.util.Base64;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.node.ObjectNode;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Controller;
import io.micronaut.tracing.annotation.NewSpan;

/*
 * DISCLAIMER: This is a very naive and insecure implementation of user
 * authentication. It should not be used as an example/blueprint to
 * follow when implementing authentication in custom services. The code
 * below exists purely to provide compatibility with the original front
 * end written for SockShop
 */
@Controller
public class UserResource implements UserApi {
    static final String HEADER_AUTHENTICATION_REQUIRED = "WWW-Authenticate";
    static final String HEADER_AUTHENTICATION = "Authorization";
    static final String BASIC_PREFIX = "Basic ";

    private static final Logger LOGGER = Logger.getLogger(UserResource.class.getName());
    private static final Pattern CREDENTIAL_PATTERN = Pattern.compile("(.*):(.*)");

    @Inject
    private UserRepository users;

    @Override
    @NewSpan
    public HttpResponse login(String auth) {
        if (!auth.startsWith(BASIC_PREFIX)) {
            return fail("Basic authentication header is missing");
        }
        String  b64 = auth.substring(BASIC_PREFIX.length());
        String  usernameAndPassword = new String(Base64.getDecoder().decode(b64), StandardCharsets.UTF_8);
        Matcher matcher = CREDENTIAL_PATTERN.matcher(usernameAndPassword);
        if (!matcher.matches()) {
            LOGGER.finest(() -> "Basic authentication header with invalid content: " + usernameAndPassword);
            return fail("Basic authentication header with invalid content");
        }

        final String username = matcher.group(1);
        final String password = matcher.group(2);

        boolean fAuth = users.authenticate(username, password);

        if (fAuth) {
            ObjectNode entity = JsonHelpers.obj().set("user", JsonHelpers.obj().put("id", username));
            return HttpResponse.ok(entity);
        }
        else {
            return fail("Invalid username or password");
        }
    }

    @Override
    @NewSpan
    public HttpResponse register(User user) {
        User prev = users.register(user);
        if (prev != null) {
            return HttpResponse.status(HttpStatus.CONFLICT).body("User with that ID already exists");
        }
        return HttpResponse.ok(JsonHelpers.obj().put("id", user.getUsername()));
    }

    // ---- helpers ---------------------------------------------------------

    private HttpResponse fail(String message) {
        return HttpResponse
                .status(HttpStatus.UNAUTHORIZED)
                .header(HEADER_AUTHENTICATION_REQUIRED, "Basic realm=\"sockshop\"")
                .body(message);
    }
}
