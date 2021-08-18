/*
 * Copyright (c) 2021 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package io.micronaut.examples.sockshop.users;

import com.oracle.coherence.micrometer.CoherenceMicrometerMetrics;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.micronaut.context.event.BeanCreatedEvent;
import io.micronaut.context.event.BeanCreatedEventListener;

import javax.inject.Singleton;

@Singleton
public class Metrics implements BeanCreatedEventListener<PrometheusMeterRegistry> {
    @Override
    public PrometheusMeterRegistry onCreated(BeanCreatedEvent<PrometheusMeterRegistry> event) {
        PrometheusMeterRegistry registry = event.getBean();
        CoherenceMicrometerMetrics.INSTANCE.bindTo(registry);
        return registry;
    }
}
