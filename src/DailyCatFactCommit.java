import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

public class DailyCatFactCommit {
    private static final String CAT_FACT_API_URL = "https://catfact.ninja/fact";
    private static final String REPO_PATH = "/Users/matthewbixby/Public/IntelliJ-daily/daily-cat-fact-committer";

    public static void main(String[] args) {
        try {
            // Get current date
            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = today.format(formatter);

            // Fetch daily cat fact
            String fact = fetchDailyCatFact();

            // Create the daily fact file
            String fileName = "daily_cat_fact_" + formattedDate + ".md";
            String filePath = REPO_PATH + "/daily-facts/" + fileName;

            // Write content to file
            String fileContent = String.format("""
                # Daily Cat Fact - %s
                
                > %s
                
                Generated automatically using the [Cat Facts API](https://catfact.ninja)
                """, formattedDate, fact);

            Files.write(Paths.get(filePath),
                    fileContent.getBytes(),
                    StandardOpenOption.CREATE);

            // Execute Git commands
            executeGitCommands(fileName, formattedDate);

            System.out.println("Successfully committed daily cat fact for " + formattedDate);

        } catch (Exception e) {
            System.err.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static String fetchDailyCatFact() throws IOException, InterruptedException {
        // Try up to 3 times with increasing delay
        for (int attempt = 1; attempt <= 3; attempt++) {
            try {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(CAT_FACT_API_URL))
                        .build();

                HttpResponse<String> response = client.send(request,
                        HttpResponse.BodyHandlers.ofString());

                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(response.body());

                return root.get("fact").asText();
            } catch (Exception e) {
                System.err.println("Attempt " + attempt + " failed: " + e.getMessage());
                if (attempt < 3) {
                    // Exponential backoff - wait longer between attempts
                    Thread.sleep(1000 * attempt * attempt);
                } else {
                    // If all attempts fail, use a fallback fact
                    return "Cats have five toes on their front paws and four on their back paws. (FALLBACK - API unavailable)";
                }
            }
        }
        // This line should never be reached due to the fallback above
        return "Unable to fetch cat fact";
    }

    private static void executeCommand(ProcessBuilder pb, String... command) throws IOException, InterruptedException {
        pb.command(command);
        Process process = pb.start();

        // Capture output and error
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

        // Read the output
        String outputLine;
        StringBuilder output = new StringBuilder();
        while ((outputLine = stdInput.readLine()) != null) {
            output.append(outputLine).append("\n");
        }

        // Read any errors
        String errorLine;
        StringBuilder error = new StringBuilder();
        while ((errorLine = stdError.readLine()) != null) {
            error.append(errorLine).append("\n");
        }

        // Wait for the command to complete and check exit value
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            System.err.println("Command output: " + output);
            System.err.println("Command error: " + error);
            throw new IOException("Command failed with exit code: " + exitCode);
        }
    }
}