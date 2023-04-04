/*
 * Copyright (c) 2021, 2023 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package com.oracle.coherence.examples.sockshop.micronaut.catalog;

import io.micronaut.http.annotation.Controller;
import io.micronaut.tracing.annotation.NewSpan;

import jakarta.inject.Inject;
import java.util.Set;

/**
 * Implementation of the Catalog Service {@code /tags} API.
 */
@Controller("/tags")
public class TagsResource implements TagApi {

    @Inject
    private CatalogRepository catalog;

    @Override
    @NewSpan
    public Tags getTags() {
        return new Tags(catalog.getTags());
    }

    public static class Tags {
        public Set<String> tags;
        public Object err;

        Tags(Set<String> tags) {
            this.tags = tags;
        }
    }
}
