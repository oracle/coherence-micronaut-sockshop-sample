/*
 * Copyright (c) 2021 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package com.oracle.coherence.examples.sockshop.micronaut.payment;

import java.io.Serializable;
import java.time.LocalDateTime;

import io.micronaut.data.annotation.Embeddable;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Composite JPA key for {@link Authorization} class.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Composite JPA key for Authorization class")
@Embeddable
public class AuthorizationId implements Serializable {
    /**
     * Order identifier.
     */
    @Schema(description = "Order identifier")
    private String orderId;

    /**
     * Time when this payment authorization was created.
     */
    @Schema(description = "Time when this payment authorization was created")
    private LocalDateTime time;
}
