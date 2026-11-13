package com.ai.amrai.controller;

import com.ai.amrai.dto.country;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AiController {
    @Value("classpath:templates/user/user.st")
    private Resource userPrompt;

    @Value("classpath:templates/rag/rag.st")
    private Resource ragPrompt;

    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    public AiController(ChatClient chatClient, VectorStore vectorStore) {
        this.chatClient = chatClient;
        this.vectorStore = vectorStore;
    }

    @GetMapping
    public String chat(@RequestParam("message") String message,
                       @RequestParam("name") String name){
        return chatClient.prompt()
                .user(user -> user.text(userPrompt)
                        .param("name", name)
                        .param("message", message))
                .call().content();
    }

    @GetMapping("/stream")
    public Flux<String> message(@RequestParam("message") String message,
                                @RequestParam("username") String username
    ){
        return this.chatClient.prompt()
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, username))

                .user(message)
                .stream().content();
    }

    @GetMapping("/country")
    public List<country> country(@RequestParam("message") String message){
        return this.chatClient.prompt()
                .user(message)

                .call()
                .entity( new ParameterizedTypeReference<
                        List<country>
                        >() {}) ;
    }


    @GetMapping("/chat-rag")
    public Flux<String> chatRag(@RequestParam("message") String message,
                       @RequestParam("username") String username){
        SearchRequest searchRequest = SearchRequest.builder().query(message).topK(3).similarityThreshold(0.5).build();
        List<Document> similarsDocs = vectorStore.similaritySearch(searchRequest);
        String similarStrings = similarsDocs.stream().map(Document::getText)
                .collect(Collectors.joining(System.lineSeparator()));

         return this.chatClient.prompt()
                 .system(
                         promptSystemSpec -> promptSystemSpec.text(ragPrompt)
                                 .param("documents", similarStrings)
                 )

                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, username))
                .user(message)
                .stream().content();


         
    }

}
