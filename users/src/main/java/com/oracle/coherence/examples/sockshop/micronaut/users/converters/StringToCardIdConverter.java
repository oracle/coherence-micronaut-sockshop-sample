/*
 * Copyright (c) 2021 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package com.oracle.coherence.examples.sockshop.micronaut.users.converters;

import com.oracle.coherence.examples.sockshop.micronaut.users.CardId;
import io.micronaut.core.convert.ConversionContext;
import io.micronaut.core.convert.TypeConverter;

import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class StringToCardIdConverter implements TypeConverter<String, CardId> {
    @Override
    public Optional<CardId> convert(String input, Class<CardId> targetType, ConversionContext context) {
        if (input != null) {
            try {
                return Optional.of(new CardId(input));
            } catch (IllegalArgumentException e) {
                context.reject(e);
            }
        }
        return Optional.empty();
    }
}
