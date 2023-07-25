/*
 * Copyright (c) 2021, 2023 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package com.oracle.coherence.examples.sockshop.micronaut.catalog;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import com.tangosol.util.Filter;
import com.tangosol.util.Filters;

import com.tangosol.util.filter.AlwaysFilter;
import com.tangosol.util.filter.LimitFilter;

import com.tangosol.util.function.Remote;

import io.micronaut.coherence.data.AbstractCoherenceRepository;
import io.micronaut.coherence.data.annotation.CoherenceRepository;

import io.micronaut.tracing.annotation.NewSpan;
import jakarta.annotation.PostConstruct;

import java.io.IOException;
import java.io.InputStream;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * An implementation of {@link CatalogRepository}
 * that that uses Coherence as a backend data store.
 */
@CoherenceRepository("socks")
public abstract class CoherenceCatalogRepository
        extends AbstractCoherenceRepository<Sock, String>
        implements CatalogRepository {
    private static final Remote.Comparator<Sock> PRICE_COMPARATOR = Remote.comparator(Sock::getPrice);
    private static final Remote.Comparator<Sock> NAME_COMPARATOR  = Remote.comparator(Sock::getName);
    private static final Logger LOGGER = Logger.getLogger(CoherenceCatalogRepository.class.getName());

    @Override
    @NewSpan("getSocks")
    public Collection<? extends Sock> getSocks(String tags, String order, int pageNum, int pageSize) {
        Remote.Comparator<Sock> comparator = "price".equals(order)
                ? PRICE_COMPARATOR
                : "name".equals(order)
                        ? NAME_COMPARATOR
                        : null;

        LimitFilter<Sock> filter = new LimitFilter<>(createTagsFilter(tags), pageSize);
        filter.setPage(pageNum -1);
        return getAllOrderedBy(filter, comparator);
    }

    @Override
    @NewSpan("getSock")
    public Sock getSock(String sockId) {
        return get(sockId);
    }

    @Override
    @NewSpan("getSockCount")
    public long getSockCount(String tags) {
        return count(createTagsFilter(tags));
    }

    @Override
    @NewSpan("getTags")
    public Set<String> getTags() {
        return getAll().stream()
                .flatMap(sock -> sock.getTag().stream())
                .collect(Collectors.toSet());
    }

    /**
     * Initialize this repository.
     */
    @PostConstruct
    void init() {
        loadData();
    }

    /**
     * Load test data into this repository.
     */
    public void loadData() {
        if (getMap().isEmpty()) {
            loadSocksFromJson().forEach(this::save);
        }
    }
    /**
     * Load socks from a JSON file.
     *
     * @param <T>     the type to load data as
     * @return a list of socks
     */
    protected <T extends Sock> List<T> loadSocksFromJson() {
        ObjectMapper mapper = new ObjectMapper();

        CollectionType javaType = mapper.getTypeFactory()
                .constructCollectionType(List.class, Sock.class);
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("data.json")) {
            return mapper.readValue(in, javaType);
        } catch (IOException e) {
            LOGGER.warning(e.getMessage());
            return Collections.emptyList();
        }
    }

    private Filter<Sock> createTagsFilter(String tags) {
        Filter<Sock> filter = AlwaysFilter.INSTANCE();
        if (tags != null && !"".equals(tags)) {
            String[] aTags = tags.split(",");
            filter = Filters.containsAny(Sock::getTag, aTags);
        }
        return filter;
    }
}