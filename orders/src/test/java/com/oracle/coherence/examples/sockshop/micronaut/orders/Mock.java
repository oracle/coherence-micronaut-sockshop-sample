/*
 * Copyright (c) 2021, 2023 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package com.oracle.coherence.examples.sockshop.micronaut.orders;

import java.lang.annotation.Annotation;
import jakarta.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

@Qualifier
@Retention(RUNTIME)
public @interface Mock {
    public static Mock INSTANCE = new Mock(){
        @Override
        public Class<? extends Annotation> annotationType() {
            return Mock.class;
        }
    };
}
