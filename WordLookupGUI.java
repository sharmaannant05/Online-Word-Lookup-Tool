package sample;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.*;
import java.util.Scanner;

public class WordLookupGUI extends Application {
    private Trie trie = new Trie();
    private static final String LOCAL_DATA_FILE = "words_dataset.txt";

    @Override
    public void start(Stage primaryStage) {
        // Layout elements
        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20;");

        Label statusLabel = new Label("Loading words into Trie...");
        ProgressBar progressBar = new ProgressBar(0);
        TextField inputField = new TextField();
        inputField.setPromptText("Enter a word...");
        Button searchButton = new Button("Search");
        Button suggestionsButton = new Button("Suggestions");
        Button clearButton = new Button("Clear");
        TextArea resultArea = new TextArea();
        resultArea.setEditable(false);

        // Add components to layout
        layout.getChildren().addAll(statusLabel, progressBar, inputField, searchButton, suggestionsButton, clearButton, resultArea);

        // Load words into Trie (with progress bar) in a background thread
        Task<Void> loadTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                try {
                    int totalWords = 0;
                    if (new File(LOCAL_DATA_FILE).exists()) {
                        // Load from local file if it exists
                        Scanner scanner = new Scanner(new File(LOCAL_DATA_FILE));
                        while (scanner.hasNextLine()) {
                            trie.insert(scanner.nextLine().trim().toLowerCase());
                            totalWords++;
                            updateProgress(totalWords, 500);
                        }
                        scanner.close();
                        updateMessage("Loaded words from local file!");
                    } else {
                        // Fetch words from Wordnik API if local file is unavailable
                        String[] words = WordnikAPI.fetchWordList(500); // Fetch 500 words
                        BufferedWriter writer = new BufferedWriter(new FileWriter(LOCAL_DATA_FILE));
                        for (String word : words) {
                            trie.insert(word.trim().toLowerCase());
                            writer.write(word.trim().toLowerCase() + "\n");
                            totalWords++;
                            updateProgress(totalWords, 500);
                        }
                        writer.close();
                        updateMessage("Loaded words from Wordnik API and saved locally!");
                    }
                } catch (Exception e) {
                    updateMessage("Error loading words: " + e.getMessage());
                }
                return null;
            }
        };

        // Update UI with progress and status
        progressBar.progressProperty().bind(loadTask.progressProperty());
        statusLabel.textProperty().bind(loadTask.messageProperty());

        new Thread(loadTask).start();

        // Button actions
        searchButton.setOnAction(event -> {
            String word = inputField.getText().trim().toLowerCase();
            if (trie.search(word)) {
                try {
                    String definition = WordnikAPI.fetchDefinition(word);
                    resultArea.setText("Word Found!\nDefinition: " + definition);
                } catch (Exception e) {
                    resultArea.setText("Error fetching definition: " + e.getMessage());
                }
            } else {
                resultArea.setText("Word not found. Try getting suggestions!");
            }
        });

        suggestionsButton.setOnAction(event -> {
            String word = inputField.getText().trim().toLowerCase();
            String suggestions = trie.getSuggestions(word);
            resultArea.setText("Suggestions:\n" + suggestions);
        });

        clearButton.setOnAction(event -> {
            inputField.clear();
            resultArea.clear();
        });

        // Set up the stage
        Scene scene = new Scene(layout, 500, 400);
        primaryStage.setTitle("Word Lookup Tool");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

