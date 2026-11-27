package com.ai.amrai.config;

import io.qdrant.client.QdrantClient;
import org.springframework.ai.chat.cache.semantic.SemanticCache;
import org.springframework.ai.chat.cache.semantic.SemanticCacheAdvisor;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.qdrant.QdrantVectorStore;
import org.springframework.ai.vectorstore.redis.cache.semantic.DefaultSemanticCache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SemanticCacheConfig {

    @Bean
    public SemanticCache semanticCache(QdrantClient qdrantClient, EmbeddingModel embeddingModel,
            @Value("${app.semantic-cache.collection-name:amr-semantic-cache}") String collectionName,
            @Value("${app.semantic-cache.similarity-threshold:0.8}") double similarityThreshold) throws Exception {
        // Keep cached answers separate from the documents used by RAG.
        // This store stays local to the cache so Qdrant's RAG store is still auto-configured.
        QdrantVectorStore cacheStore = QdrantVectorStore.builder(qdrantClient, embeddingModel)
                .collectionName(collectionName)
                .initializeSchema(true)
                .build();
        cacheStore.afterPropertiesSet();

        return DefaultSemanticCache.builder()
                .vectorStore(cacheStore)
                .similarityThreshold(similarityThreshold)
                .build();
    }

    @Bean
    public SemanticCacheAdvisor semanticCacheAdvisor(SemanticCache semanticCache) {
        return SemanticCacheAdvisor.builder().cache(semanticCache).build();
    }
}
