package io.micronaut.examples.sockshop.users.converters;

import io.micronaut.core.convert.ConversionContext;
import io.micronaut.core.convert.TypeConverter;

import io.micronaut.examples.sockshop.users.CardId;

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
