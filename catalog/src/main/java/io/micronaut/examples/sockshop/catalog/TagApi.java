/*
 * Copyright (c) 2021 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * http://oss.oracle.com/licenses/upl.
 */

package io.micronaut.examples.sockshop.catalog;

import io.micronaut.http.annotation.Get;
import io.swagger.v3.oas.annotations.Operation;

/**
 * REST API for {@code /catalog} service.
 */
public interface TagApi {
    @Get
    @Operation(summary = "Return all tags")
    TagsResource.Tags getTags();
}
