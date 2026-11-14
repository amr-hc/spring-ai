package com.ai.amrai.rag;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TavilyResponsePayload(List<Hit> results) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Hit(String title, String url, String content, Double score) {
    }
}
