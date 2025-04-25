import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class Fetch {

     public static void main(String[] args) {

        List<String> urls = readUrlsFromJson("URL.json");
        if (urls != null && !urls.isEmpty()) {
            String aspoUrl = urls.get(0);
            System.out.println("Repository URL from JSON: " + aspoUrl);

            String destination = "/calculate/Repo/"; // Adjusted path for Docker

            // Ensure aspoUrl is not null before attempting to clone
            if (aspoUrl != null && !aspoUrl.isEmpty()) {
                cloneRepository(aspoUrl, destination);
            } else {
                System.out.println("Repository URL is empty or null in the JSON file.");
            }
        } else {
            System.out.println("Could not read URLs from JSON file or the list is empty.");
        }
    }

    public static void cloneRepository(String repoUrl, String destination) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("git", "clone", repoUrl, destination);
            Process process = processBuilder.start();

            // Capture output (optional)
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // Capture errors (optional)
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while ((line = errorReader.readLine()) != null) {
                System.err.println(line);
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Repository cloned successfully to: " + destination);
            } else {
                System.err.println("Failed to clone repository. Exit code: " + exitCode);
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static List<String> readUrlsFromJson(String filePath) {
        List<String> urls = new ArrayList<>();
        StringBuilder content = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line.trim());
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return null;
        }

        String jsonString = content.toString();

        // Look for the "url" key and extract the array
        int urlKeyIndex = jsonString.indexOf("\"url\"");
        if (urlKeyIndex != -1) {
            int arrayStartIndex = jsonString.indexOf("[", urlKeyIndex);
            int arrayEndIndex = jsonString.indexOf("]", arrayStartIndex);

            if (arrayStartIndex != -1 && arrayEndIndex != -1 && arrayStartIndex < arrayEndIndex) {
                String urlsArrayString = jsonString.substring(arrayStartIndex + 1, arrayEndIndex);
                String[] urlElements = urlsArrayString.split(",");
                for (String element : urlElements) {
                    String url = element.trim().replaceAll("\"", "");
                    if (!url.isEmpty()) {
                        urls.add(url);
                    }
                }
            }
        }

        return urls;
    }
}
