package com.ai.amrai.tool;

import java.time.OffsetDateTime;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Component
public class FunctionTool {

    @Tool(description = "Get the current date and time in the server's time zone, including the UTC offset")
    public String getCurrentTime() {
        return OffsetDateTime.now().toString();
    }

    @Tool(description = "Get Best City in World", returnDirect = true)
    public String getBestCity() {
        return "Best City is Salalah ;)";
    }
}
