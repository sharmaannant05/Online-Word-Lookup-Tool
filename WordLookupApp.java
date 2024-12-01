package sample;

import java.util.Scanner;

public class WordLookupApp {
    public static void main(String[] args) {
        Trie trie = new Trie();

        // Sample word dataset
        trie.insert("hello");
        trie.insert("help");
        trie.insert("hero");
        trie.insert("hell");
        trie.insert("house");

        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Word Lookup Tool!");

        while (true) {
            System.out.println("\nEnter a word to search (or type 'exit' to quit): ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("exit")) {
                break;
            }

            if (trie.search(input)) {
                System.out.println("Word found in the Trie!");
                try {
                    System.out.println("Definition: " + WordnikAPI.fetchDefinition(input));
                } catch (Exception e) {
                    System.out.println("Error fetching definition from Wordnik API: " + e.getMessage());
                }
            } else {
                System.out.println("Word not found. Did you mean:");
                System.out.println(trie.getSuggestionsWithEditDistance(input,5));
                System.out.println("Enter your correction:");
                String correctedWord = scanner.nextLine().trim();

                if (trie.search(correctedWord)) {
                    try {
                        System.out.println("Definition: " + WordnikAPI.fetchDefinition(correctedWord));
                    } catch (Exception e) {
                        System.out.println("Error fetching definition from Wordnik API: " + e.getMessage());
                    }
                } else {
                    System.out.println("Sorry, no matches found.");
                }
            }
        }
        scanner.close();
        System.out.println("Goodbye!");
    }
}
