package sample;

import java.util.HashMap;
import java.util.Map;

import java.util.*;

public class Trie {
    private final TrieNode root;

    public Trie() {
        root = new TrieNode();
    }

    // Insert a word into the Trie
    public void insert(String word) {
        TrieNode current = root;
        for (char c : word.toCharArray()) {
            current.children.putIfAbsent(c, new TrieNode());
            current = current.children.get(c);
        }
        current.isEndOfWord = true;
    }

    // Search for an exact word in the Trie
    public boolean search(String word) {
        TrieNode current = root;
        for (char c : word.toCharArray()) {
            if (!current.children.containsKey(c)) return false;
            current = current.children.get(c);
        }
        return current.isEndOfWord;
    }

    // Get all suggestions (words) for a given prefix
    public List<String> getWordsForPrefix(String prefix) {
        TrieNode current = root;
        for (char c : prefix.toCharArray()) {
            if (!current.children.containsKey(c)) return new ArrayList<>();
            current = current.children.get(c);
        }
        List<String> results = new ArrayList<>();
        findAllWords(current, new StringBuilder(prefix), results);
        return results;
    }

    private void findAllWords(TrieNode node, StringBuilder prefix, List<String> results) {
        if (node.isEndOfWord) {
            results.add(prefix.toString());
        }
        for (Map.Entry<Character, TrieNode> entry : node.children.entrySet()) {
            findAllWords(entry.getValue(), prefix.append(entry.getKey()), results);
            prefix.deleteCharAt(prefix.length() - 1);
        }
    }

    // Get ranked suggestions based on edit distance
    public List<String> getSuggestionsWithEditDistance(String input, int maxSuggestions) {
        List<String> allWords = getWordsForPrefix("");
        PriorityQueue<Suggestion> rankedSuggestions = new PriorityQueue<>(Comparator.comparingInt(s -> s.editDistance));

        for (String word : allWords) {
            int distance = calculateEditDistance(input, word);
            rankedSuggestions.add(new Suggestion(word, distance));
        }

        List<String> result = new ArrayList<>();
        for (int i = 0; i < maxSuggestions && !rankedSuggestions.isEmpty(); i++) {
            result.add(rankedSuggestions.poll().word);
        }
        return result;
    }

    // Levenshtein Distance Algorithm
    private int calculateEditDistance(String source, String target) {
        int m = source.length(), n = target.length();
        int[][] dp = new int[m + 1][n + 1];

        for (int i = 0; i <= m; i++) dp[i][0] = i;
        for (int j = 0; j <= n; j++) dp[0][j] = j;

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (source.charAt(i - 1) == target.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(dp[i - 1][j - 1], Math.min(dp[i - 1][j], dp[i][j - 1]));
                }
            }
        }

        return dp[m][n];
    }

    // Helper class for ranking suggestions
    private static class Suggestion {
        String word;
        int editDistance;

        Suggestion(String word, int editDistance) {
            this.word = word;
            this.editDistance = editDistance;
        }
    }

    // Nested TrieNode class
    private static class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>();
        boolean isEndOfWord = false;
    }
}
