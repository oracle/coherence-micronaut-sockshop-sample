/*
 * Copyright (c) 2021 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package io.micronaut.examples.sockshop.orders;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.tangosol.net.events.partition.cache.EntryEvent;

import io.micronaut.coherence.annotation.CoherenceEventListener;
import io.micronaut.coherence.annotation.Inserted;
import io.micronaut.coherence.annotation.MapName;
import io.micronaut.coherence.annotation.Updated;
import lombok.extern.java.Log;


import static io.micronaut.examples.sockshop.orders.Order.Status.PAID;
import static io.micronaut.examples.sockshop.orders.Order.Status.PAYMENT_FAILED;
import static io.micronaut.examples.sockshop.orders.Order.Status.SHIPPED;

/**
 * A more realistic implementation of {@link OrderProcessor} that stores
 * submitted order immediately and uses Coherence server-side events
 * to process payment and ship the order asynchronously, based on the
 * order status.
 */
@Log
@Singleton
public class EventDrivenOrderProcessor implements OrderProcessor {
    public EventDrivenOrderProcessor() {
    }

    /**
     * Order repository to use.
     */
    @Inject
    protected OrderRepository orders;

    /**
     * Shipping service client.
     */
    @Inject
    protected ShippingClient shippingService;

    /**
     * Payment service client.
     */
    @Inject
    protected PaymentClient paymentService;

    // --- OrderProcessor interface -----------------------------------------

    @Override
    public void processOrder(Order order) {
        saveOrder(order);
    }
    // ---- helpers ---------------------------------------------------------

    /**
     * Save specified order.
     *
     * @param order the order to save
     */
    protected void saveOrder(Order order) {
        orders.saveOrder(order);
        log.info("Order saved: " + order);
    }

    /**
     * Process payment and update order with payment details.
     *
     * @param order the order to process the payment for
     *
     * @throws PaymentDeclinedException if the payment was declined
     */
    protected void processPayment(Order order) {
        PaymentRequest paymentRequest = PaymentRequest.builder()
                .orderId(order.getOrderId())
                .customer(order.getCustomer())
                .address(order.getAddress())
                .card(order.getCard())
                .amount(order.getTotal())
                .build();

        log.info("Processing Payment: " + paymentRequest);
        Payment payment = paymentService.authorize(paymentRequest);
        if (payment == null) {
            payment = Payment.builder()
                    .authorised(false)
                    .message("Unable to parse authorization packet")
                    .build();
        }
        log.info("Payment processed: " + payment);

        order.setPayment(payment);
        if (!payment.isAuthorised()) {
            order.setStatus(PAYMENT_FAILED);
            throw new PaymentDeclinedException(payment.getMessage());
        }

        order.setStatus(PAID);
    }

    /**
     * Submits order for shipping and updates order with shipment details.
     *
     * @param order the order to ship
     */
    protected void shipOrder(Order order) {
        ShippingRequest shippingRequest = ShippingRequest.builder()
                .orderId(order.getOrderId())
                .customer(order.getCustomer())
                .address(order.getAddress())
                .itemCount(order.getItems().size())
                .build();

        log.info("Creating Shipment: " + shippingRequest);
        Shipment shipment = shippingService.ship(shippingRequest);
        log.info("Created Shipment: " + shipment);

        order.setShipment(shipment);
        order.setStatus(SHIPPED);
    }

    // ---- helper methods --------------------------------------------------

    /**
     * An exception that is thrown if the payment is declined.
     */
    public static class PaymentDeclinedException extends OrderException {
        public PaymentDeclinedException(String s) {
            super(s);
        }
    }

    @CoherenceEventListener
    public void onOrderCreated(@Inserted @Updated @MapName("orders") EntryEvent<String, Order> event) {
        Order order = event.getValue();
        switch (order.getStatus()) {
        case CREATED:
            try {
                processPayment(order);
            }
            finally {
                saveOrder(order);
            }
            break;

        case PAID:
            try {
                shipOrder(order);
            }
            finally {
                saveOrder(order);
            }
            break;

        default:
            // do nothing, order is in a terminal state already
        }
    }
}
