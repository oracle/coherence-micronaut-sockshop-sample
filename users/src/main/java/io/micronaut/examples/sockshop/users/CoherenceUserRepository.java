/*
 * Copyright (c) 2020, 2021 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * http://oss.oracle.com/licenses/upl.
 */

package io.micronaut.examples.sockshop.users;

import io.micronaut.coherence.data.AbstractCoherenceRepository;
import io.micronaut.coherence.data.annotation.CoherenceRepository;

import javax.annotation.PostConstruct;

import java.util.Collection;

/**
 * An implementation of {@link UserRepository}
 * that that uses Coherence as a backend data store.
 */
@CoherenceRepository("users")
//@Traced
public abstract class CoherenceUserRepository
        extends AbstractCoherenceRepository<User, String>
        implements UserRepository {

    @Override
    public Address getAddress(AddressId id) {
        return getOrCreate(id.getUser()).getAddress(id.getAddressId());
    }

    @Override
    public AddressId addAddress(String userID, Address address) {
        return update(userID, User::addAddress, address, User::new).getId();
    }

    @Override
    public void removeAddress(AddressId id) {
        String userID = id.getUser();
        update(userID, User::removeAddress, id.getAddressId(), User::new);
    }

    @Override
    public CardId addCard(String userID, Card card) {
        return update(userID, User::addCard, card, User::new).getId();
    }

    @Override
    public Card getCard(CardId id) {
        return getOrCreate(id.getUser()).getCard(id.getCardId());
    }

    @Override
    public void removeCard(CardId id) {
        String userId = id.getUser();
        update(userId, User::removeCard, id.getCardId(), User::new);
    }

    @Override
    public Collection<? extends User> getAllUsers() {
        return getAll();
    }

    @Override
    public User getOrCreate(String id) {
        return getMap().getOrDefault(id, new User(id));
    }

    @Override
    public User getUser(String id) {
        return get(id);
    }

    @Override
    public User removeUser(String id) {
        return removeById(id, true);
    }

    @Override
    public boolean authenticate(String username, String password) {
        return getMap().invoke(username, entry -> {
            User u = entry.getValue(new User(entry.getKey()));
            return u.authenticate(password);
        });
    }

    @Override
    public User register(User user) {
        return getMap().putIfAbsent(user.getUsername(), user);
    }
}
