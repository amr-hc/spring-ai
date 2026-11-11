
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
