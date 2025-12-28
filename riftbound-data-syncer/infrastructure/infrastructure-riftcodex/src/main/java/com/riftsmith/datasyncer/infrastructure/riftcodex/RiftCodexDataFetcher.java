package com.riftsmith.datasyncer.infrastructure.riftcodex;

import com.riftsmith.datasyncer.domain.datafetch.DataFetcher;
import com.riftsmith.datasyncer.domain.riftbound.CardEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Component
public class RiftCodexDataFetcher implements DataFetcher {

    private static final Logger log = LoggerFactory.getLogger(RiftCodexDataFetcher.class);
    private static final String BASE_URL = "https://api.riftcodex.com/cards";
    
    private final RestClient restClient;

    public RiftCodexDataFetcher(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.baseUrl(BASE_URL).build();
    }

    @Override
    public Stream<CardEntity> fetchData() {
        log.info("Starting card fetch from RiftCodex...");
        List<CardEntity> allCards = new ArrayList<>();

        // 1. Fetch the first page to get data and metadata (total pages)
        RiftCodexResponse firstPage = fetchPage(1);
        if (firstPage == null) {
            return Stream.empty();
        }

        allCards.addAll(mapToEntities(firstPage.items()));

        // 2. Iterate through remaining pages
        int totalPages = firstPage.pages();
        for (int i = 2; i <= totalPages; i++) {
            RiftCodexResponse nextPage = fetchPage(i);
            if (nextPage != null) {
                allCards.addAll(mapToEntities(nextPage.items()));
            }
        }

        log.info("Successfully fetched {} cards across {} pages.", allCards.size(), totalPages);
        
        // Return a stream of the collected entities
        return allCards.stream();
    }

    private RiftCodexResponse fetchPage(int pageNumber) {
        try {
            return restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam("page", pageNumber)
                            .build())
                    .retrieve()
                    .body(RiftCodexResponse.class);
        } catch (Exception e) {
            log.error("Failed to fetch page {}", pageNumber, e);
            return null;
        }
    }

    private List<CardEntity> mapToEntities(List<RiftCardDto> dtos) {
        if (dtos == null) return List.of();
        
        return dtos.stream()
                .map(dto -> CardEntity.builder()
                        .id(dto.id())
                        .name(dto.name())
                        .build())
                .toList();
    }

    // ==========================================
    // Internal DTOs to map the incoming JSON
    // ==========================================

    // Maps the root JSON response
    record RiftCodexResponse(
        List<RiftCardDto> items,
        int total,
        int page,
        int size,
        int pages
    ) {}

    // Maps the individual card items in the JSON array
    // We only map 'id' and 'name' for now, but you can add more fields here later
    record RiftCardDto(
        UUID id,
        String name
        // You can map other JSON fields here later, e.g.:
        // @JsonProperty("riftbound_id") String riftboundId
    ) {}
}