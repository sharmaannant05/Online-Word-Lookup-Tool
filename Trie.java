package sample;

import java.util.HashMap;
import java.util.Map;

class TrieNode {
    Map<Character, TrieNode> children = new HashMap<>();
    boolean isEndOfWord = false;
}

public class Trie {
    private final TrieNode root;

    public Trie() {
        root = new TrieNode();
    }

    // Insert a word into the Trie
    public void insert(String word) {
        TrieNode current = root;
        for (char ch : word.toCharArray()) {
            current.children.putIfAbsent(ch, new TrieNode());
            current = current.children.get(ch);
        }
        current.isEndOfWord = true;
    }

    // Search for an exact word in the Trie
    public boolean search(String word) {
        TrieNode current = root;
        for (char ch : word.toCharArray()) {
            current = current.children.get(ch);
            if (current == null) {
                return false;
            }
        }
        return current.isEndOfWord;
    }

    // Suggest words with a given prefix
    public void autoComplete(TrieNode node, String prefix, StringBuilder result) {
        if (node.isEndOfWord) {
            result.append(prefix).append("\n");
        }
        for (Map.Entry<Character, TrieNode> entry : node.children.entrySet()) {
            autoComplete(entry.getValue(), prefix + entry.getKey(), result);
        }
    }

    public String getSuggestions(String prefix) {
        TrieNode current = root;
        for (char ch : prefix.toCharArray()) {
            current = current.children.get(ch);
            if (current == null) {
                return "No suggestions";
            }
        }
        StringBuilder result = new StringBuilder();
        autoComplete(current, prefix, result);
        return result.toString();
    }
}
