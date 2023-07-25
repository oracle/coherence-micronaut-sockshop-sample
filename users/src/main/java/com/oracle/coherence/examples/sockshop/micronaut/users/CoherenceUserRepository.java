/*
 * Copyright (c) 2021, 2023 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package com.oracle.coherence.examples.sockshop.micronaut.users;

import io.micronaut.coherence.data.AbstractCoherenceRepository;
import io.micronaut.coherence.data.annotation.CoherenceRepository;

import io.micronaut.tracing.annotation.NewSpan;
import java.util.Collection;

/**
 * An implementation of {@link UserRepository}
 * that that uses Coherence as a backend data store.
 */
@CoherenceRepository("users")
public abstract class CoherenceUserRepository
        extends AbstractCoherenceRepository<User, String>
        implements UserRepository {

    @Override
    @NewSpan("getAddress")
    public Address getAddress(AddressId id) {
        return getOrCreate(id.getUser()).getAddress(id.getAddressId());
    }

    @Override
    @NewSpan("addAddress")
    public AddressId addAddress(String userID, Address address) {
        return update(userID, User::addAddress, address, User::new).getId();
    }

    @Override
    @NewSpan("removeAddress")
    public void removeAddress(AddressId id) {
        String userID = id.getUser();
        update(userID, User::removeAddress, id.getAddressId(), User::new);
    }

    @Override
    @NewSpan("addCard")
    public CardId addCard(String userID, Card card) {
        return update(userID, User::addCard, card, User::new).getId();
    }

    @Override
    @NewSpan("getCard")
    public Card getCard(CardId id) {
        return getOrCreate(id.getUser()).getCard(id.getCardId());
    }

    @Override
    @NewSpan("removeCard")
    public void removeCard(CardId id) {
        String userId = id.getUser();
        update(userId, User::removeCard, id.getCardId(), User::new);
    }

    @Override
    @NewSpan("getAllUsers")
    public Collection<? extends User> getAllUsers() {
        return getAll();
    }

    @Override
    @NewSpan("getOrCreate")
    public User getOrCreate(String id) {
        return getMap().getOrDefault(id, new User(id));
    }

    @Override
    @NewSpan("getUsers")
    public User getUser(String id) {
        return get(id);
    }

    @Override
    @NewSpan("removeUser")
    public User removeUser(String id) {
        return removeById(id, true);
    }

    @Override
    @NewSpan("authenticate")
    public boolean authenticate(String username, String password) {
        return getMap().invoke(username, entry -> {
            User u = entry.getValue(new User(entry.getKey()));
            return u.authenticate(password);
        });
    }

    @Override
    @NewSpan("registerUser")
    public User register(User user) {
        return getMap().putIfAbsent(user.getUsername(), user);
    }
}
