/*
 * Copyright (c) 2021 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * http://oss.oracle.com/licenses/upl.
 */

package io.micronaut.examples.sockshop.catalog;

import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@SuppressWarnings("unused")
@Data
@MappedEntity
public class Sock implements Serializable {
    /**
     * Product identifier.
     */
    @Schema(description = "Product identifier")
    @Id
    private String id;

    /**
     * Product name.
     */
    @Schema(description = "Product name")
    private String name;

    /**
     * Product description.
     */
    @Schema(description = "Product description")
    private String description;

    /**
     * A list of product image URLs.
     */
    @Schema(description = "A list of product image URLs")
    private List<String> imageUrl;

    /**
     * Product price.
     */
    @Schema(description = "Product price")
    private float price;

    /**
     * Product count.
     */
    @Schema(description = "Product count")
    private int count;

    /**
     * Product tags.
     */
    @Schema(description = "Product tags")
    private Set<String> tag;
}
