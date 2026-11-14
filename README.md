## Web-search RAG

The lecture's web-search flow is available at `/api/web-search-chat`.
It sends an advanced search to Tavily (five results by default), converts the
results into Spring AI documents, and supplies them to the model through
`RetrievalAugmentationAdvisor`. Chat history is selected by `username`.

Set `TAVILY_API_KEY` in your IDE's run environment, or set `tavily.api-key` in
`application-local.properties` at the project root. That local file is imported
automatically and ignored by Git. The supplied key has been saved there locally.
Keep the existing OpenAI configuration and Qdrant setup available when running
the full application.

Restart the application, then try:

```powershell
curl.exe -G "http://localhost:8099/api/web-search-chat" --data-urlencode "message=What are the latest Java releases?" --data-urlencode "username=amr"
```

To change the result count, set `tavily.max-results` (1–20) in your configuration;
the default is 5. The existing `/api/chat-rag` endpoint continues to use Qdrant.

## Accessing the H2 Console

1. Reload the Maven project in your IDE.
2. Restart the application.
3. Open [http://localhost:8099/h2-console](http://localhost:8099/h2-console) in your browser.
4. Enter the following connection details:

   | Field | Value |
   | --- | --- |
   | Driver Class | `org.h2.Driver` |
   | JDBC URL | `jdbc:h2:file:./data/chat-memory` |
   | User Name | `sa` |
   | Password | Leave empty |

5. Click **Connect**.

To view the stored chat messages, run:

```sql
SELECT * FROM SPRING_AI_CHAT_MEMORY;
```
