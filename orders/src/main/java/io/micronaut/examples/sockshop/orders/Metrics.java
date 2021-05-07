package io.micronaut.examples.sockshop.orders;

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
