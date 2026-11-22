package com.ai.amrai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.preretrieval.query.transformation.TranslationQueryTransformer;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class Configration {
    @Value("classpath:templates/system/system.st")
    private Resource systemPrompt;

    @Bean
    public TranslationQueryTransformer englishQueryTransformer(ChatClient.Builder chatClientBuilder) {
        return TranslationQueryTransformer.builder()
                .chatClientBuilder(chatClientBuilder.clone())
                .targetLanguage("english")
                .build();
    }

    @Bean
    public RetrievalAugmentationAdvisor retrievalAugmentationAdvisor(VectorStore vectorStore,
                                                                    TranslationQueryTransformer englishQueryTransformer) {
        return RetrievalAugmentationAdvisor.builder()
                .queryTransformers(englishQueryTransformer)
                .documentRetriever(VectorStoreDocumentRetriever.builder()
                        .vectorStore(vectorStore)
                        .topK(3)
                        .similarityThreshold(0.5)
                        .build())
                .build();
    }

    @Bean
    public ChatClient getChatClient(
            ChatClient.Builder chatClientBuilder, ChatMemory chatMemory) {
        return chatClientBuilder.clone()
                .defaultSystem(systemPrompt)
                .defaultAdvisors(
                    new SimpleLoggerAdvisor(),
                    MessageChatMemoryAdvisor.builder(chatMemory).build()
                )
                .build();
    }

    @Bean
    public ChatMemory chatMemory(ChatMemoryRepository repository) {
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(repository)
                .maxMessages(500)
                .build();
    }


}
