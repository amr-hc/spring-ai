package com.ai.amrai.config;

import org.springframework.ai.chat.cache.semantic.SemanticCache;
import org.springframework.ai.chat.cache.semantic.SemanticCacheAdvisor;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.redis.cache.semantic.DefaultSemanticCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.RedisClient;

@Configuration
public class SemanticCacheConfig {

    @Bean
    public RedisClient redisClient() {
        return RedisClient.builder()
                .hostAndPort("localhost", 6379)
                .build();
    }

    @Bean
    public SemanticCache semanticCache(RedisClient redisClient, EmbeddingModel embeddingModel) {
        return DefaultSemanticCache.builder()
                .jedisClient(redisClient)
                .embeddingModel(embeddingModel)
                .similarityThreshold(0.8)
                .indexName("amr-semantic-cache")
                .prefix("cache:")
                .build();
    }

    @Bean
    public SemanticCacheAdvisor semanticCacheAdvisor(SemanticCache semanticCache) {
        return SemanticCacheAdvisor.builder().cache(semanticCache).build();
    }
}
