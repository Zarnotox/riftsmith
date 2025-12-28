package com.riftsmith.datasyncer.domain.datafetch;

import com.riftsmith.datasyncer.domain.riftbound.CardEntity;

import java.util.stream.Stream;

public interface DataFetcher {
    Stream<CardEntity> fetchData();
}
