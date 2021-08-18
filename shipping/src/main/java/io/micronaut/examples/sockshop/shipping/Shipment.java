/*
 * Copyright (c) 2021 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package io.micronaut.examples.sockshop.shipping;

import java.io.Serializable;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Shipment information to send as a response to Order service.
 */
@Data
@NoArgsConstructor
@Schema(description = "Shipment information to send as a response to Order service")
@MappedEntity
public class Shipment implements Serializable {
    /**
     * Order identifier.
     */
    @Schema(description = "Order identifier")
    @Id
    private String orderId;

    /**
     * Shipping carrier.
     */
    @Schema(description = "Shipping carrier")
    private String carrier;

    /**
     * Tracking number.
     */
    @Schema(description = "racking number")
    private String trackingNumber;

    /**
     * Estimated delivery date.
     */
    @Schema(description = "Estimated delivery date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate deliveryDate;

    @Builder
    Shipment(String orderId, String carrier, String trackingNumber, LocalDate deliveryDate) {
        this.orderId = orderId;
        this.carrier = carrier;
        this.trackingNumber = trackingNumber;
        this.deliveryDate = deliveryDate;
    }
}
