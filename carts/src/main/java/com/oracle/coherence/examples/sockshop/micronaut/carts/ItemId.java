/*
 * Copyright (c) 2021 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package com.oracle.coherence.examples.sockshop.micronaut.carts;

import lombok.Data;

import java.io.Serializable;

/**
 * Composite key for the {@link Item class} when using JPA.
 */
@Data
public class ItemId implements Serializable {
    /**
     * The item identifier.
     */
    private String itemId;

    /**
     * The ID of the cart this item belongs to.
     */
    private String cart;
}
