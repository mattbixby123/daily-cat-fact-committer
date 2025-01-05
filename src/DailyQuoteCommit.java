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

public class DailyQuoteCommit {
    private static final String QUOTES_API_URL = "https://api.quotable.io/random";
    private static final String REPO_PATH = "/path/to/your/repo"; // Update this with your repo path

    public static void main(String[] args) {
        try {
            // Get current date
            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = today.format(formatter);

            // Fetch daily quote
            String quote = fetchDailyQuote();

            // Create the daily quote file
            String fileName = "daily_quote_" + formattedDate + ".md";
            String filePath = REPO_PATH + "/" + fileName;

            // Write content to file
            String fileContent = String.format("""
                # Daily Quote - %s
                
                > %s
                
                Generated automatically using the [Quotable API](https://api.quotable.io)
                """, formattedDate, quote);

            Files.write(Paths.get(filePath),
                    fileContent.getBytes(),
                    StandardOpenOption.CREATE);

            // Execute Git commands
            executeGitCommands(fileName, formattedDate);

            System.out.println("Successfully committed daily quote for " + formattedDate);

        } catch (Exception e) {
            System.err.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static String fetchDailyQuote() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(QUOTES_API_URL))
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.body());

        return String.format("%s - %s",
                root.get("content").asText(),
                root.get("author").asText());
    }

    private static void executeGitCommands(String fileName, String date) throws IOException, InterruptedException {
        // Change to repository directory
        ProcessBuilder pb = new ProcessBuilder();
        pb.directory(new File(REPO_PATH));

        // Execute Git commands
        executeCommand(pb, "git", "pull", "origin", "main");
        executeCommand(pb, "git", "add", fileName);
        executeCommand(pb, "git", "commit", "-m", "Added daily quote for " + date);
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