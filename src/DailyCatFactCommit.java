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
            String filePath = REPO_PATH + "/" + fileName;

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
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(CAT_FACT_API_URL))
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.body());

        return root.get("fact").asText();
    }

    private static void executeGitCommands(String fileName, String date) throws IOException, InterruptedException {
        // Change to repository directory
        ProcessBuilder pb = new ProcessBuilder();
        pb.directory(new File(REPO_PATH));

        // Execute Git commands
        executeCommand(pb, "git", "pull", "origin", "main");
        executeCommand(pb, "git", "add", fileName);
        executeCommand(pb, "git", "commit", "-m", "Added daily cat fact for " + date);
        executeCommand(pb, "git", "push", "origin", "main");
    }

    private static void executeCommand(ProcessBuilder pb, String... command) throws IOException, InterruptedException {
        pb.command(command);
        Process process = pb.start();

        // Wait for the command to complete and check exit value
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new IOException("Command failed with exit code: " + exitCode);
        }
    }
}