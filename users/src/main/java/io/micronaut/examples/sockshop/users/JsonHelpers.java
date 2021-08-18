/*
 * Copyright (c) 2021 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package io.micronaut.examples.sockshop.users;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Map;


import static java.util.Collections.singletonMap;

abstract class JsonHelpers {
    private static final ObjectMapper J = new ObjectMapper();
    static {
       J.setDefaultPropertyInclusion(JsonInclude.Include.ALWAYS);
    }

    static ObjectNode obj() {
        return J.createObjectNode();
    }

    static Map<String, Object> embed(String name, Object value) {
        return singletonMap("_embedded", singletonMap(name, value));
    }
}
