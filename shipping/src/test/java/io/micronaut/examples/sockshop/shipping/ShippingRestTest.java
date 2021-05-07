/*
 * Copyright (c) 2020, 2021 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * http://oss.oracle.com/licenses/upl.
 */

package io.micronaut.examples.sockshop.shipping;

import io.micronaut.context.ApplicationContext;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Integration tests for {@link ShippingResource} gRPC API.
 */
@MicronautTest
public class ShippingRestTest {
    @Inject
    ApplicationContext context;

    @Inject
    ShippingClient client;

    private TestShipmentRepository shipments;

    @BeforeEach
    void setup() {
        shipments = context.getBean(TestShipmentRepository.class);
        shipments.clear();
    }

    @Test
    void testFedEx() throws Exception {
        ShippingRequest request = TestDataFactory.shippingRequest("A123", 1);
        Shipment shipment = client.ship(request);
        assertThat(shipment.getCarrier(), is("FEDEX"));
        assertThat(shipment.getDeliveryDate(), is(LocalDate.now().plusDays(1)));
    }

    @Test
    void testUPS() {
        Shipment shipment = client.ship(TestDataFactory.shippingRequest("A456", 3));
        assertThat(shipment.getCarrier(), is("UPS"));
        assertThat(shipment.getDeliveryDate(), is(LocalDate.now().plusDays(3)));
    }

    @Test
    void testUSPS() {
        Shipment shipment = client.ship(TestDataFactory.shippingRequest("A789", 10));
        assertThat(shipment.getCarrier(), is("USPS"));
        assertThat(shipment.getDeliveryDate(), is(LocalDate.now().plusDays(5)));
    }

    @Test
    void testGetShipmentByOrder() {
        LocalDate deliveryDate = LocalDate.now().plusDays(2);
        shipments.saveShipment(TestDataFactory.shipment("A123", "UPS", "1Z999AA10123456784", deliveryDate));

        Shipment shipment = client.getShipmentByOrderId("A123");
        assertThat(shipment.getOrderId(), is("A123"));
        assertThat(shipment.getCarrier(), is("UPS"));
        assertThat(shipment.getTrackingNumber(), is("1Z999AA10123456784"));
        assertThat(shipment.getDeliveryDate(), is(deliveryDate));

//        assertThat(client.getShipmentByOrderId("B456"), nullValue());
    }
}
