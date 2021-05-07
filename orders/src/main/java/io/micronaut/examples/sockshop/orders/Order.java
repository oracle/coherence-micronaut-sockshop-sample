/*
 * Copyright (c) 2020, 2021 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * http://oss.oracle.com/licenses/upl.
 */

package io.micronaut.examples.sockshop.orders;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;


/**
 * Order information.
 */
@Data
@NoArgsConstructor
@MappedEntity
public class Order implements Serializable, Comparable<Order> {
    /**
     * Order identifier.
     */
    @JsonProperty("id")
    @Schema(description = "Order identifier")
    @Id
    private String orderId;

    /**
     * Customer information.
     */
    @Schema(description = "Customer information")
    private Customer customer;

    /**
     * Billing/shipping address.
     */
    @Schema(description = "Billing/shipping address")
    private Address address;

    /**
     * Payment card details.
     */
    @Schema(description = "Payment card details")
    private Card card;

    /**
     * Order date and time.
     */
    @Schema(description = "Order date and time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime date;

    /**
     * Order total.
     */
    @Schema(description = "Order total")
    private float total;

    /**
     * Order items.
     */
    @Schema(description = "Order items")
    private Collection<Item> items = new ArrayList<>();

    /**
     * Payment authorization.
     */
    @Schema(description = "Payment authorization")
    private Payment payment;

    /**
     * Shipment details.
     */
    @Schema(description = "Shipment details")
    private Shipment shipment;

    /**
     * Order status.
     */
    @Schema(description = "Order status")
    private Status status;

    @Builder
    public Order(Customer customer,
                 Address address,
                 Card card,
                 Collection<Item> items) {
        this.orderId  = UUID.randomUUID().toString().substring(0, 8);
        this.date     = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        this.customer = customer;
        this.address  = address;
        this.card     = card;
        this.status   = Status.CREATED;
        this.setItems(items);
    }

    /**
     * Set order items.
     *
     * @param items order items
     */
    public void setItems(Collection<Item> items) {
        float total = 0.0f;
        for (Item item : items) {
            total += item.getQuantity() * item.getUnitPrice();
            item.setOrder(this);
        }
        this.total = total;
        this.items = items;
    }

    /**
     * Order links.
     *
     * @return order links
     */
    @JsonProperty("_links")
    public Links getLinks() {
        return Links.order(orderId);
    }

    @Override
    public int compareTo(Order o) {
        return date.compareTo(o.date) * -1;
    }

    /**
     * Order status enumeration.
     */
    public enum Status {
        /**
         * Order was created.
         */
        CREATED,

        /**
         * Payment was processed successfully.
         */
        PAID,

        /**
         * Order was shipped.
         */
        SHIPPED,

        /**
         * Payment was not authorized.
         */
        PAYMENT_FAILED,

        /**
         * Order shipment failed.
         */
        SHIPMENT_FAILED
    }
}
