import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.Scanner;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {
    private static final String API_URL = "https://api.anthropic.com/v1/complete";
    private static final String API_KEY_ENV = "CLAUDE_API_KEY";
    private static final String DEFAULT_MODEL = "claude-3.5"; // change if needed

    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) {
        String userPrompt;
        if (args.length > 0) {
            userPrompt = String.join(" ", args);
        } else {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter a prompt for Claude: ");
            userPrompt = scanner.nextLine();
        }

        String prompt = buildPrompt(userPrompt);
        System.out.println("Sending request to Claude...");
        try {
            String answer = callClaude(prompt);
            System.out.println("\nClaude response:\n");
            System.out.println(answer);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static String getApiKey() {
        String apiKey = System.getenv(API_KEY_ENV);
        if (apiKey == null || apiKey.isEmpty()) {
            throw new RuntimeException(
                "Missing " + API_KEY_ENV + " environment variable. Set it before running the program."
            );
        }
        return apiKey;
    }

    private static String buildPrompt(String userPrompt) {
        return "\n\nHuman: " + userPrompt + "\n\nAssistant: ";
    }

    private static String callClaude(String prompt) throws IOException, InterruptedException {
        String apiKey = getApiKey();
        Map<String, Object> payload = Map.of(
            "model", DEFAULT_MODEL,
            "prompt", prompt,
            "max_tokens_to_sample", 300,
            "temperature", 0.7
        );

        String jsonPayload = objectMapper.writeValueAsString(payload);

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(API_URL))
            .header("Content-Type", "application/json")
            .header("X-API-Key", apiKey)
            .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
            .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        response.body();

        if (response.statusCode() != 200) {
            throw new RuntimeException("HTTP error: " + response.statusCode() + " - " + response.body());
        }

        JsonNode jsonResponse = objectMapper.readTree(response.body());
        if (jsonResponse.has("completion")) {
            return jsonResponse.get("completion").asText().trim();
        } else if (jsonResponse.has("output")) {
            return jsonResponse.get("output").asText().trim();
        } else {
            throw new RuntimeException("Unexpected response format: " + response.body());
        }
    }
}