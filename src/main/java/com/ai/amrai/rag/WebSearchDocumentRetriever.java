package com.ai.amrai.rag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.retrieval.search.DocumentRetriever;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class WebSearchDocumentRetriever implements DocumentRetriever {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSearchDocumentRetriever.class);
    private static final String TAVILY_BASE_URL = "https://api.tavily.com/search";
    private static final int DEFAULT_RESULT_LIMIT = 5;

    private final RestClient restClient;
    private final String apiKey;
    private final int resultLimit;

    private WebSearchDocumentRetriever(RestClient.Builder restClientBuilder, String apiKey, int resultLimit) {
        Assert.notNull(restClientBuilder, "RestClient.Builder is required");
        Assert.isTrue(resultLimit >= 1 && resultLimit <= 20, "maxResults must be between 1 and 20");
        this.restClient = restClientBuilder.clone().baseUrl(TAVILY_BASE_URL).build();
        this.apiKey = apiKey;
        this.resultLimit = resultLimit;
    }

    @Override
    public List<Document> retrieve(Query query) {
        Assert.notNull(query, "Query is required");
        Assert.state(StringUtils.hasText(apiKey),
                "Set TAVILY_API_KEY or tavily.api-key before using web search");

        TavilyResponsePayload response = restClient.post()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new TavilyRequestPayload(query.text(), "advanced", resultLimit))
                .retrieve()
                .body(TavilyResponsePayload.class);

        if (response == null || response.results() == null) {
            return List.of();
        }

        List<Document> documents = new ArrayList<>();
        for (TavilyResponsePayload.Hit hit : response.results()) {
            if (hit == null || !StringUtils.hasText(hit.content())) {
                continue;
            }
            Map<String, Object> metadata = new HashMap<>();
            if (hit.title() != null) {
                metadata.put("title", hit.title());
            }
            if (hit.url() != null) {
                metadata.put("url", hit.url());
            }
            if (hit.score() != null) {
                metadata.put("score", hit.score());
            }
            documents.add(Document.builder().text(hit.content()).metadata(metadata).build());
        }
        LOGGER.debug("Retrieved {} web search documents", documents.size());
        return documents;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private RestClient.Builder restClientBuilder = RestClient.builder();
        private String apiKey = System.getenv("TAVILY_API_KEY");
        private int resultLimit = DEFAULT_RESULT_LIMIT;

        private Builder() {
        }

        public Builder restClientBuilder(RestClient.Builder restClientBuilder) {
            this.restClientBuilder = restClientBuilder;
            return this;
        }

        public Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public Builder maxResults(int maxResults) {
            this.resultLimit = maxResults;
            return this;
        }

        public WebSearchDocumentRetriever build() {
            return new WebSearchDocumentRetriever(restClientBuilder, apiKey, resultLimit);
        }
    }
}
