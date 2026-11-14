package com.ai.amrai.rag;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TavilyRequestPayload(
        String query,
        @JsonProperty("search_depth") String searchDepth,
        @JsonProperty("max_results") int maxResults) {
}
