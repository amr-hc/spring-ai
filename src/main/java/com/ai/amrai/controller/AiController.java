package com.ai.amrai.controller;

import com.ai.amrai.dto.country;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AiController {
    @Value("classpath:templates/user/user.st")
    private Resource userPrompt;

    private final ChatClient chatClient;
    public AiController(ChatClient chatClient) {
        this.chatClient = chatClient;
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

}
