package com.ai.amrai.rag;

import jakarta.annotation.PostConstruct;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RandomDataLoader {

    private final VectorStore vectorStore;
    public RandomDataLoader(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @PostConstruct
    public void loadDataToDatabase() {
        List<String> list = List.of(
                "Germany trip planning: A suggested seven-night trip includes three nights in Berlin, two in Hamburg, and two in Munich. Allow time for train journeys.",
                "Berlin sightseeing: Explore the Brandenburg Gate, Museum Island, the government district, and Kreuzberg. Check attraction opening hours and booking requirements.",
                "Munich sightseeing: Visit Marienplatz, the cathedral, and the English Garden. Use public transport to reach the Allianz Arena and check visiting times.",
                "Hamburg sightseeing: Explore Speicherstadt, the waterfront, the Elbphilharmonie, and Miniatur Wunderland. Check whether tours and attractions need advance booking.",
                "Train travel in Germany: Use DB Navigator to find routes, book tickets, and check platform updates. Review connection times, fare conditions, and seat reservations.",
                "Local transport in Germany: The Deutschland-Ticket covers participating local and regional transport. It generally excludes ICE, IC, EC, and FlixTrain; check route exceptions and subscription conditions.",
                "Accommodation in Germany: Compare total hotel costs, breakfast, cancellation terms, and check-in hours. Budget separately for transport, meals, and attractions.",
                "Germany travel preparation: Save booking details offline. Pack walking shoes, clothing layers, and rain protection, and check the forecast and baggage allowances."
        );
        

        List<Document> documents = list.stream().map(Document::new).toList();
        vectorStore.add(documents);
    }


}
