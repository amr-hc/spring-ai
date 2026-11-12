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
                "Germany trip planning: One suggested seven-night itinerary is three nights in Berlin, two in Hamburg, and two in Munich. This is an example itinerary, not an official travel package. Allow time for train journeys and hotel check-in, and compare arriving in one city and departing from another to reduce backtracking.",
                "Berlin sightseeing: Germany's capital offers historical landmarks, museums, and diverse neighborhoods. Visitors can explore the Brandenburg Gate, the government district, Museum Island, and Kreuzberg. A practical sightseeing plan groups nearby places into the same day and checks each attraction's current opening hours and reservation requirements before visiting.",
                "Munich sightseeing: Munich offers the old town around Marienplatz, its cathedral, and the English Garden. Public transport also connects visitors with the Allianz Arena. For a relaxed visit, separate old-town sightseeing from park time, and check the route and available visiting times before planning a stadium visit.",
                "Hamburg sightseeing: Hamburg offers maritime attractions including the Landungsbruecken waterfront, the Speicherstadt warehouse district, the Elbphilharmonie, and Miniatur Wunderland. The Binnenalster lake provides another walking area. Plan indoor attractions and waterfront walks separately, and confirm whether a specific exhibition, concert, or tour requires advance booking.",
                "Train journey planning in Germany: DB Navigator provides journey searches, ticket booking, and travel information. Check the exact departure and arrival stations, train number, interchange time, and platform updates. Review the fare conditions before purchasing, and check whether a seat reservation is included or must be booked separately for the selected service.",
                "Local transport in Germany: The Deutschland-Ticket covers participating local and regional services such as RB, RE, S-Bahn, buses, trams, and underground trains. It generally excludes ICE, IC, EC, and FlixTrain, although specific route exceptions exist. Check the exact service, current subscription price, and cancellation conditions before buying; a long-distance journey may require a separate ticket.",
                "Accommodation and budgeting in Germany: Compare hotels, hostels, and apartments by the total stay cost rather than the advertised nightly rate. Check breakfast, cancellation terms, check-in hours, luggage storage, and any additional charges. Build separate budget categories for accommodation, intercity transport, local transport, meals, and attractions; actual costs depend on dates and booking choices.",
                "Preparing for a Germany holiday: Save hotel addresses, booking references, and important contact details offline. Pack comfortable walking shoes, clothing layers, and rain protection, then adjust the packing list to the destination and forecast. Check baggage allowances with each transport provider and leave spare time between arrival, onward travel, and activities with fixed entry times."
        );
        

        List<Document> documents = list.stream().map(Document::new).toList();
        vectorStore.add(documents);
    }


}
