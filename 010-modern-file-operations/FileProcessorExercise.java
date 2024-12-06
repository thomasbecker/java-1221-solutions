import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileProcessorExercise {

    static class ConfigFileProcessor {
        private final Path configDir;

        public ConfigFileProcessor(String directory) {
            this.configDir = Path.of(directory);
            // Create directory if it doesn't exist
            try {
                Files.createDirectories(configDir);
            } catch (IOException e) {
                System.err.println("Failed to create directory: " + e.getMessage());
            }
        }

        public String readConfig(String filename) throws IOException {
            return Files.readString(configDir.resolve(filename));
        }

        public void writeConfig(String filename, String content) throws IOException {
            Files.writeString(configDir.resolve(filename), content);
        }

        public boolean areConfigsIdentical(String file1, String file2) throws IOException {
            return Files.mismatch(
                    configDir.resolve(file1),
                    configDir.resolve(file2)
            ) == -1;
        }

        public void backupConfig(String filename) throws IOException {
            Path source = configDir.resolve(filename);
            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            Path backup = configDir.resolve(filename + "." + timestamp + ".bak");

            Files.copy(source, backup, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    public static void main(String[] args) {
        ConfigFileProcessor processor = new ConfigFileProcessor("configs");

        try {
            // Write test configs
            processor.writeConfig("app.config", """
                    server.port=8080
                    db.url=localhost
                    db.name=myapp""");

            processor.writeConfig("app2.config", """
                    server.port=8080
                    db.url=localhost
                    db.name=myapp2""");

            // Read and display config
            System.out.println("Config content:");
            System.out.println(processor.readConfig("app.config"));

            // Compare configs
            boolean identical = processor.areConfigsIdentical(
                    "app.config",
                    "app2.config"
            );
            System.out.println("\nConfigs identical: " + identical);

            // Create backup
            processor.backupConfig("app.config");
            System.out.println("\nBackup created successfully");

        } catch (IOException e) {
            System.err.println("File operation failed: " + e.getMessage());
        }
    }
}
