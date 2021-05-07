package io.micronaut.examples.sockshop.users.converters;

import io.micronaut.core.convert.ConversionContext;
import io.micronaut.core.convert.TypeConverter;

import io.micronaut.examples.sockshop.users.AddressId;

import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class StringToAddressIdConverter implements TypeConverter<String, AddressId> {
    @Override
    public Optional<AddressId> convert(String input, Class<AddressId> targetType, ConversionContext context) {
        if (input != null) {
            try {
                return Optional.of(new AddressId(input));
            } catch (IllegalArgumentException e) {
                context.reject(e);
            }
        }
        return Optional.empty();
    }
}
